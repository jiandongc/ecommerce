CREATE TABLE voucher
(
    id serial PRIMARY KEY,
    type varchar(50) NOT NULL,
    code varchar(50) UNIQUE NOT NULL,
    name varchar(255),
    max_uses int NOT NULL DEFAULT 1,
    max_uses_user int NOT NULL DEFAULT 1,
    min_spend double precision,
    discount_amount double precision,
    start_date date NOT NULL,
    end_date date,
    customer_uid uuid,
    creation_time timestamp DEFAULT now(),
    soft_delete boolean DEFAULT false
);