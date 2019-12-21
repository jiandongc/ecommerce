CREATE TABLE token
(
    id serial PRIMARY KEY,
    text varchar(255) not null,
    type varchar(50) not null,
    start_ts timestamp default now(),
    end_ts timestamp,
    customer_id int REFERENCES customer(id) NOT NULL
);