package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpClientException;
import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpRequestHandler;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.HttpStatus;
import com.singhankit.jhttp.JHttp;
import com.singhankit.jhttp.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author Ankit Singh
 */
abstract class ReqBodyLessHandler implements RequestHandler {
    final Request req;
    final JHttp jHttp;

    ReqBodyLessHandler(Request req, JHttp jHttp) {
        this.req = req;
        this.jHttp = jHttp;
    }

    HttpResponse doHandle() {
        var handlerAndRequest = getHandlerAndRequest();
        executeRequestInterceptors(handlerAndRequest.request());
        HttpResponse httpResponse = handlerAndRequest.handler().handle(handlerAndRequest.request());
        executeResponseInterceptors(httpResponse);
        return httpResponse;
    }

    private void executeRequestInterceptors(HttpRequest request) {
        for(var requestInterceptor : jHttp.getRequestInterceptors()) {
            if(requestInterceptor.intercept(request)) {
                continue;
            }
            break;
        }
    }

    private void executeResponseInterceptors(HttpResponse response) {
        for(var responseInterceptor : jHttp.getResponseInterceptors()) {
            if(responseInterceptor.intercept(response)) {
                continue;
            }
            break;
        }
    }

    private HandlerAndRequest getHandlerAndRequest() {
        RouteExtractor route = new RouteExtractor();
        List<RequestMapping> requestMappings = jHttp.getRequestMappings(req.method().name());
        for(RequestMapping requestMapping : requestMappings) {
            boolean result = route.extract(requestMapping.getPath(), req.path());
            if(result) {
                var request = new HttpRequest(req.headers(), requestMapping.getPath(), req.method(), null, route.getPathVariables(), route.getQueryParams());
                return new HandlerAndRequest(requestMapping.getRequestHandler(), request);
            }
        }
        return doGetHandlerAndRequest();
    }

    private HandlerAndRequest doGetHandlerAndRequest() {
        List<RequestMapping> mappings = jHttp.getRequestMappings(req.path(), req.method().name());
        if(mappings.isEmpty()) {
            throw new HttpClientException(HttpStatus.NOT_FOUND, "No request mapping found for path: " + req.path());
        } else if(mappings.size() > 1) {
            throw new HttpClientException(HttpStatus.NOT_FOUND, "More than one request mapping found for path: " + req.path());
        } else {
            var requestMapping = mappings.getFirst();
            return new HandlerAndRequest(requestMapping.getRequestHandler(), new HttpRequest(req.headers(), requestMapping.getPath(), req.method(), null, Map.of(), Map.of()));
        }
    }

    record HandlerAndRequest(HttpRequestHandler handler, HttpRequest request) {}
}
