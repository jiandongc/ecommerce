CREATE TABLE product
(
    id serial PRIMARY KEY,
    product_id int not null,
    type varchar(50) not null,
    start_date date not null,
    end_date date,
    customer_id int REFERENCES customer(id) NOT NULL
);