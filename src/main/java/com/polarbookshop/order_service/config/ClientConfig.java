package com.polarbookshop.order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Configuration
public class ClientConfig {

    @Bean
    public WebClient webClient(ClientProperties clientProperties,
                               WebClient.Builder builder) {
        return builder
                .baseUrl(clientProperties.catalogServiceUri().toString())
                .build();
    }
}
