package com.singhankit.jhttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ankit Singh
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    void main() {
        var helloMapping = RequestMapping.GET()
                                           .path("/hello")
//                                           .mediaType(MediaType.APPLICATION_JSON)
                                           .requestHandler(handleRequest())
                                           .build();
        JHttp jHttp = JHttp.defaults()
                           .addMapping(helloMapping)
                           .build();
        jHttp.start();
    }

     HttpRequestHandler handleRequest(){
        return (request)->{
            LOG.info("Received: {}",request);
            return new HttpResponse(request.headers().add("Content-Type","application/json"),HttpStatus.OK, null);
        };
    }
}
