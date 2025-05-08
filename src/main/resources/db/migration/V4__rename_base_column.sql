alter table categories
    add deleted BOOLEAN DEFAULT FALSE;

alter table categories
    add updated_on date;

alter table product_images
    add deleted BOOLEAN DEFAULT FALSE;

alter table product_images
    add updated_on date;

alter table product_variants
    add deleted BOOLEAN DEFAULT FALSE;

alter table product_variants
    add updated_on date;

alter table products
    add deleted BOOLEAN DEFAULT FALSE;

alter table products
    add updated_on date;

alter table reviews
    add deleted BOOLEAN DEFAULT FALSE;

alter table reviews
    add updated_on date;

alter table roles
    add deleted BOOLEAN DEFAULT FALSE;

alter table roles
    add updated_on date;

alter table users
    add deleted BOOLEAN DEFAULT FALSE;

alter table users
    add updated_on date;

alter table categories
    drop COLUMN is_deleted;

alter table categories
    drop COLUMN update_on;

alter table product_images
    drop COLUMN is_deleted;

alter table product_images
    drop COLUMN update_on;

alter table product_variants
    drop COLUMN is_deleted;

alter table product_variants
    drop COLUMN update_on;

alter table products
    drop COLUMN is_deleted;

alter table products
    drop COLUMN update_on;

alter table reviews
    drop COLUMN is_deleted;

alter table reviews
    drop COLUMN update_on;

alter table roles
    drop COLUMN is_deleted;

alter table roles
    drop COLUMN update_on;

alter table users
    drop COLUMN is_deleted;

alter table users
    drop COLUMN update_on;

alter table product_images
    alter COLUMN product_id SET NOT NULL;