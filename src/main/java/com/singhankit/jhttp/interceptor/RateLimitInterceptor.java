package com.singhankit.jhttp.interceptor;

import com.singhankit.jhttp.HttpClientException;
import com.singhankit.jhttp.HttpException;
import com.singhankit.jhttp.HttpRequest;
import com.singhankit.jhttp.HttpStatus;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ankit Singh
 */
public class RateLimitInterceptor implements RequestInterceptor {

    private final int maxRequests;
    private final long windowMillis;
    private final Map<String, Counter> counters = new ConcurrentHashMap<>();

    public RateLimitInterceptor(int maxRequests, long windowsMillis) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowsMillis;
    }

    @Override
    public boolean intercept(HttpRequest request) throws HttpException {
        String ip = request.headers().get("X-Forwarded-For")
                           .orElseThrow(() -> new HttpClientException(HttpStatus.BAD_REQUEST, "'X-Forwarded-For' header missing"));

        long now = Instant.now().toEpochMilli();
        counters.compute(ip, (_, counter) -> {
            if(counter == null || now - counter.startTime > windowMillis) {
                return new Counter(now, 1);
            } else {
                counter.count++;
                return counter;
            }
        });

        Counter counter = counters.get(ip);
        if(counter.count > maxRequests) {
            request.headers().add("Retry-After", String.valueOf(windowMillis));
            throw new HttpClientException(HttpStatus.TO_MANY_REQUESTS, "Limit exceeded");
        }
        return true;
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE + 1;
    }

    private static class Counter {
        long startTime;
        int count;

        Counter(long startTime, int count) {
            this.startTime = startTime;
            this.count = count;
        }
    }


}
