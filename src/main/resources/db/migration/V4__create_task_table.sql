CREATE TABLE tasks
(
    id                CHAR(36) NOT NULL,
    name              VARCHAR(100) NOT NULL,
    sprint_id         CHAR(36),
    description       CHARACTER,
    priority          SMALLINT,
    story_point       SMALLINT,
    reporter_id       CHAR(36),
    assignee_id       CHAR(36),
    status            VARCHAR(10),
    type              VARCHAR(10),
    CONSTRAINT tasks_pkey PRIMARY KEY (id)
);

CREATE INDEX tasks_sprint_id_idx ON tasks (sprint_id);
CREATE INDEX tasks_reporter_id_idx ON tasks (reporter_id);
CREATE INDEX tasks_assignee_id_idx ON tasks (assignee_id);