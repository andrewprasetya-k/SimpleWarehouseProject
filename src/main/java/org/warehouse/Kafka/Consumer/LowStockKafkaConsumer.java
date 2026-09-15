package org.warehouse.Kafka.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.warehouse.Kafka.Dto.LowStockKafkaMessage;

@Component
public class LowStockKafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(LowStockKafkaConsumer.class);

    @KafkaListener(
            topics = "${app.kafka.topics.low-stock-alerts}",
            groupId = "${app.kafka.consumer.groups.low-stock}"
    )
    public void onMessage(LowStockKafkaMessage message) {
        logger.warn("KAFKA_LOW_STOCK_NOTIFICATION message={}", message);

    }
}
