package com.singhankit.jhttp;

import java.util.Objects;

/**
 * @author Ankit Singh
 */
public class HttpServerException extends HttpException {

    public HttpServerException(HttpStatus status) {
        super(validate(status));
    }

    public HttpServerException(HttpStatus status, String message) {
        super(validate(status), message);
    }

    private static HttpStatus validate(HttpStatus status) {
        if(Objects.isNull(status) || !status.isServerError()) {
            throw new IllegalArgumentException("Invalid http status");
        }
        return status;
    }
}
