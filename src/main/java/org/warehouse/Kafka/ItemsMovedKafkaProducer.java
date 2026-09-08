package org.warehouse.Kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void publish(Integer warehouseId, List<Integer> itemIds, String status) {
        ItemsMovedKafkaMessage message = new ItemsMovedKafkaMessage(
                warehouseId,
                itemIds,
                status
        );

        System.out.println("KAFKA_ITEMS_MOVED_PRODUCE payload=" + message);

        CompletableFuture<?> sendResult = kafkaTemplate.send(topicName, warehouseId.toString(), message);
        sendResult.whenComplete((result, error) -> {
            if (error != null) {
                log.error("KAFKA_ITEMS_MOVED_SEND_FAILED warehouseId={} itemIds={} status={}", warehouseId, itemIds, status, error);
                return;
            }

            System.out.println("KAFKA_ITEMS_MOVED_SENT warehouseId=" + warehouseId + " itemIds=" + itemIds + " status=" + status);
        });
    }
}
