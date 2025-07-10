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

    ResBodyHandler(Request req, JHttp jHttp, boolean resBodyAllowed) {
        super(req, jHttp);
        this.resBodyAllowed = resBodyAllowed;
    }

    @Override
    public void handle() {
        var response = new StringBuilder();
        try {
            HttpResponse httpResponse = doHandle();
            if(!resBodyAllowed) {
                Util.requireNull(httpResponse.body(), "Response cannot include body");
            }
            response.append(generateRes(httpResponse));
        } catch(HttpException ex) {
            response.append(generateErrorRes(req.headers(), ex));
        } send(response.toString());
    }
}
