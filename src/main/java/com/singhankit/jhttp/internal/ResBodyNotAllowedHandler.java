package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.JHttp;
import com.singhankit.jhttp.Util;

/**
 * @author Ankit Singh
 */
 class ResBodyNotAllowedHandler extends BaseReqBodyLessHandler {

    ResBodyNotAllowedHandler(Request req, JHttp jHttp) {
        super(req, jHttp);

    }
    @Override
    public void handle() {
        var response = new StringBuilder();
        try {
            HttpResponse httpResponse = doHandle();
            Util.requireNull(httpResponse.body(),"Response cannot include body");
            response.append(generateRes(httpResponse));
        } catch(HttpException ex) {
            response.append(generateErrorRes(req.headers(), ex));
        }
        send(response.toString());
    }
}
