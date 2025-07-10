package com.singhankit.jhttp.interceptor;

import com.singhankit.jhttp.HttpClientException;
import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpHeaders;
import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.singhankit.jhttp.HttpHeaders.AUTHORIZATION;

/**
 * @author Ankit Singh
 */
public class BasicAuthInterceptor implements RequestInterceptor {

    private final String username;
    private final String password;

    public BasicAuthInterceptor(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean intercept(HttpRequest request) throws HttpException {
        try {
            HttpHeaders headers = request.headers();
            String authHeader = headers.get(AUTHORIZATION).orElseThrow(this::unauthorized);
            if(!match(getToken(authHeader))) {
                throw unauthorized();
            }
            return true;
        } catch(Exception e) {
            throw unauthorized();
        }
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

    private String getToken(String authHeader) {
        String base64Credentials = authHeader.substring("Basic ".length());
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    private boolean match(String token) {
        return token.equals(username + ":" + password);
    }

    private HttpClientException unauthorized() throws HttpException {
        return new HttpClientException(HttpStatus.UNAUTHORIZED, "Access Denied");
    }
}
