package com.singhankit.jhttp;

import java.util.Objects;

/**
 * @author Ankit Singh
 */
public class HttpClientException extends HttpException {

    public HttpClientException(HttpStatus status) {
        super(validate(status));
    }

    public HttpClientException(HttpStatus status, String message) {
        super(validate(status), message);
    }

    private static HttpStatus validate(HttpStatus status) {
        if(Objects.isNull(status) || !status.isClientError()) {
            throw new IllegalArgumentException("Invalid http status");
        }
        return status;
    }
}
