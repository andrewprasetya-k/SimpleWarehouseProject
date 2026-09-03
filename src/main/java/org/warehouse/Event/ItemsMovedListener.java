package org.warehouse.Event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ItemsMovedListener {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onBeforeCommit(ItemsMovedEvent event) {
        System.out.println("Committing... item " + event.getItemsId() + " to warehouse " + event.getWarehouseId());
    }

    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    public void itemsMovedEvent(ItemsMovedEvent event) {
        System.out.println("Success! Item " + event.getItemsId() + " is moved to warehouse " + event.getWarehouseId());
        //tempat untuk tambahkan push notification/email, dll
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onAfterRollback(ItemsMovedEvent event) {
        System.out.println("Failed to moved "+ event.getItemsId()+" to warehouse "+event.getWarehouseId());
    }
}
