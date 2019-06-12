CREATE TABLE delivery_option
(
    id serial PRIMARY KEY,
    method varchar(50),
    charge double precision,
    eta_from_date date,
    eta_to_date date,
    shopping_cart_id integer REFERENCES shopping_cart (id) NOT NULL
);