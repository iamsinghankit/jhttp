package com.singhankit.jhttp;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/// A utility class for common helper methods used throughout the HTTP server.
///
/// @author Ankit Singh
public class Util {

    /**
     * is Empty method
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static void requireNonNull(Object obj, String message) {
        if(Objects.isNull(obj)) {
            throw new HttpServerException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
    }

    public static void requireNull(Object obj, String message) {
        if(Objects.nonNull(obj)) {
            throw new HttpServerException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
    }

    public static String toJson(Map<String, String> map) {
        if(Objects.isNull(map) || map.isEmpty()) {
            return "{}";
        }
        return map.entrySet().stream()
                  .map(e -> "\"" + e.getKey() + "\"" + ":" + "\"" + e.getValue() + "\"")
                  .collect(Collectors.joining(", ", "{", "}"));
    }

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch(IOException _) {}
    }

    public static void requireNonEmpty(Collection<?> collection, String msg) {
        if(collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }
}
