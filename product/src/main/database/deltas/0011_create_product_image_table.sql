CREATE TABLE product_image
(
    id serial PRIMARY KEY,
    product_id int REFERENCES product(id) not null,
    image_type_id int REFERENCES image_type(id) not null,
    url text not null
);
