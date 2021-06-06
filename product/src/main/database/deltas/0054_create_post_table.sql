CREATE TABLE post
(
    id serial PRIMARY KEY,
    title varchar(255) not null,
    slug varchar(100) not null,
    summary varchar(255),
    image text,
    content text,
    create_date date DEFAULT CURRENT_DATE,
    update_date date,
    published boolean DEFAULT false,
    author_id int REFERENCES author(id) not null
);
