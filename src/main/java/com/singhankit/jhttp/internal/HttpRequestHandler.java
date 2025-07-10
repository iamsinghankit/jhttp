package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpHeaders;
import com.singhankit.jhttp.JHttp;
import com.singhankit.jhttp.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

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
        try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {
            String requestLine = in.readLine();
            if(Util.isEmpty(requestLine)) {
                return;
            }
            var request = Request.of(in, out, requestLine);
            addRequestHeaders(request.headers());
            RequestHandler requestHandler = switch(request.method()) {
                case GET -> new ResBodyHandler(request, jHttp, true);
                case POST, PUT, PATCH, DELETE -> new ReqResBodyAllowedHandler(request, jHttp);
                case TRACE, HEAD -> new ResBodyHandler(request, jHttp, false);
                case OPTIONS -> new OptionsRequestHandler(request, jHttp);
                case UNKNOWN -> () -> {};
            };
            requestHandler.handle();
        } catch(IOException e) {
            LOG.error("Error occurred while handling request", e);
        } finally {
            Util.close(clientSocket);
        }
    }


    private void addRequestHeaders(HttpHeaders httpHeaders) {
        if(httpHeaders.get("X-Forwarded-For").isEmpty()) {
            httpHeaders.add("X-Forwarded-For", clientSocket.getInetAddress().getHostAddress());
        }
    }
}
