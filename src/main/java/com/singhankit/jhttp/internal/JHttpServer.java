package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.JHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Ankit Singh
 */
 class JHttpServer implements Server {
    private static final Logger LOG = LoggerFactory.getLogger(JHttpServer.class);

    private volatile boolean isRunning ;
    private ServerSocket server;
    private final JHttp jHttp;

     JHttpServer(JHttp jHttp) {
        this.isRunning = true;
        this.jHttp = jHttp;
    }


    public synchronized void start() {
        try {
            server = new ServerSocket(jHttp.getPort());
            LOG.info("JHttp server started on port: {}", jHttp.getPort());
            while(isRunning) {
                var client = server.accept();
                jHttp.getServerThreadPool().execute(new HttpRequestHandler(client,jHttp));
            }
        } catch(IOException ex) {
            LOG.error("Error occurred while waiting for client", ex);
        }
    }

    @Override
    public synchronized void stop() {
        if(!isRunning && server.isClosed()) {
            LOG.warn("JHttp server is already stopped");
            return;
        }
        LOG.info("Stopping JHttpServer...");
        isRunning = false;
        Util.close(server);
    }
}
