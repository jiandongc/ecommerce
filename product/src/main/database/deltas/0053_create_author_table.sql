CREATE TABLE author
(
    id serial PRIMARY KEY,
    name varchar(255) not null,
    slug varchar(100) not null,
    summary varchar(255),
    url text,
    image text,
    create_date date DEFAULT CURRENT_DATE,
    published boolean DEFAULT false
);
