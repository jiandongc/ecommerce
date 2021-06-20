CREATE TABLE delivery_option_offer
(
    id serial PRIMARY KEY,
    country_code varchar(10) not null,
    method varchar(50) not null,
    charge double precision not null,
    min_days_required integer not null,
    max_days_required integer not null,
    vat_rate integer not null,
    start_date date,
    end_date date,
    creation_time timestamp default now()
);