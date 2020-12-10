CREATE TABLE product
(
    product_id          BIGINT PRIMARY KEY,
    product_name        TEXT                NULL,
    product_price       DECIMAL             NOT NULL DEFAULT 0,
    product_brand       TEXT                NOT NULL,
    product_colour      TEXT                NOT NULL,
    product_status      TEXT                NULL,
    created_at          TIMESTAMPTZ         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          TEXT                NULL,
    updated_by          TEXT                NULL
);

CREATE TABLE shopping_cart
(
    shopping_cart_id      BIGINT PRIMARY KEY,
    buyer_id              BIGINT NOT NULL,
    status                TEXT        NOT NULL,
    created_at            TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE shopping_cart_item
(
    shopping_cart_item_id      UUID PRIMARY KEY,
    shopping_cart_id           BIGINT NOT NULL,
    quantity                   INT NOT NULL,
    product_id                 BIGINT NOT NULL,
    price                      DECIMAL NOT NULL DEFAULT 0,
    created_at                 TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE shopping_order
(
    order_id      BIGINT PRIMARY KEY,
    buyer_id      BIGINT NOT NULL,
    cart_id       BIGINT NOT NULL,
    status        TEXT        NOT NULL,

    payment_method              TEXT     NOT NULL,

-- shipping address
    shipping_name               TEXT     NOT NULL,
    shipping_phone_number       TEXT     NOT NULL,
    shipping_region             TEXT     NOT NULL,
    shipping_street             TEXT     NOT NULL,

    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE outbox
(
    event_id      UUID PRIMARY KEY,
    topic         TEXT        NOT NULL,
    payload       TEXT        NOT NULL,
    trace_context JSONB,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cmd_proceeded
(
    cmd_proceeded_id TEXT PRIMARY KEY,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);