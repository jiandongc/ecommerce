CREATE TABLE order_address
(
    id serial PRIMARY KEY,
    order_id integer REFERENCES orders (id) not null,
    address_type varchar(10) NOT NULL,
    name varchar(255) NOT NULL,
    title varchar(10) NOT NULL,
    mobile varchar(20),
    address_line_1 varchar(255) NOT NULL,
    address_line_2 varchar(255),
    address_line_3 varchar(255),
    city varchar(60) NOT NULL,
    country varchar(60) NOT NULL,
    post_code varchar(10) NOT NULL,
    UNIQUE (order_id, address_type)
);