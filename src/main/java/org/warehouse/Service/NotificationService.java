package org.warehouse.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.warehouse.Kafka.Dto.ItemsMovedKafkaMessage;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {
    private final WebClient webClient;

    public NotificationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void notifyItemsMoved(ItemsMovedKafkaMessage message) {
        System.out.println(
                "NOTIFICATION_ITEMS_MOVED warehouseId=" + message.warehouseId()
                        + " itemIds=" + message.itemIds()
                        + " status=" + message.status()
        );
    }

    public Mono<Boolean> checkHealth() {
        return webClient.get()
                .uri("http://localhost:8080/health")
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful());
    }
}