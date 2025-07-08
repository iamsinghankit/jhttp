package com.singhankit.jhttp;

/**
 * @author Ankit Singh
 */
public interface HttpRequestHandler {

    HttpResponse handle(HttpRequest request);
}
