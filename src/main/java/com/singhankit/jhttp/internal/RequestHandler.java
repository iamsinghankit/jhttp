package com.singhankit.jhttp.internal;

/**
 * @author Ankit Singh
 */
interface RequestHandler {
    String LINE_END = "\r\n";
    String HTTP_VERSION = "HTTP/1.1";

    void handle() ;
}
