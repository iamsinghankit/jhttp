package com.singhankit.jhttp;

import com.singhankit.jhttp.config.ConfigManager;
import com.singhankit.jhttp.interceptor.CorsInterceptor;
import com.singhankit.jhttp.interceptor.CorsInterceptor.CorsHeader;
import com.singhankit.jhttp.interceptor.JsonLoggingInterceptor;
import com.singhankit.jhttp.interceptor.RequestInterceptor;
import com.singhankit.jhttp.interceptor.ResponseInterceptor;
import com.singhankit.jhttp.internal.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import static com.singhankit.jhttp.config.ConfigSettings.SERVER_PORT;
import static com.singhankit.jhttp.config.ConfigSettings.SERVER_THREAD_NAME;
import static java.util.Comparator.comparingInt;
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
        this.requestInterceptors = requestInterceptors.stream().sorted(comparingInt(RequestInterceptor::order)).toList();
        this.responseInterceptors = responseInterceptors.stream().sorted(comparingInt(ResponseInterceptor::order)).toList();
    }

    public static JHttpBuilder defaults() {
        ConfigManager config = Context.getConfig();
        return new JHttpBuilder()
                .port(config.getInt(SERVER_PORT, 9090))
                .serverThreadPool(Executors.newThreadPerTaskExecutor(Thread.ofVirtual()
                                                                           .name(config.getString(SERVER_THREAD_NAME,"jhttp-thread-"), 1)
                                                                           .factory()));
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

    public void start() {
        httpServer = Server.createHttpServer(this);
        httpServer.start();
    }

    public void shutdown() {
        httpServer.stop();
    }

    public static class JHttpBuilder {

        private final List<RequestMapping> requestMappings;
        private Integer port;
        private ExecutorService serverThreadPool;
        private final List<RequestInterceptor> requestInterceptors;
        private final List<ResponseInterceptor> responseInterceptors;

        private JHttpBuilder() {
            this.requestMappings = new ArrayList<>();
            this.requestInterceptors = new ArrayList<>();
            this.responseInterceptors = new ArrayList<>();
        }

        public JHttpBuilder port(int port) {
            this.port = port;
            return this;
        }

        public JHttpBuilder enableLogging(boolean enable) {
            if(enable){
                var interceptor = new JsonLoggingInterceptor();
                addRequestInterceptor(interceptor);
                addResponseInterceptor(interceptor);
            }
            return this;
        }

        public JHttpBuilder enableCors(boolean enable) {
            if(enable){
                addResponseInterceptor(new CorsInterceptor(CorsHeader.allowAll()));
            }
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
            return new JHttp(port, requestMappings, requestInterceptors,responseInterceptors, serverThreadPool);
        }

    }
}
