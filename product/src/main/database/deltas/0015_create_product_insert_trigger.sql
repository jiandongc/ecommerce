CREATE TRIGGER update_product_code AFTER INSERT ON product
    FOR EACH ROW EXECUTE PROCEDURE update_product_code();