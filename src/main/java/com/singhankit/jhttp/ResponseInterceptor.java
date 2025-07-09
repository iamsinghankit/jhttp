package com.singhankit.jhttp;

/**
 * @author Ankit Singh
 */
public interface ResponseInterceptor {

    boolean intercept(HttpResponse response);
}
