package com.singhankit.jhttp;

/**
 * @author Ankit Singh
 */
public interface RequestInterceptor {

    boolean intercept(HttpRequest request);
}
