alter table orders_items
    drop constraint fk_orders_items_on_order;

drop table orders cascade;

drop table orders_items cascade;