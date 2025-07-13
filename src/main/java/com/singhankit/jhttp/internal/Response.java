package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpHeaders;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.HttpServerException;
import com.singhankit.jhttp.HttpStatus;
import com.singhankit.jhttp.MediaType;
import com.singhankit.jhttp.Util;

import java.util.Objects;

import static com.singhankit.jhttp.HttpHeaders.CONTENT_LENGTH;
import static com.singhankit.jhttp.HttpHeaders.CONTENT_TYPE;

/**
 * @author Ankit Singh
 */
 class Response {

    private static final String LINE_END = "\r\n";
    private static final String HTTP_VERSION = "HTTP/1.1";

    private static final String ERROR_JSON = """
            {
              "error":"%s",
              "reason":"%s",
            }
            """;
    public String response;

    Response(String response) {
        this.response = response;
    }

     static Response error(HttpHeaders httpHeaders, HttpException ex) {
        String errorJson = ERROR_JSON.formatted(ex.status().description(), ex.getMessage());
        httpHeaders.add(CONTENT_TYPE, MediaType.APPLICATION_JSON.value());
        return new Response(generateHttpResponse(ex.status(), httpHeaders, errorJson));
    }

    static Response success(HttpResponse res) {
        var headers = Objects.isNull(res.headers()) ? new HttpHeaders() : res.headers();
        if(Util.isNotEmpty(res.body())) {
            return new Response(generateHttpResponse(res.status(), headers, res.body()));
        }
        headers.remove(CONTENT_TYPE);
        headers.remove(CONTENT_LENGTH);
        return new Response(genResLine(res.status(), headers) + headers.toHttpString() + LINE_END);
    }

    static private String generateHttpResponse(HttpStatus status, HttpHeaders headers, String body) {
        headers.get(CONTENT_TYPE)
               .orElseThrow(() -> new HttpServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Content-Type is missing"));
        headers.add(CONTENT_LENGTH, String.valueOf(body.length()));
        return genResLine(status, headers) + headers.toHttpString() + LINE_END + body;
    }

    static private String genResLine(HttpStatus status, HttpHeaders headers) {
        headers.add("Connection", "close");
        headers.add("Server", "JHttp");
        return HTTP_VERSION + " " + status.code() + " " + status.description() + LINE_END;
    }

    public String toHttpString() {
        return response;
    }
}
