package com.singhankit.jhttp.interceptor;

import com.singhankit.jhttp.HttpHeaders;
import com.singhankit.jhttp.HttpMethod;
import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpResponse;

/**
 * @author Ankit Singh
 */
public class CorsInterceptor implements ResponseInterceptor {

    private final CorsHeader corsHeader;

    public CorsInterceptor(CorsHeader corsHeader) {
        this.corsHeader = corsHeader;
    }

    @Override
    public boolean intercept(HttpRequest request, HttpResponse response) {
        if(request.method() != HttpMethod.OPTIONS) {
            return true;
        }
        HttpHeaders headers = response.headers();
        headers.add("Access-Control-Allow-Origin", corsHeader.allowOrigins);
        headers.add("Access-Control-Allow-Methods", corsHeader.allowMethods);
        headers.add("Access-Control-Allow-Headers", corsHeader.allowHeaders);
        headers.add("Access-Control-Max-Age", corsHeader.maxAge);
        return true;
    }

    public record CorsHeader(String allowOrigins, String allowMethods, String allowHeaders, String maxAge) {

        public static CorsHeader allowAll() {
            return new CorsHeader("*", "GET,POST,PUT,DELETE,PATCH,OPTIONS,TRACE", "*", "3600");
        }
    }
}
