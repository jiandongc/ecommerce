CREATE TABLE sku
(
    id serial PRIMARY KEY,
    product_id int REFERENCES product(id) not null,
    sku varchar(50) not null,
    stock_quantity int not null,
    price double precision
);
