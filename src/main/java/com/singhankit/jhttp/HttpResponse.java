package com.singhankit.jhttp;

import java.util.Objects;

/**
 * @author Ankit Singh
 */
public record HttpResponse(HttpHeaders headers, HttpStatus status, String body) {

    public HttpResponse {
        if(Objects.isNull(status)) {
            throw new HttpServerException(HttpStatus.INTERNAL_SERVER_ERROR, "status is missing");
        }
    }

    public HttpResponse withStatus(HttpStatus status) {
        return new HttpResponse(headers, status, body);
    }


}

