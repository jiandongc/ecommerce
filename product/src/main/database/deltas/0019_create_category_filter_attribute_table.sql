CREATE TABLE category_filter_attribute
(
    id serial PRIMARY KEY,
    category_id int REFERENCES category(id) not null,
    attribute_id int REFERENCES attribute(id) not null
);
