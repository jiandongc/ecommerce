CREATE TABLE category_attribute
(
    id serial PRIMARY KEY,
    category_id int REFERENCES category(id) not null,
    key varchar(50) not null,
    value varchar(50) not null,
    ordering smallint not null
);