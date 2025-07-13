package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.JHttp;

/**
 * @author Ankit Singh
 */
class ReqResBodyAllowedHandler extends BaseReqBodyHandler {

    ReqResBodyAllowedHandler(TCPHandler tcpHandler, Request req, JHttp jHttp) {
        super(tcpHandler, req, jHttp);
    }

    @Override
    public void handle() {
        Response response;
        try {
            HttpResponse httpResponse = doHandle();
            response = Response.success(httpResponse);
        } catch(HttpException ex) {
            response = Response.error(req.headers(), ex);
        }
        send(response.toHttpString());
    }
}
