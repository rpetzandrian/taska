CREATE TABLE organizations
(
    id                CHAR(36) NOT NULL,
    code              VARCHAR(10) NOT NULL,
    name              VARCHAR(100) NOT NULL,
    description       CHARACTER,
    CONSTRAINT organizations_pkey PRIMARY KEY (id)
);


CREATE INDEX organizations_code_idx ON organizations (code);