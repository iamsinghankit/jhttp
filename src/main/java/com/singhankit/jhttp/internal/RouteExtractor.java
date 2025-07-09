package com.singhankit.jhttp.internal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ankit Singh
 */
 class RouteExtractor {

    private final Map<String, String> pathVariables = new HashMap<>();
    private final Map<String, String> queryParams = new HashMap<>();

     Map<String, String> getPathVariables() {
        return pathVariables;
    }

     Map<String, String> getQueryParams() {
        return queryParams;
    }

    /**
     * Extracts path variables and query parameters from a given path.
     * @param patternPattern e.g. /user/{id}/order/{orderId}
     * @param fullPath e.g. /user/42/order/99?include=summary&limit=10
     * @return true if path matches the pattern, false otherwise
     */
     boolean extract(String patternPattern, String fullPath) {
        pathVariables.clear();
        queryParams.clear();

        // Split path and query string
        String[] split = fullPath.split("\\?", 2);
        String actualPath = normalize(split[0]);
        if (split.length > 1) {
            parseQueryParams(split[1]);
        }

        return matchPath(normalize(patternPattern), actualPath);
    }

    private String normalize(String path) {
        return path.equals("/") ? "/" : path.replaceAll("/+$", "");
    }

    private boolean matchPath(String pattern, String actualPath) {
        String[] patternParts = pattern.split("/");
        String[] actualParts = actualPath.split("/");

        if (patternParts.length != actualParts.length) return false;

        for (int i = 0; i < patternParts.length; i++) {
            if (patternParts[i].startsWith("{") && patternParts[i].endsWith("}")) {
                String varName = patternParts[i].substring(1, patternParts[i].length() - 1);
                pathVariables.put(varName, actualParts[i]);
            } else if (!patternParts[i].equals(actualParts[i])) {
                return false;
            }
        }
        return true;
    }

    private void parseQueryParams(String query) {
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                queryParams.put(decode(kv[0]), decode(kv[1]));
            } else if (kv.length == 1) {
                queryParams.put(decode(kv[0]), "");
            }
        }
    }

    private String decode(String s) {
        return s.replace("+", " ").replace("%20", " "); // basic decoding
    }
}
