ALTER TABLE order_item
    ADD COLUMN vat_rate integer NOT NULL,
    ADD COLUMN vat double precision NOT NULL,
    ADD COLUMN sale double precision NOT NULL;

