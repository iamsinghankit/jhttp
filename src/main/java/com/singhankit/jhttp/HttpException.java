package com.singhankit.jhttp;

/**
 * @author Ankit Singh
 */
public class HttpException extends RuntimeException{

    private final HttpStatus status;

    public HttpException(HttpStatus status) {
        this.status = status;
    }

    public HttpException(HttpStatus status,String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus status(){
        return this.status;
    }


}
