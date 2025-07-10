package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.JHttp;

/**
 * @author Ankit Singh
 */
class ReqResBodyAllowedHandler extends BaseReqBodyHandler {

    ReqResBodyAllowedHandler(Request req, JHttp jHttp) {
        super(req, jHttp);
    }

    @Override
    public void handle() {
        var response = new StringBuilder();
        try {
            HttpResponse httpResponse = doHandle();
            response.append(generateRes(httpResponse));
        } catch(HttpException ex) {
            response.append(generateErrorRes(req.headers(), ex));
        }
        send(response.toString());
    }
}
