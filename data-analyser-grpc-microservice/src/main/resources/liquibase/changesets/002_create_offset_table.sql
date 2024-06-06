create table offsets
(   id BIGSERIAL,
    current_offset BIGINT DEFAULT 0
);

INSERT INTO offsets
VALUES (0)