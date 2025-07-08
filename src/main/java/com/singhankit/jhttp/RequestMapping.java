package com.singhankit.jhttp;

import static java.util.Objects.requireNonNull;

/**
 * @author Ankit Singh
 */
public class RequestMapping {


    private final String path;
    private final HttpMethod method;
    private final HttpRequestHandler requestHandler;
    private final MediaType mediaType;

    private RequestMapping(String path, HttpMethod method, HttpRequestHandler requestHandler, MediaType mediaType) {
        this.path = path;
        this.method = method;
        this.requestHandler = requestHandler;
        this.mediaType = mediaType;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpRequestHandler getRequestHandler() {
        return requestHandler;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public static GETBuilder GET() {
        return new GETBuilder();
    }

    public static POSTBuilder POST() {
        return new POSTBuilder();
    }

    public static class GETBuilder extends RequestMappingBuilder {
        @Override
        public GETBuilder path(String path) {
            return (GETBuilder) super.path(path);
        }

        @Override
        public GETBuilder requestHandler(HttpRequestHandler requestHandler) {
            return (GETBuilder) super.requestHandler(requestHandler);
        }

        public RequestMapping build() {
            requireNonNull(path, "path is required");
            requireNonNull(requestHandler,"request handler is required");
            return new RequestMapping(path, HttpMethod.GET, requestHandler, null);
        }
    }

    public static class POSTBuilder extends RequestMappingBuilder {

        MediaType mediaType;

        public RequestMappingBuilder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        @Override
        public POSTBuilder path(String path) {
            return (POSTBuilder) super.path(path);
        }

        @Override
        public POSTBuilder requestHandler(HttpRequestHandler requestHandler) {
            return (POSTBuilder) super.requestHandler(requestHandler);
        }

        public RequestMapping build() {
            requireNonNull(path, "path is required");
            requireNonNull(mediaType, "mediaType is required");
            requireNonNull(requestHandler,"request handler is required");
            return new RequestMapping(path, HttpMethod.POST, requestHandler, mediaType);
        }
    }

    public abstract static class RequestMappingBuilder {
        String path;
        HttpMethod method;
        HttpRequestHandler requestHandler;

        public RequestMappingBuilder path(String path) {
            this.path = path;
            return this;
        }

        private RequestMappingBuilder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public RequestMappingBuilder requestHandler(HttpRequestHandler requestHandler) {
            this.requestHandler = requestHandler;
            return this;
        }

        public abstract RequestMapping build();
    }
}
