package com.singhankit.jhttp;

import com.singhankit.jhttp.internal.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * @author Ankit Singh
 */
public class JHttp {

    private final Integer port;
    private final List<RequestMapping> requestMappings;
    private final List<RequestInterceptor> requestInterceptors;
    private final List<ResponseInterceptor> responseInterceptors;
    private final ExecutorService serverThreadPool;
    private Server httpServer;

    private JHttp(int port, List<RequestMapping> requestMappings, List<RequestInterceptor> requestInterceptors,
                  List<ResponseInterceptor> responseInterceptors,ExecutorService serverThreadPool) {
        this.port = port;
        this.requestMappings = requestMappings;
        this.serverThreadPool = serverThreadPool;
        this.requestInterceptors = requestInterceptors;
        this.responseInterceptors = responseInterceptors;
    }

    public static JHttpBuilder defaults() {
        int defaultPort = 9090;
        String defaultThreadName = "jhttp-thread-";
        var builder = new JHttpBuilder();
        return builder.port(defaultPort).serverThreadPool(Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name(defaultThreadName, 1).factory()));
    }

    public static JHttpBuilder of() {
        return new JHttpBuilder();
    }

    public int getPort() {
        return port;
    }

    public List<RequestMapping> getRequestMappings(String method) {
        return getRequestMappings(p -> p.getMethod().equalCheck(method));
    }

    public List<RequestMapping> getRequestMappings(String path, String method) {
        return getRequestMappings(p -> p.getPath().equals(path) && p.getMethod().equalCheck(method));
    }

    public List<RequestMapping> getRequestMappings(String path, String method, String mediaType) {
        return getRequestMappings(p -> p.getPath().equals(path) &&
                p.getMethod().equalCheck(method) &&
                p.getMediaType().equalCheck(mediaType));
    }

    private List<RequestMapping> getRequestMappings(Predicate<RequestMapping> predicate) {
        return requestMappings.stream().filter(predicate).toList();
    }

    public ExecutorService getServerThreadPool() {
        return serverThreadPool;
    }

    public List<RequestInterceptor> getRequestInterceptors() {
        return requestInterceptors;
    }

    public List<ResponseInterceptor> getResponseInterceptors() {
        return responseInterceptors;
    }

    void start() {
        httpServer = Server.createHttpServer(this);
        httpServer.start();
    }

    void shutdown() {
        httpServer.stop();
    }

    public static class JHttpBuilder {

        private final List<RequestMapping> requestMappings;
        private Integer port;
        private ExecutorService serverThreadPool;
        private final List<RequestInterceptor> requestInterceptors;
        private final List<ResponseInterceptor> responseInterceptors;

        JHttpBuilder() {
            this.requestMappings = new ArrayList<>();
            this.requestInterceptors = new ArrayList<>();
            this.responseInterceptors = new ArrayList<>();
        }


        public JHttpBuilder port(int port) {
            this.port = port;
            return this;
        }


        public JHttpBuilder addMapping(RequestMapping requestMapping) {
            requestMappings.add(requestMapping);
            return this;
        }

        public JHttpBuilder addMappings(List<RequestMapping> requestMappings) {
            this.requestMappings.addAll(requestMappings);
            return this;
        }

        public JHttpBuilder addRequestInterceptor(RequestInterceptor requestInterceptor) {
            requestInterceptors.add(requestInterceptor);
            return this;
        }

        public JHttpBuilder addRequestInterceptors(List<RequestInterceptor> requestInterceptors) {
            this.requestInterceptors.addAll(requestInterceptors);
            return this;
        }
        public JHttpBuilder addResponseInterceptor(ResponseInterceptor responseInterceptor) {
            responseInterceptors.add(responseInterceptor);
            return this;
        }

        public JHttpBuilder addResponseInterceptors(List<ResponseInterceptor> responseInterceptor) {
            this.responseInterceptors.addAll(responseInterceptor);
            return this;
        }

        public JHttpBuilder serverThreadPool(ExecutorService serverThreadPool) {
            this.serverThreadPool = serverThreadPool;
            return this;
        }

        public JHttp build() {
            requireNonNull(port);
            requireNonNull(serverThreadPool);
            Util.requireNonEmpty(requestMappings, "request mappings cannot be empty");
            return new JHttp(port, requestMappings,requestInterceptors,responseInterceptors, serverThreadPool);
        }

    }
}
