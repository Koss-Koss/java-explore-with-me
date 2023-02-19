CREATE TABLE IF NOT EXISTS hits (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    app VARCHAR(100) NOT NULL,
    uri VARCHAR(100) NOT NULL,
    ip VARCHAR(30) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
    );