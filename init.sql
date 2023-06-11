CREATE TABLE address
(
    id       SERIAL PRIMARY KEY,
    street   VARCHAR(50) NULL,
    city     VARCHAR(50) NULL,
    state    VARCHAR(50)
);

CREATE TABLE tb_client
(
    id         SERIAL PRIMARY KEY,
    password   VARCHAR(500) NOT NULL,
    email      VARCHAR(50) NULL,
    name       VARCHAR(50) NOT NULL,
    score      VARCHAR(50)  NULL,
    address_id INTEGER NULL
);

