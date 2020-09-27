ALTER TABLE product DROP CONSTRAINT IF EXISTS product_customer_id_fkey;

ALTER TABLE product ALTER COLUMN customer_id DROP NOT NULL;