package org.warehouse.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(
            WebClient.Builder builder,
            @Value("${notification.service.base-url:http://localhost:8081}") String baseUrl
    ) {
        return builder.baseUrl(baseUrl).build();
    }
}
