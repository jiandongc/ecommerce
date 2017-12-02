CREATE TABLE shopping_cart_item
(
    id serial PRIMARY KEY,
    shopping_cart_id integer REFERENCES shopping_cart (id) not null,
    sku varchar(50) not null,
    product_name varchar(255),
    unit_price double precision,
    quantity smallint default(1),
    image_url text,
    creation_time timestamp default now(),
    last_update_time timestamp,
    UNIQUE (shopping_cart_id, sku)
);