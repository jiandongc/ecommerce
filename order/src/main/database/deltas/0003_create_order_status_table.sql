CREATE TABLE order_status
(
    id serial PRIMARY KEY,
    order_id integer REFERENCES orders (id) not null,
    status varchar(50) not null,
    description varchar(255),
    creation_time timestamp default now()
);