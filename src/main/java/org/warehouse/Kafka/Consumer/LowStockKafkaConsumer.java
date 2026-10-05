package org.warehouse.Kafka.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.warehouse.Kafka.Dto.LowStockKafkaMessage;
import org.warehouse.Service.NotificationService;

@Component
public class LowStockKafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(LowStockKafkaConsumer.class);
    private final NotificationService notificationService;

    public LowStockKafkaConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.low-stock-alerts}",
            groupId = "${app.kafka.consumer.groups.low-stock}"
    )
    public void onMessage(LowStockKafkaMessage message) {
        logger.warn("KAFKA_LOW_STOCK_NOTIFICATION message={}", message);
        notificationService.notifyLowStock(message)
                .doOnSuccess(unused -> logger.info("Low-stock notification sent"))
                .doOnError(error -> logger.error("Failed to send low-stock notification", error))
                .subscribe();
    }
}
