package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpHeaders;
import com.singhankit.jhttp.HttpMethod;
import com.singhankit.jhttp.JHttp;
import com.singhankit.jhttp.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * @author ankitsingh
 */
class HttpRequestHandler implements Runnable, RequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestHandler.class);
    private final Socket clientSocket;
    private final JHttp jHttp;


    public HttpRequestHandler(Socket socket, JHttp jHttp) {
        this.clientSocket = socket;
        this.jHttp = jHttp;
    }

    @Override
    public void run() {
        handle();
    }

    @Override
    public void handle() {
        try(TCPHandler tcpHandler = new TCPHandler(clientSocket)) {

            String requestLine = tcpHandler.nextLine();
            if(Util.isEmpty(requestLine)) {
                return;
            }

            var request = createRequest(tcpHandler, requestLine);
            RequestHandler requestHandler = switch(request.method()) {
                case GET -> new ResBodyHandler(tcpHandler, request, jHttp, true);
                case POST, PUT, PATCH, DELETE -> new ReqResBodyAllowedHandler(tcpHandler, request, jHttp);
                case TRACE, HEAD -> new ResBodyHandler(tcpHandler, request, jHttp, false);
                case OPTIONS -> new OptionsRequestHandler(tcpHandler, request, jHttp);
                case UNKNOWN -> () -> {};
            };
            requestHandler.handle();

        } catch(IOException e) {
            LOG.error("Error occurred while handling request", e);
        } finally {
            Util.close(clientSocket);
        }
    }

    private Request createRequest(TCPHandler tcpHandler, String requestLine) throws IOException {
        var tokenizer = new StringTokenizer(requestLine);
        var method = HttpMethod.of(tokenizer.nextToken());
        String path = tokenizer.nextToken();
        var headers = new HttpHeaders(tcpHandler.readKeyValueLine());
        addDefaultHeaders(headers);
        return new Request(headers, path, method);
    }

    private void addDefaultHeaders(HttpHeaders httpHeaders) {
        if(httpHeaders.get("X-Forwarded-For").isEmpty()) {
            httpHeaders.add("X-Forwarded-For", clientSocket.getInetAddress().getHostAddress());
        }
    }
}
