CREATE TABLE attribute_value
(
    id serial PRIMARY KEY,
    attribute_id int REFERENCES attribute(id) not null,
    attribute_value varchar(255)
);
