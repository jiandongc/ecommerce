CREATE TABLE address
(
    id serial PRIMARY KEY,
    address_type varchar(10) NOT NULL,
    title varchar(10) NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    mobile varchar(20),
    address_line_1 varchar(255) NOT NULL,
    address_line_2 varchar(255),
    address_line_3 varchar(255),
    city varchar(60) NOT NULL,
    country varchar(60) NOT NULL,
    post_code varchar(10) NOT NULL,
    shopping_cart_id integer REFERENCES shopping_cart (id) NOT NULL,
    UNIQUE (shopping_cart_id, address_type)
);