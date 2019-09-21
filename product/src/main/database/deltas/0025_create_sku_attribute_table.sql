CREATE TABLE sku_attribute
(
    id serial PRIMARY KEY,
    sku_id int REFERENCES sku(id) not null,
    key varchar(50) not null,
    value varchar(50) not null
);