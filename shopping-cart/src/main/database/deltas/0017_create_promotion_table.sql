CREATE TABLE promotion
(
    id serial PRIMARY KEY,
    voucher_code varchar(50),
    discount_amount double precision,
    vat_rate integer,
    creation_time timestamp DEFAULT now(),
    last_update_time timestamp,
    shopping_cart_id integer REFERENCES shopping_cart (id) NOT NULL
);