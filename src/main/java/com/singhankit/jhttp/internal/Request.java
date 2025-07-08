package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpHeaders;
import com.singhankit.jhttp.HttpMethod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author Ankit Singh
 */
 record Request(BufferedReader in, BufferedWriter out, HttpHeaders headers, String path, HttpMethod method) {

     static Request of(BufferedReader in, BufferedWriter out,String requestLine) throws IOException {
        StringTokenizer tokenizer = new StringTokenizer(requestLine);
        String method = tokenizer.nextToken();
        String path = tokenizer.nextToken();
        return new Request(in, out, HttpHeaders.of(in), path, HttpMethod.of(method));
    }

}
