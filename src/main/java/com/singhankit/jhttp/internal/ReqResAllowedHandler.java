package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.JHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ankit Singh
 */
class ReqResAllowedHandler extends ReqBodyHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ReqResAllowedHandler.class);

    ReqResAllowedHandler(Request req, JHttp jHttp) {
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
        try {
            req.out().write(response.toString());
            req.out().flush();
        } catch(Exception e) {
            LOG.error("Error occurred while handling POST request", e);
        }
    }
}
