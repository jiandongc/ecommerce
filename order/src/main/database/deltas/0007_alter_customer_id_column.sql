ALTER TABLE orders DROP COLUMN customer_id;
ALTER TABLE orders ADD COLUMN customer_uid uuid;
