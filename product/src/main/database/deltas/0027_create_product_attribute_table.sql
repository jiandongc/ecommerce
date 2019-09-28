CREATE TABLE product_attribute
(
    id serial PRIMARY KEY,
    product_id int REFERENCES product(id) not null,
    key varchar(50) not null,
    value varchar(50) not null
);