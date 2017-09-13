CREATE TABLE product
(
    id serial PRIMARY KEY,
    name varchar(255) not null,
    description text,
    parent_id int null
);
