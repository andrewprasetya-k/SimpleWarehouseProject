package org.warehouse.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.warehouse.Kafka.Dto.ItemsMovedKafkaMessage;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final WebClient webClient;

    public NotificationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void notifyItemsMoved(ItemsMovedKafkaMessage message) {
        log.info("NOTIFICATION_ITEMS_MOVED eventId={} sourceWarehouseId={} targetWarehouseId={} itemIds={} status={}",
                message.eventId(), message.sourceWarehouseId(), message.warehouseId(), message.itemIds(), message.status());
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
