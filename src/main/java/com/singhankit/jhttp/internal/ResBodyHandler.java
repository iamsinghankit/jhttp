package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.JHttp;
import com.singhankit.jhttp.Util;

/**
 * @author Ankit Singh
 */
class ResBodyHandler extends BaseReqBodyLessHandler {

    private final boolean resBodyAllowed;

    ResBodyHandler(TCPHandler tcpHandler, Request req, JHttp jHttp, boolean resBodyAllowed) {
        super(tcpHandler, req, jHttp);
        this.resBodyAllowed = resBodyAllowed;
    }

    @Override
    public void handle() {
        Response response;
        try {
            HttpResponse httpResponse = doHandle();
            if(!resBodyAllowed) {
                Util.requireNull(httpResponse.body(), "Response cannot include body");
            }
            response = Response.success(httpResponse);
        } catch(HttpException ex) {
            response = Response.error(req.headers(), ex);
        }
        send(response.toHttpString());
    }
}
