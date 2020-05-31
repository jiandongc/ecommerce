CREATE TABLE response
(
    id serial PRIMARY KEY,
    comment_id int REFERENCES comment(id) not null,
    response text not null,
    vote int default 0,
    active boolean DEFAULT true,
    creation_time timestamp default now()
);