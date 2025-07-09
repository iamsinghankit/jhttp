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
interface RequestHandler {
    String SPACE = " ";
    String SEP = ":";
    String LINE_END = "\r\n";
    String HTTP_VERSION = "HTTP/1.1";

    String ERROR_JSON = """
            {
              "error":"%s",
              "reason":"%s",
            }
            """;

    void handle();

    default String generateErrorRes(HttpHeaders httpHeaders, HttpException ex) {
        String errorJson = ERROR_JSON.formatted(ex.status().description(), ex.getMessage());
        httpHeaders.add(CONTENT_TYPE, MediaType.APPLICATION_JSON.value());
        httpHeaders.add(CONTENT_LENGTH, String.valueOf(errorJson.length()));
        return genResLine(ex.status()) + addResHeaders(httpHeaders) + LINE_END + errorJson;
    }

    default String generateRes(HttpResponse res) {
        var headers = Objects.isNull(res.headers()) ? new HttpHeaders() : res.headers();
        if(Util.isNotEmpty(res.body())) {
            headers.get(CONTENT_TYPE).orElseThrow(() -> new HttpServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Content-Type is missing"));
            headers.add(CONTENT_LENGTH, String.valueOf(res.body().length()));
            return genResLine(res.status()) + addResHeaders(headers) + LINE_END + res.body();
        }
        else{
            headers.remove(CONTENT_TYPE);
            headers.remove(CONTENT_LENGTH);
        }
        return genResLine(res.status()) + addResHeaders(headers) + LINE_END;
    }

    private String genResLine(HttpStatus status) {
        return HTTP_VERSION + SPACE + status.code() + SPACE + status.description() + LINE_END;
    }

    private String addResHeaders(HttpHeaders headers) {
        headers.add("Connection", "close");
        headers.add("Server", "JHttp");
        var res = new StringBuilder();
        headers.forEach(e -> res.append(e.getKey()).append(SEP).append(e.getValue()).append(LINE_END));
        return res.toString();
    }
}
