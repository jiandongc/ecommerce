ALTER TABLE address DROP COLUMN first_name;
ALTER TABLE address DROP COLUMN last_name;
ALTER TABLE address ADD COLUMN name varchar(255) NOT NULL;

