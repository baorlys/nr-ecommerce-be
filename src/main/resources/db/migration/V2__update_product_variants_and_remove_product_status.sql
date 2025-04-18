ALTER TABLE products
    DROP CONSTRAINT fk_products_on_productstatus;

CREATE TABLE product_variants
(
    id             VARCHAR(26)       NOT NULL,
    created_on     date,
    update_on      date,
    is_deleted     BOOLEAN DEFAULT FALSE,
    product_id     VARCHAR(26)       NOT NULL,
    weight         DECIMAL(10, 2)    NOT NULL,
    unit           VARCHAR(255)      NOT NULL,
    price          DECIMAL(20, 2)    NOT NULL,
    stock_quantity INTEGER DEFAULT 0 NOT NULL,
    CONSTRAINT pk_product_variants PRIMARY KEY (id)
);

ALTER TABLE product_variants
    ADD CONSTRAINT FK_PRODUCT_VARIANTS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

DROP TABLE product_statuses CASCADE;

ALTER TABLE products
    DROP COLUMN price;

ALTER TABLE products
    DROP COLUMN product_status_id;

ALTER TABLE products
    DROP COLUMN stock_quantity;