CREATE TABLE price
(
    id serial PRIMARY KEY,
    price double precision not null,
    start_date date not null,
    end_date date,
    discount_rate varchar(50),
    creation_time timestamp default now(),
    sku_id integer REFERENCES sku (id) NOT NULL
);