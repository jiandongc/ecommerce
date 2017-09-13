CREATE TABLE sku_attribute_value
(
    id serial PRIMARY KEY,
    sku_id int REFERENCES sku(id) not null,
    attribute_value_id int REFERENCES attribute_value(id) not null
);
