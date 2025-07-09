package com.singhankit.jhttp;

/**
 * @author Ankit Singh
 */
public enum HttpMethod {

    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    OPTIONS,
    PATCH,
    TRACE,
    UNKNOWN;


    public static HttpMethod of(String method) {
        return switch(method) {
            case "GET" -> HttpMethod.GET;
            case "POST" -> HttpMethod.POST;
            case "PUT" -> HttpMethod.PUT;
            case "DELETE" -> HttpMethod.DELETE;
            case "HEAD" -> HttpMethod.HEAD;
            case "OPTIONS" -> HttpMethod.OPTIONS;
            case "PATCH" -> HttpMethod.PATCH;
            case "TRACE" -> HttpMethod.TRACE;
            case null, default -> HttpMethod.UNKNOWN;
        };
    }

    public boolean equalCheck(String value){
        return this.name().equalsIgnoreCase(value);
    }
}
