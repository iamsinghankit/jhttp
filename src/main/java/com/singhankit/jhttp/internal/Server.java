package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.JHttp;

/**
 * @author Ankit Singh
 */
public interface Server {

    static Server createHttpServer(JHttp jHttp) {
        return new JHttpServer(jHttp);
    }

    void start();

    void stop();
}
