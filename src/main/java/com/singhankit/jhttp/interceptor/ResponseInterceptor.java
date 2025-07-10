package com.singhankit.jhttp.interceptor;

import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpResponse;

/**
 * @author Ankit Singh
 */
public interface ResponseInterceptor {

    boolean intercept(HttpRequest request, HttpResponse response) throws HttpException;

    int order();
}
