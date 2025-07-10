package com.singhankit.jhttp.interceptor;

import com.singhankit.jhttp.HttpClientException;
import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpStatus;

import java.util.Set;

/**
 * @author Ankit Singh
 */
public class IPBlockListInterceptor implements RequestInterceptor {

    private final Set<String> blockedIps;

    public IPBlockListInterceptor(Set<String> blockedIps) {
        this.blockedIps = blockedIps;
    }

    @Override
    public boolean intercept(HttpRequest request) throws HttpException {
        String ip = request.headers().get("X-Forwarded-For")
                           .orElseThrow(() -> new HttpClientException(HttpStatus.BAD_REQUEST, "'X-Forwarded-For' header missing"));

        if(blockedIps.contains(ip)) {
            throw new HttpClientException(HttpStatus.FORBIDDEN, "Host not allowed");
        }
        return true;
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }
}
