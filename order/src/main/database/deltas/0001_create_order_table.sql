CREATE TABLE orders
(
    id serial PRIMARY KEY,
    order_number varchar(255) not null,
    customer_id int not null,
    items double precision not null,
    postage double precision not null,
    promotion double precision not null,
    total_before_vat double precision not null,
    items_vat double precision not null,
    postage_vat double precision not null,
    promotion_vat double precision not null,
    total_vat double precision not null,
    order_total double precision not null,
    order_date date not null,
    delivery_method varchar(50),
    min_days_required int,
    max_days_required int,
    creation_time timestamp default now()
);