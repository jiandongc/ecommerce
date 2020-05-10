CREATE TABLE product_owner.changelog
(
	change_number bigint NOT NULL,
 	delta_set varchar(10),
 	start_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	complete_dt timestamp NULL DEFAULT NULL,
	applied_by varchar(100) NOT NULL,
	description varchar(500) NOT NULL,
	PRIMARY KEY (change_number)
);