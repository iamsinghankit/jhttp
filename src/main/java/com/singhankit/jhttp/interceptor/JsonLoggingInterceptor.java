package com.singhankit.jhttp.interceptor;

import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ankit Singh
 */
public class JsonLoggingInterceptor implements RequestInterceptor, ResponseInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(JsonLoggingInterceptor.class);
    private final String requestJson = """
          {
            "request": {
                "method": "%s",
                "path": "%s",
                "headers": %s,
                "pathVariables": %s,
                "params": %s
            }
          }
          """;

    private final String responseJson = """
          {
           "response": {
               "method": "%s",
               "path": "%s",
               "status": %d,
               "headers": %s,
               "body": %s
            }
          }
          """;

    @Override
    public boolean intercept(HttpRequest request) throws HttpException {
        if(!LOG.isDebugEnabled()) {
            return true;
        }
        String msg = requestJson.formatted(request.method(), request.path(), request.headers(), Util.toJson(request.pathVariables()), Util.toJson(request.params()));
        LOG.debug(msg);
        return true;
    }

    @Override
    public boolean intercept(HttpRequest request, HttpResponse response) throws HttpException {
        if(!LOG.isDebugEnabled()) {
            return true;
        }
        String msg = responseJson.formatted(request.method(), request.path(),response.status().code(), response.headers(), response.body());
        LOG.debug(msg);
        return true;
    }



    @Override
    public int order() {
        return Integer.MIN_VALUE + 3;
    }
}
