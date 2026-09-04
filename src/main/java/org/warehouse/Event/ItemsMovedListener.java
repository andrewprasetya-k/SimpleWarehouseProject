package org.warehouse.Event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ItemsMovedListener {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onBeforeCommit(ItemsMovedEvent event) {
        System.out.println("Committing... item " + event.itemsId() + " to warehouse " + event.warehouseId());
    }

    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    public void itemsMovedEvent(ItemsMovedEvent event) {
        System.out.println("Success! Item " + event.itemsId() + " is moved to warehouse " + event.warehouseId());
        //tempat untuk tambahkan push notification/email, dll
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onAfterRollback(ItemsMovedEvent event) {
        System.out.println("Failed to moved "+ event.itemsId()+" to warehouse "+event.warehouseId());
    }
}
