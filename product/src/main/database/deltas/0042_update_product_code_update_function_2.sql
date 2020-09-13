CREATE OR REPLACE FUNCTION update_product_code() RETURNS trigger AS $update_product_code$
DECLARE category_code varchar(50);
DECLARE product_id varchar(50);
BEGIN
    SELECT c.category_code INTO category_code FROM category c WHERE c.id = NEW.category_id;
    product_id:=lpad(NEW.id::varchar, 7, '0');
    UPDATE product SET product_code = concat(category_code, '-', product_id) WHERE id = NEW.id AND product_code IS NULL;
    RETURN NEW;
END;
$update_product_code$ LANGUAGE plpgsql;