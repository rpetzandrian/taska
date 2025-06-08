CREATE TABLE sprints
(
    id                CHAR(36) NOT NULL,
    organization_code VARCHAR(10) NOT NULL,
    name              VARCHAR(100) NOT NULL,
    description       CHARACTER,
    start_date        timestamp,
    end_date          timestamp,
    status            VARCHAR(10),
    created_by        CHAR(36),
    CONSTRAINT sprints_pkey PRIMARY KEY (id)
);

CREATE INDEX sprints_organization_code_idx ON sprints (organization_code);
CREATE INDEX sprints_created_by_idx ON sprints (created_by);
CREATE INDEX sprint_status_idx ON sprints (created_by);
