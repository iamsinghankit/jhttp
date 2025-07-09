package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpClientException;
import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpRequestHandler;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.HttpStatus;
import com.singhankit.jhttp.JHttp;
import com.singhankit.jhttp.RequestMapping;
import com.singhankit.jhttp.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.singhankit.jhttp.HttpHeaders.CONTENT_LENGTH;
import static com.singhankit.jhttp.HttpHeaders.CONTENT_TYPE;

/**
 * @author Ankit Singh
 */
class PostRequestHandler implements RequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(PostRequestHandler.class);
    private final Request req;
    private final JHttp jHttp;

    PostRequestHandler(Request req, JHttp jHttp) {
        this.req = req;
        this.jHttp = jHttp;
    }

    @Override
    public void handle() {
        var response = new StringBuilder();
        try {
            String body = readBody();
            var handlerAndRequest = getHandlerAndRequest(body);
            HttpResponse httpResponse = handlerAndRequest.handler().handle(handlerAndRequest.request());
            response.append(generateRes(httpResponse));
        } catch(HttpException ex) {
            response.append(generateErrorRes(req.headers(), ex));
        }
        try {
            req.out().write(response.toString());
            req.out().flush();
        } catch(Exception e) {
            LOG.error("Error occurred while handling POST request", e);
        }
    }

    private Optional<String> contentType() {
        return req.headers().get(CONTENT_TYPE);
    }


    private String readBody() {
        String body = null;
        var contentLengthOpt = req.headers().get(CONTENT_LENGTH);
        if(contentLengthOpt.isEmpty()) {
            return body;
        }
        int cl = Integer.parseInt(contentLengthOpt.get());
        try {
            if(cl > 0) {
                char[] bodyChars = new char[cl];
                int read = req.in().read(bodyChars);
                body = new String(bodyChars, 0, read);
            }
            return body;
        } catch(IOException e) {
            throw new HttpClientException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private HandlerAndRequest getHandlerAndRequest(String body) {
        if(Util.isNotEmpty(body) && contentType().isEmpty()) {
            throw new HttpClientException(HttpStatus.BAD_REQUEST, "Content-Type is missing");
        }
        RouteExtractor route = new RouteExtractor();
        List<RequestMapping> requestMappings = jHttp.getRequestMappings(req.method().name());
        for(RequestMapping requestMapping : requestMappings) {
            boolean result = route.extract(requestMapping.getPath(), req.path());
            if(result) {
                var request = new HttpRequest(req.headers(), body, route.getPathVariables(), route.getQueryParams());
                return new HandlerAndRequest(requestMapping.getRequestHandler(), request);
            }
        }
        return doGetHandlerAndRequest(body);
    }

    private HandlerAndRequest doGetHandlerAndRequest(String body) {
        List<RequestMapping> mappings = jHttp.getRequestMappings(req.path(), req.method().name());
        if(mappings.isEmpty()) {
            throw new HttpClientException(HttpStatus.NOT_FOUND, "No request mapping found for path: " + req.path());
        } else if(mappings.size() > 1) {
            throw new HttpClientException(HttpStatus.NOT_FOUND, "More than one request mapping found for path: " + req.path());
        } else {
            return new HandlerAndRequest(mappings.getFirst().getRequestHandler(), new HttpRequest(req.headers(), body, Map.of(), Map.of()));
        }
    }

    private record HandlerAndRequest(HttpRequestHandler handler, HttpRequest request) {}


}
