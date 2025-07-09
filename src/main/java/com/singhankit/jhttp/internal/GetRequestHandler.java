package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpClientException;
import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpRequestHandler;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.HttpStatus;
import com.singhankit.jhttp.JHttp;
import com.singhankit.jhttp.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Ankit Singh
 */
class GetRequestHandler implements RequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GetRequestHandler.class);
    private final Request req;
    private final JHttp jHttp;

    GetRequestHandler(Request req, JHttp jHttp) {
        this.req = req;
        this.jHttp = jHttp;

    }

    @Override
    public void handle() {
        var response = new StringBuilder();
        try {
            var handlerAndRequest = getHandlerAndRequest();
            HttpResponse httpResponse = handlerAndRequest.handler().handle(handlerAndRequest.request());
            response.append(generateRes(httpResponse));
        } catch(HttpException ex) {
            response.append(generateErrorRes(req.headers(), ex));
        }
        try {
            req.out().write(response.toString());
            req.out().flush();
        } catch(IOException ex) {
            LOG.error("Error occurred while handling GET requests", ex);
        }
    }

    private HandlerAndRequest getHandlerAndRequest() {
        RouteExtractor route = new RouteExtractor();
        List<RequestMapping> requestMappings = jHttp.getRequestMappings(req.method().name());
        for(RequestMapping requestMapping : requestMappings) {
            boolean result = route.extract(requestMapping.getPath(), req.path());
            if(result) {
                var request = new HttpRequest(req.headers(), null, route.getPathVariables(), route.getQueryParams());
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
            return new HandlerAndRequest(mappings.getFirst().getRequestHandler(), new HttpRequest(req.headers(), null, Map.of(), Map.of()));
        }
    }

    private record HandlerAndRequest(HttpRequestHandler handler, HttpRequest request) {}
}

