CREATE TABLE categories
(
    id           VARCHAR(26) NOT NULL,
    created_on   date,
    update_on    date,
    is_deleted   BOOLEAN DEFAULT FALSE,
    name         VARCHAR(255),
    description  VARCHAR(255),
    image_url    VARCHAR(255),
    parent_id    VARCHAR(26),
    storage_type VARCHAR(255),
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE product_images
(
    id           VARCHAR(26)  NOT NULL,
    created_on   date,
    update_on    date,
    is_deleted   BOOLEAN DEFAULT FALSE,
    product_id   VARCHAR(26),
    image_url    VARCHAR(255) NOT NULL,
    alt_text     VARCHAR(255),
    is_primary   BOOLEAN DEFAULT FALSE,
    sort_order   INTEGER,
    storage_type VARCHAR(255),
    CONSTRAINT pk_product_images PRIMARY KEY (id)
);

CREATE TABLE product_statuses
(
    id         VARCHAR(26)  NOT NULL,
    created_on date,
    update_on  date,
    is_deleted BOOLEAN DEFAULT FALSE,
    name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_product_statuses PRIMARY KEY (id)
);

CREATE TABLE products
(
    id                VARCHAR(26)       NOT NULL,
    created_on        date,
    update_on         date,
    is_deleted        BOOLEAN DEFAULT FALSE,
    name              VARCHAR(255)      NOT NULL,
    description       VARCHAR(255),
    price             DECIMAL(20, 2)    NOT NULL,
    stock_quantity    INTEGER DEFAULT 0 NOT NULL,
    category_id       VARCHAR(26),
    product_status_id VARCHAR(26),
    is_featured       BOOLEAN,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE reviews
(
    id         VARCHAR(26) NOT NULL,
    created_on date,
    update_on  date,
    is_deleted BOOLEAN DEFAULT FALSE,
    user_id    VARCHAR(26) NOT NULL,
    product_id VARCHAR(26) NOT NULL,
    rating     INTEGER     NOT NULL,
    comment    VARCHAR(255),
    CONSTRAINT pk_reviews PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id         VARCHAR(26)  NOT NULL,
    created_on date,
    update_on  date,
    is_deleted BOOLEAN DEFAULT FALSE,
    name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            VARCHAR(26)  NOT NULL,
    created_on    date,
    update_on     date,
    is_deleted    BOOLEAN DEFAULT FALSE,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    phone         VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    role_id       VARCHAR(26)  NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE product_statuses
    ADD CONSTRAINT uc_product_statuses_name UNIQUE (name);

ALTER TABLE products
    ADD CONSTRAINT uc_products_name UNIQUE (name);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_phone UNIQUE (phone);

ALTER TABLE categories
    ADD CONSTRAINT FK_CATEGORIES_ON_PARENT FOREIGN KEY (parent_id) REFERENCES categories (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE products
    ADD CONSTRAINT FK_PRODUCTS_ON_PRODUCTSTATUS FOREIGN KEY (product_status_id) REFERENCES product_statuses (id);

ALTER TABLE product_images
    ADD CONSTRAINT FK_PRODUCT_IMAGES_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE reviews
    ADD CONSTRAINT FK_REVIEWS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE reviews
    ADD CONSTRAINT FK_REVIEWS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ROLE FOREIGN KEY (role_id) REFERENCES roles (id);