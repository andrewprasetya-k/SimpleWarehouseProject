package org.warehouse.Kafka;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.warehouse.Event.LowStockEvent;

@Component
public class LowStockKafkaBridgeListener {
    private final LowStockKafkaProducer producer;

    public LowStockKafkaBridgeListener(LowStockKafkaProducer producer) {
        this.producer = producer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onLowStockAfterCommit(LowStockEvent event) {
        producer.publish(event.itemId(), event.itemName(), event.remainingQuantity(), event.warehouseId());
    }
}
