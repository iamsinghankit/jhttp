package com.singhankit.jhttp.internal;

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
            //requestLine is normally empty for OPTIONS request.
            if(Util.isEmpty(requestLine)) {
                return;
            }
            LOG.debug("Request: {}", requestLine);
            var request = Request.of(in, out, requestLine);
            RequestHandler requestHandler = switch(request.method()) {
                case GET -> new ResBodyAllowedHandler(request, jHttp);
                case POST, PUT, PATCH, DELETE -> new ReqResAllowedHandler(request, jHttp);
                case TRACE, OPTIONS, HEAD -> new ResBodyNotAllowedHandler(request, jHttp);
            };

            requestHandler.handle();

        } catch(IOException e) {
            LOG.error("Error occurred while handling request", e);
        } finally {
            Util.close(clientSocket);
        }
    }
}
