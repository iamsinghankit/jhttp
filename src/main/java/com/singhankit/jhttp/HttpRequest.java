package com.singhankit.jhttp;

import com.singhankit.jhttp.internal.Body;

import java.util.Map;

/**
 * @author Ankit Singh
 */
public record HttpRequest(HttpHeaders headers, Body body, Map<String, String> pathVariables,
                          Map<String, String> params) {}

