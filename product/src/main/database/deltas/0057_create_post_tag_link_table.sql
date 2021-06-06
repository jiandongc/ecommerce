CREATE TABLE post_tag_link
(
    id serial PRIMARY KEY,
    post_id int REFERENCES post(id) not null,
    post_tag_id int REFERENCES post_tag(id) not null
);
