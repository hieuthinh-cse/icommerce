CREATE TABLE buyer
(
    buyer_id           BIGINT PRIMARY KEY,
    email              VARCHAR(320) NOT NULL,
    password           CHAR(96)     NULL,
    buyer_name         TEXT         NOT NULL,
    social_id          TEXT         NOT NULL,
    social_platform    TEXT         NOT NULL,
    status             TEXT         NOT NULL,
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         TEXT         NOT NULL,
    updated_by         TEXT         NOT NULL
);

CREATE UNIQUE INDEX email_unique_idx on buyer (LOWER(email));

CREATE TABLE buyer_address
(
    address_id         UUID PRIMARY KEY,
    buyer_id           BIGINT       NOT NULL,
    name               TEXT         NOT NULL,
    default_address    BOOLEAN      NOT NULL,
    phone_number       TEXT         NOT NULL,
    region             TEXT         NOT NULL,
    street             TEXT         NOT NULL,
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (buyer_id) REFERENCES buyer (buyer_id) ON DELETE CASCADE
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

-- role
INSERT INTO role(role_name, description, created_at, updated_at, created_by, updated_by)
VALUES ('ADM', 'System Administrator', '2020-04-01 07:00:00', '2020-04-01 07:00:00', '', '');

-- privilege
INSERT INTO privilege(privilege_name, description, created_at, updated_at, created_by, updated_by)
VALUES ('REA_PRO', 'Read Product', '2020-04-01 07:00:00', '2020-04-01 07:00:00', '', '');
INSERT INTO privilege(privilege_name, description, created_at, updated_at, created_by, updated_by)
VALUES ('WRI_PRO', 'Write Product', '2020-04-01 07:00:00', '2020-04-01 07:00:00', '', '');

-- role_privilege
INSERT INTO role_privilege(role_name, privilege_name, created_at, created_by)
VALUES ('ADM', 'REA_PRO', '2020-04-01 07:00:00', '');
INSERT INTO role_privilege(role_name, privilege_name, created_at, created_by)
VALUES ('ADM', 'WRI_PRO', '2020-04-01 07:00:00', '');

-- buyer
INSERT INTO buyer (buyer_id,email,"password",buyer_name,social_id,social_platform,status,created_by,updated_by) VALUES
(360009466,'buyer@gmail.com','$argon2id$v=19$m=4096,t=3,p=1$IY5eWqE64TcKsdNBOIDSew$PueFKwzqUhExkeaIXGKtyWrMqgwZg834rkqG4yUBxcY','Thịnh Nguyễn','902162113258707','FACEBOOK','ACTIVE','System','System');