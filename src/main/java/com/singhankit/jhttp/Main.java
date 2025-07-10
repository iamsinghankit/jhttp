package com.singhankit.jhttp;

import com.singhankit.jhttp.interceptor.CorsInterceptor;
import com.singhankit.jhttp.interceptor.CorsInterceptor.CorsHeader;
import com.singhankit.jhttp.interceptor.BasicAuthInterceptor;
import com.singhankit.jhttp.interceptor.RequestInterceptor;
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
        var putHelloMapping = RequestMapping.OPTIONS().path("/hello").requestHandler(handlePOSTRequest()).build();

        var mappings = List.of(getHelloMapping, postHelloMapping, putHelloMapping);

        JHttp jHttp = JHttp.defaults()
                           .addMappings(mappings)
                           .addRequestInterceptor(addHeaderInRequest())
                           .addRequestInterceptor(new BasicAuthInterceptor("ankit","pass"))
                           .addResponseInterceptor(new CorsInterceptor(CorsHeader.allowAll()))
                           .build();
        jHttp.start();
    }


    private RequestInterceptor addHeaderInRequest(){
        return request-> {
            request.headers().add("RequestInterceptor","Testing");
            return true;
        };
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
