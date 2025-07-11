package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpHeaders;
import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.HttpStatus;
import com.singhankit.jhttp.JHttp;
import com.singhankit.jhttp.Util;

import java.util.Map;

/**
 * @author Ankit Singh
 */
class OptionsRequestHandler extends BaseReqBodyLessHandler {

    OptionsRequestHandler(TCPHandler tcpHandler, Request req, JHttp jHttp) {
        super(tcpHandler, req, jHttp);
    }

    @Override
    public void handle() {
        var response = new StringBuilder();
        try {
            if(isCorsPreflight(req.headers())) {
                var httpRequest = new HttpRequest(req.headers(), req.path(), req.method(), null, Map.of(), Map.of());
                executeRequestInterceptors(httpRequest);
                var httpResponse = new HttpResponse(req.headers(), HttpStatus.NO_CONTENT, null);
                executeResponseInterceptors(httpRequest, httpResponse);
                response.append(generateHttpResponse(httpResponse));
            } else {
                HttpResponse httpResponse = doHandle();
                Util.requireNull(httpResponse.body(), "Response cannot include body");
                response.append(generateHttpResponse(httpResponse));
            }
        } catch(HttpException ex) {
            response.append(generateHttpResponse(req.headers(), ex));
        }
        send(response.toString());
    }

    private boolean isCorsPreflight(HttpHeaders headers) {
        return headers.get("Origin").isPresent() && headers.get("Access-Control-Request-Method").isPresent();
    }
}
