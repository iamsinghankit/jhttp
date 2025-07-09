package com.singhankit.jhttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Ankit Singh
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    void main() {
        var getHelloMapping = RequestMapping.GET().path("/hello").requestHandler(handleGetRequest()).build();
        var postHelloMapping = RequestMapping.POST().path("/hello").requestHandler(handlePOSTRequest()).build();
        var putHelloMapping = RequestMapping.PUT().path("/hello").requestHandler(handlePOSTRequest()).build();

        var mappings = List.of(getHelloMapping, postHelloMapping, putHelloMapping);

        JHttp jHttp = JHttp.defaults()
                           .addMappings(mappings)
                           .build();
        jHttp.start();
    }

    HttpRequestHandler handleGetRequest() {
        return (request) -> {
            LOG.info("GET Received: {}", request);
            return new HttpResponse(request.headers(), HttpStatus.OK, null);
        };
    }

    HttpRequestHandler handlePOSTRequest() {
        return (request) -> {
            LOG.info("POST Received: {}", request);
            return new HttpResponse(request.headers(), HttpStatus.OK, null);
        };
    }
}
