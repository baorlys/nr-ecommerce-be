alter table products
    add status VARCHAR(255);

alter table product_variants
    alter COLUMN name SET NOT NULL;