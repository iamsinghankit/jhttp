package com.singhankit.jhttp;

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
    public static final String AUTHORIZATION = "Authorization";
    private final Map<String, String> headers;

    public HttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public HttpHeaders() {
        this(new HashMap<>());
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

    public boolean remove(String key) {
        return headers.remove(key) != null;
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(headers.get(key));
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        return headers.entrySet().iterator();
    }

    public String toHttpString(){
        var res = new StringBuilder();
        headers.forEach((k,v) -> res.append(k).append(":").append(v).append("\r\n"));
        return res.toString();
    }

    @Override
    public String toString() {
        return Util.toJson(headers);
    }
}
