CREATE TABLE warehouse.stock_movement (
    id SERIAL PRIMARY KEY,
    item_id INTEGER NOT NULL REFERENCES warehouse.item(id),
    warehouse_id INTEGER REFERENCES warehouse.warehouse_location(id),
    movement_type VARCHAR(50) NOT NULL,
    quantity_change INTEGER NOT NULL,
    previous_quantity INTEGER NOT NULL,
    current_quantity INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE INDEX idx_stock_movement_item_id ON warehouse.stock_movement(item_id);
CREATE INDEX idx_stock_movement_warehouse_id ON warehouse.stock_movement(warehouse_id);
CREATE INDEX idx_stock_movement_item_id_created_at
    ON warehouse.stock_movement(item_id, created_at DESC);

CREATE INDEX idx_stock_movement_created_at
    ON warehouse.stock_movement(created_at DESC);