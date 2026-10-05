package org.warehouse.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.warehouse.Kafka.Dto.ItemsMovedKafkaMessage;
import org.warehouse.Kafka.Dto.LowStockKafkaMessage;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final WebClient webClient;

    public NotificationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Void> notifyItemsMoved(ItemsMovedKafkaMessage message) {
//        log.info("NOTIFICATION_ITEMS_MOVED eventId={} sourceWarehouseId={} targetWarehouseId={} itemIds={} status={}",
//                message.eventId(), message.sourceWarehouseId(), message.warehouseId(), message.itemIds(), message.status());
        return webClient.post()
                .uri("/notification/items-moved")
                .bodyValue(message)
                .retrieve()
                .toBodilessEntity()
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(3)))
                .then();
    }

    public Mono<Void> notifyLowStock(LowStockKafkaMessage message) {
        return webClient.post()
                .uri("/notification/low-stock")
                .bodyValue(message)
                .retrieve()
                .toBodilessEntity()
                .retryWhen(Retry.backoff(1, Duration.ofSeconds(2)))
                .then();
    }

    public Mono<Boolean> checkHealth() {
        return webClient.get()
                .uri("/health")
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorReturn(false);
    }
}
