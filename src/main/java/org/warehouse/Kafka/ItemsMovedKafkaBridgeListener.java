package org.warehouse.Kafka;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.warehouse.Event.ItemsMovedEvent;

@Component
public class ItemsMovedKafkaBridgeListener {
    private final ItemsMovedKafkaProducer producer;

    public ItemsMovedKafkaBridgeListener(ItemsMovedKafkaProducer producer) {
        this.producer = producer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onItemsMovedAfterCommit(ItemsMovedEvent event) {
        producer.publish(event.warehouseId(), event.itemsId(), "COMMITTED");
    }
}
