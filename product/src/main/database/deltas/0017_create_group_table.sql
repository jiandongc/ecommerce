CREATE TABLE product_group
(
    product_group int not null,
    type varchar(50) not null,
    product_id int REFERENCES product(id) not null
);
