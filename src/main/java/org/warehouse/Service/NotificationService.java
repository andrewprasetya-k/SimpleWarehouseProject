package org.warehouse.Service;

import org.springframework.stereotype.Service;
import org.warehouse.Kafka.ItemsMovedKafkaMessage;

@Service
public class NotificationService {

    public void notifyItemsMoved(ItemsMovedKafkaMessage message) {
        System.out.println("NOTIFICATION_ITEMS_MOVED itemId " + message.itemIds() + " has been moved to warehouse "+message.warehouseId());
    }
}
