CREATE TABLE product_tag
(
    id serial PRIMARY KEY,
    code varchar(50),
    tag varchar(50) not null,
    color_hex varchar(50),
    start_date date not null,
    end_date date,
    creation_time timestamp default now(),
    product_id integer REFERENCES product (id) NOT NULL
);