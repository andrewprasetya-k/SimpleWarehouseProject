package org.warehouse.Kafka.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.warehouse.Kafka.Dto.LowStockKafkaMessage;

@Component
public class LowStockKafkaConsumer {
    private static Logger logger = LoggerFactory.getLogger(LowStockKafkaConsumer.class);

    @KafkaListener(
            topics = "${app.kafka.topics.low-stock-alerts}",
            groupId = "warehouse-low-stock-group"
    )
    public void onMessage(LowStockKafkaMessage message) {
        System.out.println("KAFKA_LOW_STOCK_NOTIFICATION message=" + message);
        logger.warn("KAFKA_LOW_STOCK_NOTIFICATION message=" + message);

    }
}
