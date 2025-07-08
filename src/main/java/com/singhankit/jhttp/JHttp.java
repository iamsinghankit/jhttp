package com.singhankit.jhttp;

import com.singhankit.jhttp.internal.Server;
import com.singhankit.jhttp.internal.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.requireNonNull;

/**
 * @author Ankit Singh
 */
public class JHttp {

    private final Integer port;
    private final List<RequestMapping> requestMappings;
    private final ExecutorService serverThreadPool;
    private Server httpServer;

    private JHttp(int port, List<RequestMapping> requestMappings, ExecutorService serverThreadPool) {
        this.port = port;
        this.requestMappings = requestMappings;
        this.serverThreadPool = serverThreadPool;
    }

    public static JHttpBuilder defaults() {
        var builder = new JHttpBuilder();
        return builder
                .port(8080)
                .serverThreadPool(Executors.newThreadPerTaskExecutor(Thread.ofVirtual()
                                                                           .name("jhttpserver-thread-", 1)
                                                                           .factory()));
    }

    public static JHttpBuilder of() {
        return new JHttpBuilder();
    }

    public int getPort() {
        return port;
    }

    public List<RequestMapping> getRequestMappings() {
        return requestMappings;
    }

     RequestMapping getRequestMapping(String path, String method) {
        var requests = requestMappings.stream()
                                      .filter(m -> m.getPath().equals(path) &&
                                              m.getMethod().equalCheck((method)))
                                      .toList();
        if(requests.isEmpty()) {
            throw new IllegalStateException("No request mapping found for path: " + path);
        } else if(requests.size() > 1) {
            throw new IllegalStateException("More than one request mapping found for path: " + path);
        } else {
            return requests.getFirst();
        }
    }
    public RequestMapping getRequestMapping(String path, String method, String mediaType) {
        var requests = requestMappings.stream()
                                      .filter(m -> m.getPath().equals(path) &&
                                              m.getMediaType().equalCheck(mediaType) &&
                                              m.getMethod().equalCheck((method)))
                                      .toList();
        if(requests.isEmpty()) {
            throw new IllegalStateException("No request mapping found for path: " + path);
        } else if(requests.size() > 1) {
            throw new IllegalStateException("More than one request mapping found for path: " + path);
        } else {
            return requests.getFirst();
        }
    }

    public ExecutorService getServerThreadPool() {
        return serverThreadPool;
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

        JHttpBuilder() {
            this.requestMappings = new ArrayList<>();
        }


        public JHttpBuilder port(int port) {
            this.port = port;
            return this;
        }


        public JHttpBuilder addMapping(RequestMapping requestMapping) {
            requestMappings.add(requestMapping);
            return this;
        }

        public JHttpBuilder requestMappings(List<RequestMapping> requestMappings) {
            this.requestMappings.addAll(requestMappings);
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
            return new JHttp(port, requestMappings, serverThreadPool);
        }

    }
}
