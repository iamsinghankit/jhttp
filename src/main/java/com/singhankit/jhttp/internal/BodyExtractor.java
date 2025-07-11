package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.Body;
import com.singhankit.jhttp.Body.FilePart;
import com.singhankit.jhttp.HttpClientException;
import com.singhankit.jhttp.HttpHeaders;
import com.singhankit.jhttp.HttpStatus;
import com.singhankit.jhttp.MediaType;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.singhankit.jhttp.HttpHeaders.CONTENT_LENGTH;
import static com.singhankit.jhttp.HttpHeaders.CONTENT_TYPE;

/**
 * @author ankitsingh
 */
class BodyExtractor {

    private final TCPHandler tcpHandler;

    BodyExtractor(TCPHandler tcpHandler) {
        this.tcpHandler = tcpHandler;
    }

    Body extract(HttpHeaders headers) {
        var contentLengthOpt = headers.get(CONTENT_LENGTH);
        if(contentLengthOpt.isEmpty()) {
            return null;
        }
        int cl = Integer.parseInt(contentLengthOpt.get());
        if(cl <= 0) {
            return null;
        }
        var contentType = headers.get(CONTENT_TYPE)
                                 .orElseThrow(() -> new HttpClientException(HttpStatus.BAD_REQUEST, "Content-Type is missing"));
        try {
            String raw = tcpHandler.readString(cl);
            return doExtract(contentType, raw);
        } catch(IOException e) {
            throw new HttpClientException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private Body doExtract(String contentType, String bodyText) {
        Body body = new Body();
        body.setRaw(bodyText);
        body.setContentType(contentType);

        if(contentType == null) return body;

        if(contentType.startsWith(MediaType.URLENCODED.value())) {
            parseUrlEncoded(bodyText, body);
        } else if(contentType.startsWith(MediaType.MULTIPART_FORM_DATA.value())) {
            String boundary = getBoundary(contentType);
            if(boundary != null) {
                parseMultipart(bodyText, boundary, body);
            }
        } else if(contentType.equals(MediaType.BINARY.value())) {
            // Treat raw body as binary file
            byte[] fileBytes = bodyText.getBytes(StandardCharsets.ISO_8859_1); // or UTF-8 if content is text
            var file = new FilePart("", fileBytes);
            body.getFileFields().put("file", file); // single file with default field name
        }

        // Else: Keep as raw (could be JSON, XML, etc.)
        return body;
    }

    private void parseUrlEncoded(String bodyText, Body body) {
        for(String pair : bodyText.split("&")) {
            String[] kv = pair.split("=", 2);
            if(kv.length == 2) {
                body.getFormFields().put(decode(kv[0]), decode(kv[1]));
            }
        }
    }

    private String decode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    private String getBoundary(String contentType) {
        int idx = contentType.indexOf("boundary=");
        if(idx != -1) {
            return "--" + contentType.substring(idx + 9).trim();
        }
        return null;
    }

    private void parseMultipart(String bodyText, String boundary, Body body) {
        String[] parts = bodyText.split(boundary);
        for(String part : parts) {
            if(part.isBlank() || part.equals("--") || part.equals("--\r\n")) continue;

            String[] sections = part.split("\r\n\r\n", 2);
            if(sections.length != 2) continue;

            String headers = sections[0].trim();
            String value = sections[1];

            String name = null;
            String filename = null;
            String contentType = MediaType.TEXT.value();

            for(String headerLine : headers.split("\r\n")) {
                headerLine = headerLine.toLowerCase();
                if(headerLine.startsWith("content-disposition")) {
                    name = extractQuoted(headerLine, "name");
                    filename = extractQuoted(headerLine, "filename");
                } else if(headerLine.startsWith(CONTENT_TYPE)) {
                    String[] kv = headerLine.split(":", 2);
                    if(kv.length == 2) contentType = kv[1].trim();
                }
            }

            value = value.replaceAll("(\r\n)?--$", "").strip();

            if(name == null) continue;

            if(filename != null) {
                byte[] content = value.getBytes(StandardCharsets.ISO_8859_1);
                body.getFileFields().put(name, new FilePart(filename, content));
            } else {
                body.getFormFields().put(name, value);
            }
        }
    }

    private String extractQuoted(String header, String key) {
        int start = header.indexOf(key + "=\"");
        if(start == -1) return null;
        start += key.length() + 2;
        int end = header.indexOf("\"", start);
        return (end > start) ? header.substring(start, end) : null;
    }
}
