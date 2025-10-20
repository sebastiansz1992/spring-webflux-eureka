package com.sebastian.springcloud.app.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(SampleGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Request Path: {}", exchange.getRequest().getURI().getPath());

        // PRE-PROCESSING: Add token to request headers
        String tokenValue = "asdasdasd";
        
        // Properly mutate the request and create a new exchange
        ServerWebExchange mutatedExchange = exchange.mutate()
            .request(exchange.getRequest().mutate()
                .headers(httpHeaders -> {
                    httpHeaders.add("token", tokenValue);
                    logger.info("Added token to request headers: {}", tokenValue);
                })
                .build())
            .build();

        // Add token to response headers (before processing)
        mutatedExchange.getResponse().getHeaders().add("token", tokenValue);
        logger.info("Added token to response headers: {}", tokenValue);

        logger.info("Request Headers before forwarding: {}", mutatedExchange.getRequest().getHeaders());

        // Continue with the filter chain using the mutated exchange
        return chain
            .filter(mutatedExchange)
            .then(Mono.fromRunnable(() -> {
                // POST-PROCESSING: Log response details
                logger.info("Response Status Code: {}", mutatedExchange.getResponse().getStatusCode());
                logger.info("Final Request Headers: {}", mutatedExchange.getRequest().getHeaders());
                logger.info("Final Response Headers: {}", mutatedExchange.getResponse().getHeaders());

                // Add cookie to response
                mutatedExchange.getResponse().getCookies().add("color", 
                    ResponseCookie.from("color", "blue").build());
            }));
    }

    @Override
    public int getOrder() {
        return 100;
    }

}
