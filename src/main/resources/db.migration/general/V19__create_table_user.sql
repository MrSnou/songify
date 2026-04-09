CREATE SEQUENCE IF NOT EXISTS users_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    authorities TEXT[],
    enabled     BOOLEAN      NOT NULL
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

