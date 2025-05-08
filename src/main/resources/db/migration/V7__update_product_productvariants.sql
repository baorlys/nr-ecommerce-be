
alter table product_variants
    drop COLUMN unit;

alter table product_variants
    drop COLUMN weight;

alter table product_variants
    alter COLUMN name SET NOT NULL;