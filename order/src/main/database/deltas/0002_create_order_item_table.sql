CREATE TABLE order_item
(
    id serial PRIMARY KEY,
    order_id integer REFERENCES orders (id) not null,
    sku varchar(50) not null,
    product_code varchar(50) not null,
    product_name varchar(255) not null,
    product_description varchar(255),
    unit_price double precision not null,
    quantity int not null,
    sub_total double precision not null,
    image_url text,
    UNIQUE (order_id, sku)
);