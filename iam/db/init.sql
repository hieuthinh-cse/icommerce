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