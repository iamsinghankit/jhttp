package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpHeaders;
import com.singhankit.jhttp.HttpMethod;

/**
 * @author Ankit Singh
 */
record Request(HttpHeaders headers, String path, HttpMethod method) {}
