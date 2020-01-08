ALTER TABLE brand
    ADD COLUMN description text,
    ADD COLUMN country varchar(50),
    ADD COLUMN image_url text,
    ADD COLUMN start_date date,
    ADD COLUMN end_date date;
