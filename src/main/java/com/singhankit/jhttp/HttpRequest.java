package com.singhankit.jhttp;

import java.util.Map;

/**
 * @author Ankit Singh
 */
public record HttpRequest(HttpHeaders headers, String path, HttpMethod method, Body body,
                          Map<String, String> pathVariables, Map<String, String> params) {}

