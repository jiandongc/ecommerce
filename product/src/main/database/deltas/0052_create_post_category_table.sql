CREATE TABLE post_category
(
    id serial PRIMARY KEY,
    name varchar(255) not null,
    slug varchar(100) not null,
    summary varchar(255),
    image text,
    published boolean DEFAULT false,
    parent_id int
);
