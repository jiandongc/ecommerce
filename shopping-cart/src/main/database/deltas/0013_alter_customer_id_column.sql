ALTER TABLE shopping_cart DROP COLUMN customer_id;
ALTER TABLE shopping_cart ADD COLUMN customer_uid uuid;
