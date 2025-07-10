package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.JHttp;
import com.singhankit.jhttp.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Objects;

/**
 * @author Ankit Singh
 */
class JHttpServer implements Server {
    private static final Logger LOG = LoggerFactory.getLogger(JHttpServer.class);

    private final JHttp jHttp;
    private volatile boolean isRunning;
    private ServerSocket server;

    JHttpServer(JHttp jHttp) {
        this.jHttp = jHttp;
    }

    public synchronized void start() {
        try {
            if(isRunning && Objects.nonNull(server) && !server.isClosed()) {
                throw new IllegalStateException("JHttp server already running");
            }
            isRunning = true;
            server = new ServerSocket(jHttp.getPort(),0, InetAddress.getByName(null));
            LOG.info("JHttp server started on port: {}", jHttp.getPort());
            while(isRunning) {
                var client = server.accept();
                jHttp.getServerThreadPool().execute(new HttpRequestHandler(client, jHttp));
            }
        } catch(IOException ex) {
            LOG.error("Error occurred while waiting for client", ex);
        }
    }

    @Override
    public synchronized void stop() {
        if(!isRunning && server.isClosed()) {
            LOG.info("JHttp server is already terminated");
            return;
        }
        LOG.info("Stopping JHttpServer...");
        isRunning = false;
        Util.close(server);
    }
}
