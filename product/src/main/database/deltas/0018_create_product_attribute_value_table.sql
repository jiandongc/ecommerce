CREATE TABLE product_attribute_value
(
    id serial PRIMARY KEY,
    product_id int REFERENCES product(id) not null,
    attribute_value_id int REFERENCES attribute_value(id) not null
);
