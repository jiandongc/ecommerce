CREATE TABLE anon_cart_item
(
id int PRIMARY KEY,
anon_cart_id integer REFERENCES anon_cart (id),
product_id integer,
product_name varchar(255),
product_price double precision,
quantity smallint
);