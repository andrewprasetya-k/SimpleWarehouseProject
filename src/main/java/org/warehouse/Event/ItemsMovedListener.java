package org.warehouse.Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ItemsMovedListener {
    private static final Logger log = LoggerFactory.getLogger(ItemsMovedListener.class);

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onBeforeCommit(ItemsMovedEvent event) {
        log.info("ITEMS_MOVED_BEFORE_COMMIT eventId={} itemIds={} sourceWarehouseId={} warehouseId={}",
                event.eventId(), event.itemsId(), event.sourceWarehouseId(), event.warehouseId());
    }

    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    public void itemsMovedEvent(ItemsMovedEvent event) {
        log.info("ITEMS_MOVED_AFTER_COMMIT eventId={} itemIds={} sourceWarehouseId={} warehouseId={}",
                event.eventId(), event.itemsId(), event.sourceWarehouseId(), event.warehouseId());
        //tempat untuk tambahkan push notification/email, dll
    }

    //kayaknya ini baru
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onAfterRollback(ItemsMovedEvent event) {
        log.warn("ITEMS_MOVED_AFTER_ROLLBACK eventId={} itemIds={} sourceWarehouseId={} warehouseId={}",
                event.eventId(), event.itemsId(), event.sourceWarehouseId(), event.warehouseId());
    }
}
