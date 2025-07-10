package com.singhankit.jhttp.interceptor;

import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpRequest;

/**
 * @author Ankit Singh
 */
public interface RequestInterceptor {

    boolean intercept(HttpRequest request) throws HttpException;

    default int order(){
        return 0;
    }
}
