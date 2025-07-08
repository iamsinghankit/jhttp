package com.singhankit.jhttp;

/**
 * @author Ankit Singh
 */
public record HttpResponse(HttpStatus status, MediaType mediaType,String body) {}
