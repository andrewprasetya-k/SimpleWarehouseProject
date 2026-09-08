package org.warehouse.Kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ItemsMovedKafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(ItemsMovedKafkaConsumer.class);

    @KafkaListener(
            topics = "${app.kafka.topics.items-moved}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onMessage(ItemsMovedKafkaMessage message) {
        System.out.println("KAFKA_ITEMS_MOVED_CONSUME payload=" + message);
        System.out.println("KAFKA_ITEMS_MOVED_RECEIVED warehouseId=" + message.warehouseId()
                + " itemIds=" + message.itemIds()
                + " status=" + message.status());
    }
}
