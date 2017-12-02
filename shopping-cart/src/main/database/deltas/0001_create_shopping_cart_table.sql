CREATE TABLE shopping_cart
(
    id serial PRIMARY KEY,
    cart_uid uuid not null,
    customer_id int,
    creation_time timestamp default now()
);