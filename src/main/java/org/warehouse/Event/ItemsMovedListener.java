package org.warehouse.Event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ItemsMovedListener {
    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    public void itemsMovedEvent(ItemsMovedEvent event) {
        System.out.println("Sukses permanen! Item " + event.getItemsId() + " pindah ke warehouse " + event.getWarehouseId());
        //tempat untuk tambahkan push notification/email, dll
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onAfterRollback(ItemsMovedEvent event) {
        System.out.println("Gagal, membatalkan memindahkan item "+ event.getItemsId()+" pindah ke warehouse "+event.getWarehouseId());
    }
}
