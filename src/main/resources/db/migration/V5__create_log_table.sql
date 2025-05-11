CREATE TABLE logs (
    id                CHAR(36) NOT NULL,
    created_at        timestamp,
    reference_id      CHAR(36),
    type              VARCHAR(10),
    content           JSONB,
    action            VARCHAR(10),
    CONSTRAINT logs_pkey PRIMARY KEY (id)
);

CREATE INDEX logs_reference_id_idx on logs (reference_id);
