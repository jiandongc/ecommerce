CREATE TABLE post_meta
(
    id serial PRIMARY KEY,
    post_id int REFERENCES post(id) not null,
    name varchar(50) not null,
    content varchar(255) not null
);
