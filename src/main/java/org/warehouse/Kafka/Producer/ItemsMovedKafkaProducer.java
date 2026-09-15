package org.warehouse.Kafka.Producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.warehouse.Kafka.Dto.ItemsMovedKafkaMessage;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ItemsMovedKafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(ItemsMovedKafkaProducer.class);

    private final KafkaTemplate<String, ItemsMovedKafkaMessage> kafkaTemplate;
    private final String topicName;

    public ItemsMovedKafkaProducer(
            KafkaTemplate<String, ItemsMovedKafkaMessage> kafkaTemplate,
            @Value("${app.kafka.topics.items-moved}") String topicName
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(
            UUID eventId,
            Integer sourceWarehouseId,
            Integer warehouseId,
            List<Integer> itemIds,
            String status,
            Instant occurredAt
    ) {
        ItemsMovedKafkaMessage message = new ItemsMovedKafkaMessage(
                eventId,
                sourceWarehouseId,
                warehouseId,
                itemIds,
                status,
                occurredAt
        );

        log.info("KAFKA_ITEMS_MOVED_PRODUCE payload={}", message);

        CompletableFuture<?> sendResult = kafkaTemplate.send(topicName, eventId.toString(), message);
        sendResult.whenComplete((result, error) -> {
            if (error != null) {
                log.error("KAFKA_ITEMS_MOVED_SEND_FAILED warehouseId={} itemIds={} status={}", warehouseId, itemIds, status, error);
                return;
            }

            log.info("KAFKA_ITEMS_MOVED_SENT eventId={} sourceWarehouseId={} warehouseId={} itemIds={} status={}",
                    eventId, sourceWarehouseId, warehouseId, itemIds, status);
        });
    }
}
