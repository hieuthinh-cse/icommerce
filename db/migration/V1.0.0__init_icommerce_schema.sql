CREATE TABLE product
(
    product_id          BIGINT PRIMARY KEY,
    product_name        TEXT                NULL,
    product_price       DECIMAL             NOT NULL DEFAULT 0,
    product_status      TEXT                NULL,
    created_at          TIMESTAMPTZ         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMPTZ         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          TEXT                NULL,
    updated_by          TEXT                NULL
);

CREATE TABLE buyer
(
    buyer_id      UUID PRIMARY KEY,
    email         VARCHAR(320) NOT NULL,
    password      CHAR(96)     NOT NULL,
    buyer_name    TEXT         NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    TEXT         NOT NULL,
    updated_by    TEXT         NOT NULL
);

CREATE TABLE role
(
    role_name   CHAR(3) PRIMARY KEY,
    description TEXT        NOT NULL UNIQUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by  TEXT        NOT NULL,
    updated_by  TEXT        NOT NULL
);

CREATE TABLE account
(
    account_id    UUID PRIMARY KEY,
    email         VARCHAR(320) NOT NULL,
    password      CHAR(96)     NOT NULL,
    account_name  TEXT         NOT NULL,
    role_name     CHAR(3)      NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    TEXT         NOT NULL,
    updated_by    TEXT         NOT NULL,

    FOREIGN KEY (role_name) REFERENCES role (role_name) ON DELETE CASCADE
);

CREATE UNIQUE INDEX email_unique_idx on account (LOWER(email));

CREATE TABLE privilege
(
    privilege_name CHAR(7) PRIMARY KEY,
    description    TEXT        NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     TEXT        NOT NULL,
    updated_by     TEXT        NOT NULL
);

CREATE TABLE role_privilege
(
    role_name      CHAR(3)     NOT NULL,
    privilege_name CHAR(7)     NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     TEXT        NOT NULL,

    PRIMARY KEY (role_name, privilege_name),
    FOREIGN KEY (role_name) REFERENCES role (role_name) ON DELETE CASCADE,
    FOREIGN KEY (privilege_name) REFERENCES privilege (privilege_name) ON DELETE CASCADE
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