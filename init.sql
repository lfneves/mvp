CREATE TABLE address
(
    id       SERIAL PRIMARY KEY,
    street   VARCHAR(50) NULL,
    city     VARCHAR(50) NULL,
    state    VARCHAR(50)
);

CREATE TABLE reactive_user
(
    id         serial PRIMARY KEY,
    password   VARCHAR(500) NOT NULL,
    name       VARCHAR(50) NOT NULL,
    score      VARCHAR(50)  NULL,
    address_id INTEGER NULL
);

