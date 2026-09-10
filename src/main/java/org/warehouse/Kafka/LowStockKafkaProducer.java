package org.warehouse.Kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.warehouse.Kafka.Dto.LowStockKafkaMessage;

import java.util.concurrent.CompletableFuture;


@Service
public class LowStockKafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(LowStockKafkaProducer.class);

    private final KafkaTemplate<String, LowStockKafkaMessage> kafkaTemplate;
    private final String topicName;

    public LowStockKafkaProducer(KafkaTemplate<String, LowStockKafkaMessage> kafkaTemplate, @Value("${app.kafka.topics.low-stock-alerts}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(Integer itemId, String itemName, Integer remainingQuantity, Integer warehouseId) {
        LowStockKafkaMessage message = new LowStockKafkaMessage(itemId, itemName, remainingQuantity, warehouseId);

        System.out.println("KAFKA_LOW_STOCK_PRODUCE payload=" + message);

        CompletableFuture<?> sendResult = kafkaTemplate.send(topicName, itemId.toString(), message);
        sendResult.whenComplete((result, error) -> {
            if (error != null) {
                log.error("KAFKA_LOW_STOCK_SEND_FAILED itemId={}", itemId, error);
                return;
            }
        });
    }


}
