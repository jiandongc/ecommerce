ALTER TABLE delivery_option DROP COLUMN eta_from_date;
ALTER TABLE delivery_option DROP COLUMN eta_to_date;
ALTER TABLE delivery_option ADD COLUMN min_days_required int;
ALTER TABLE delivery_option ADD COLUMN max_days_required int;
ALTER TABLE delivery_option ADD COLUMN creation_time timestamp default now();
ALTER TABLE delivery_option ADD COLUMN last_update_time timestamp;
