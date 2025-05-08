create TABLE cart_items
(
    id         VARCHAR(26)    NOT NULL,
    created_on date,
    updated_on date,
    deleted    BOOLEAN DEFAULT FALSE,
    variant_id VARCHAR(255)   NOT NULL,
    name       VARCHAR(255)   NOT NULL,
    image_url  VARCHAR(255),
    quantity   INTEGER        NOT NULL,
    price      DECIMAL(20, 2) NOT NULL,
    cart_id    VARCHAR(26),
    CONSTRAINT pk_cart_items PRIMARY KEY (id)
);

create TABLE carts
(
    id         VARCHAR(26) NOT NULL,
    created_on date,
    updated_on date,
    deleted    BOOLEAN DEFAULT FALSE,
    user_id    VARCHAR(26),
    CONSTRAINT pk_carts PRIMARY KEY (id)
);

create TABLE orders
(
    id               VARCHAR(26) NOT NULL,
    created_on       date,
    updated_on       date,
    deleted          BOOLEAN DEFAULT FALSE,
    user_id          VARCHAR(255),
    shipping_address VARCHAR(255),
    phone            VARCHAR(255),
    total_price      DECIMAL,
    status           VARCHAR(255),
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

create TABLE orders_items
(
    orders_id  VARCHAR(26) NOT NULL,
    variant_id VARCHAR(255),
    name       VARCHAR(255),
    quantity   INTEGER,
    price      DECIMAL
);

alter table carts
    add CONSTRAINT FK_CARTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

alter table cart_items
    add CONSTRAINT FK_CART_ITEMS_ON_CART FOREIGN KEY (cart_id) REFERENCES carts (id);

alter table orders_items
    add CONSTRAINT fk_orders_items_on_order FOREIGN KEY (orders_id) REFERENCES orders (id);