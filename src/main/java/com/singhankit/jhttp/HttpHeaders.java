package com.singhankit.jhttp;

import com.singhankit.jhttp.internal.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Ankit Singh
 */
public class HttpHeaders {

    private final Map<String, String> headers;

    public HttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public HttpHeaders() {
        this(new HashMap<>());
    }

    public static HttpHeaders of(BufferedReader in) throws IOException {
        var headers = new HttpHeaders();
        String line;
        while(!(line = in.readLine()).isEmpty()) {
            int colonPos = line.indexOf(":");
            if(colonPos != -1) {
                String key = line.substring(0, colonPos).trim();
                String value = line.substring(colonPos + 1).trim();
                headers.add(key, value);
            }
        }
        return headers;
    }

    public void add(String key, String value) {
        if(Util.isEmpty(key) || Util.isEmpty(value)) {
            throw new IllegalArgumentException("Header key/value cannot be empty");
        }
        headers.put(key, value);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(headers.get(key));
    }
}
