CREATE TABLE post_category_link
(
    id serial PRIMARY KEY,
    post_id int REFERENCES post(id) not null,
    post_category_id int REFERENCES post_category(id) not null
);
