package com.singhankit.jhttp.internal;

import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpResponse;
import com.singhankit.jhttp.JHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ankit Singh
 */
 class PostRequestHandler implements RequestHandler {
     private static final Logger LOG = LoggerFactory.getLogger(PostRequestHandler.class);
    private final Request req;
    private final JHttp jHttp;

    PostRequestHandler(Request req, JHttp jHttp) {
        this.req = req;
        this.jHttp =jHttp;

    }

    @Override
    public void handle() {
        try {
            int contentLength = Integer.parseInt(req.headers().get("Content-Length").get());
            String contentType = req.headers().get("Content-Type").get();

            String body = null;
            if(contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                int read = req.in().read(bodyChars);
                body = new String(bodyChars, 0, read);
            }

            var requestHandler = jHttp.getRequestMapping(req.path(), req.method().name(), contentType).getRequestHandler();
            var res = requestHandler.handle(new HttpRequest(body));
            String response = generateResponse(res);
            req.out().write(response);
            req.out().flush();
        }catch(Exception e) {}
    }

    private String generateResponse(HttpResponse res) {
        StringBuilder sb = new StringBuilder(HTTP_VERSION + " ");
        sb.append(res.status().code()+" "+res.status().description()+LINE_END)
          .append("Connection: closed"+LINE_END)
          .append("Server: JHTTP server"+LINE_END+LINE_END)
          .append(res.body());
        return  sb.toString();
    }
}
