package com.singhankit.jhttp;

import com.singhankit.jhttp.internal.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Ankit Singh
 */
public class HttpHeaders implements Iterable<Map.Entry<String, String>> {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
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

    public HttpHeaders add(String key, String value) {
        if(Util.isEmpty(key) || Util.isEmpty(value)) {
            throw new IllegalArgumentException("Header key/value cannot be empty");
        }
        headers.put(key, value);
        return this;
    }

    public HttpHeaders addAll(HttpHeaders headers) {
        if(Objects.nonNull(headers)) {
            headers.forEach(e -> this.add(e.getKey(), e.getValue()));
        }
        return this;
    }

    public HttpHeaders addAll(Map<String, String> headers) {
        headers.forEach(this::add);
        return this;
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(headers.get(key));
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        return headers.entrySet().iterator();
    }
}
