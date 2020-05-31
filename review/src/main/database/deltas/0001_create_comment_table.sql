CREATE TABLE comment
(
    id serial PRIMARY KEY,
    code varchar(50),
    comment text not null,
    vote int default 0,
    active boolean DEFAULT true,
    creation_time timestamp default now()
);