package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.JHttp;

import java.io.IO;

/**
 * @author Ankit Singh
 */
class GetRequestHandler implements RequestHandler {

    private final Request req;
    private final JHttp jHttp;

    GetRequestHandler(Request req, JHttp jHttp) {
        this.req = req;
        this.jHttp = jHttp;

    }

    @Override
    public void handle() {
        try {

            HttpResponse res = jHttp.getRequestMappings().getFirst().getRequestHandler().handle(new HttpRequest(null));
            String response = generateResponse(res);
            IO.println("Response generated: "+response);
            req.out().write(response);
            req.out().flush();
        } catch(Exception e) {

        }
    }

    private String generateResponse(HttpResponse res) {
        StringBuilder sb = new StringBuilder(HTTP_VERSION + " ");
        sb.append(res.status().code()+" "+res.status().description()+LINE_END)
        .append("Connection: closed"+LINE_END)
        .append("Server: JHTTP server"+LINE_END+LINE_END);
        return  sb.toString();
    }
}
