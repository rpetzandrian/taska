CREATE TABLE users
(
    id                CHAR(36) NOT NULL,
    name              VARCHAR(150),
    email             VARCHAR(150),
    password          VARCHAR(100),
    role              VARCHAR(10),
    organization_code VARCHAR(10),
    token             VARCHAR(100),
    expired_at        TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT users_email_key UNIQUE (email);

CREATE INDEX users_organization_code_idx ON users (organization_code);