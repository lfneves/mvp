CREATE TABLE tb_address
(
    id       SERIAL PRIMARY KEY,
    street   VARCHAR(50) NULL,
    city     VARCHAR(50) NULL,
    state    VARCHAR(50) NULL,
    postal_code VARCHAR(50) NULL
);

CREATE TABLE tb_client
(
    id         SERIAL PRIMARY KEY,
    password   VARCHAR(500) NOT NULL,
    email      VARCHAR(50) NULL,
    cpf        VARCHAR(50) NULL,
    name       VARCHAR(50) NOT NULL,
    id_address INTEGER REFERENCES tb_address(id)
);

CREATE TABLE tb_category
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(250) NOT NULL,
    description VARCHAR(250) NULL
);

CREATE TABLE tb_product
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR NOT NULL,
    price       NUMERIC NOT NULL,
    quantity    INTEGER NULL,
    id_category INTEGER REFERENCES tb_category(id)
);

CREATE TABLE tb_order
(
    id                  SERIAL  PRIMARY KEY,
    id_client           INTEGER REFERENCES tb_client(id) NOT NULL,
    total_price         NUMERIC NOT NULL,
    checkout            BOOLEAN DEFAULT FALSE
);

CREATE TABLE tb_order_product
(
    id_order_product    SERIAL  PRIMARY KEY,
    id_product          INTEGER REFERENCES tb_product(id),
    id_order            INTEGER REFERENCES tb_order(id)
);

