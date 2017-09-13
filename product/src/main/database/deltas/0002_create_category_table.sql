CREATE TABLE category
(
    id serial PRIMARY KEY,
    name varchar(255),
    description text,
    image_url text,
    hidden boolean not null,
    parent_id int
);
