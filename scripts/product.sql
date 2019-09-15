delete from product.sku_attribute_value;
delete from product.product_attribute_value;
delete from product.attribute_value;
delete from product.category_filter_attribute;
delete from product.attribute;
delete from product.price;
delete from product.sku;
delete from product.product_image;
delete from product.image_type;
delete from product.product_group;
delete from product.product;
delete from product.brand;
delete from product.category;

--brands
insert into product.brand(name) values ('东丰制果');
insert into product.brand(name) values ('創味食品');
insert into product.brand(name) values ('伊藤久右卫门');
insert into product.brand(name) values ('资生堂');
insert into product.brand(name) values ('CANMAKE/井田制药');
insert into product.brand(name) values ('嘉娜宝Kanebo—KATE');

--categories
insert into product.category(name, description, image_url, hidden, category_code) values ('Clothing', 'Clothing', '/images/ps0001.jpg', false, 'msg');
insert into product.category(name, description, image_url, hidden, parent_id, category_code) values ('Nightwear', 'Nightwear', '/images/ps0001.jpg', false, (select id from product.category where category_code = 'msg'), 'xxls');
insert into product.category(name, description, image_url, hidden, parent_id, category_code) values ('Bodysuits', 'Bodysuits', '/images/ps0001.jpg', false, (select id from product.category where category_code = 'msg'), 'cyl');
insert into product.category(name, description, image_url, hidden, parent_id, category_code) values ('Baby Nightwear', 'Baby Nightwear', '/images/ps0001.jpg', false, (select id from product.category where category_code = 'xxls'), 'bxxls');
insert into product.category(name, description, image_url, hidden, parent_id, category_code) values ('Adult Nightwear', 'Adult Nightwear', '/images/ps0001.jpg', false, (select id from product.category where category_code = 'xxls'), 'axxls');
insert into product.category(name, description, image_url, hidden, parent_id, category_code) values ('Baby Bodysuits', 'Baby Bodysuits', '/images/ps0001.jpg', false, (select id from product.category where category_code = 'cyl'), 'bcyl');
insert into product.category(name, description, image_url, hidden, parent_id, category_code) values ('Adult Bodysuits', 'Adult Bodysuits', '/images/ps0001.jpg', false, (select id from product.category where category_code = 'cyl'), 'acyl');
insert into product.category(name, description, image_url, hidden, category_code) values ('Little Home', 'Little Home', '/images/ps0001.jpg', false, 'mzg');
insert into product.category(name, description, image_url, hidden, parent_id, category_code) values ('Storage', 'Storage', '/images/ps0001.jpg', false, (select id from product.category where category_code = 'mzg'), 'mzgj');
insert into product.category(name, description, image_url, hidden, parent_id, category_code) values ('Room Accesories', 'Room Accesories', '/images/ps0001.jpg', false, (select id from product.category where category_code = 'mzg'), 'fsgl');

--attribute
insert into product.attribute(name) values ('Size');
insert into product.attribute(name) values ('Father''s size');
insert into product.attribute(name) values ('Mother''s size');
insert into product.attribute(name) values ('Baby''s size');
insert into product.attribute(name) values ('Color');
insert into product.attribute(name) values ('Brand');
insert into product.attribute(name) values ('Shape');

insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Size'), 'S');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Size'), 'M');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Size'), 'L');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Size'), 'XL');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Baby''s size'), '0-3 Months');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Baby''s size'), '3-6 Months');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Baby''s size'), '6-9 Months');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Baby''s size'), '9-12 Months');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Mother''s size'), 'S');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Mother''s size'), 'M');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Mother''s size'), 'L');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Mother''s size'), 'XL');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Father''s size'), 'S');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Father''s size'), 'M');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Father''s size'), 'L');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Father''s size'), 'XL');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Color'), 'blue');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Color'), 'red');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Color'), 'black');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Brand'), 'nike');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Brand'), 'adidas');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Brand'), 'new balance');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Shape'), 'square');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Shape'), 'trinagle');
insert into product.attribute_value(attribute_id, attribute_value) values((select id from product.attribute where name = 'Shape'), 'circle');

--category_filter_attribute
insert into product.category_filter_attribute(category_id, attribute_id, ordering) values ((select id from product.category where name = 'Storage'), (select id from product.attribute where name = 'Color'), 0);
insert into product.category_filter_attribute(category_id, attribute_id, ordering) values ((select id from product.category where name = 'Storage'), (select id from product.attribute where name = 'Brand'), 1);
insert into product.category_filter_attribute(category_id, attribute_id, ordering) values ((select id from product.category where name = 'Storage'), (select id from product.attribute where name = 'Shape'), 2);

--image types
insert into product.image_type(type) values ('Main');
insert into product.image_type(type) values ('Detail');
insert into product.image_type(type) values ('Color');

--products
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Clothing'), (select id from product.brand where name = '东丰制果'), '', 'White Christening Photo Frame');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Clothing'), (select id from product.brand where name = '創味食品'), '', 'Ditsy Print Backpack');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Clothing'), (select id from product.brand where name = '东丰制果'), '', 'Blue Ditsy Storage Bag');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Clothing'), (select id from product.brand where name = '伊藤久右卫门'), '', 'Personalised Star Toy Chest');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Nightwear'), (select id from product.brand where name = '东丰制果'), '', 'Mini White Star Storage Bag');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Nightwear'), (select id from product.brand where name = '創味食品'), '', 'Large Pink Stripe and Star Storage Box');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Nightwear'), (select id from product.brand where name = '东丰制果'), '', 'White Personalised Rocking Chair');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Nightwear'), (select id from product.brand where name = '伊藤久右卫门'), '', 'Blue Slogan Bodysuit - I Heart Daddy');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Bodysuits'), (select id from product.brand where name = '东丰制果'), '', 'Good Looks Bodysuit');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Bodysuits'), (select id from product.brand where name = 'CANMAKE/井田制药'), '', 'Pink Elephant Storage Tub');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Bodysuits'), (select id from product.brand where name = 'CANMAKE/井田制药'), '', 'Blue Elephant Storage Tub');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Bodysuits'), (select id from product.brand where name = 'CANMAKE/井田制药'), '', 'Personalised Grey Star Laundry Bag');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Bodysuits'), (select id from product.brand where name = '东丰制果'), '', 'Personalised Pink Crown Sleepsuit');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Little Home'), (select id from product.brand where name = '創味食品'), '', 'Personalised Blue Splash Print Sleepsuit');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Little Home'), (select id from product.brand where name = '嘉娜宝Kanebo—KATE'), '', 'Personalised Angel Wings Sleepsuit - Pink');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Little Home'), (select id from product.brand where name = '伊藤久右卫门'), '', 'Personalised Pink And White Checked Pyjamas');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Storage'), (select id from product.brand where name = '东丰制果'), '', 'Personalised Blue Splash Print 3 Pack of Bandana Bibs');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Storage'), (select id from product.brand where name = '伊藤久右卫门'), '', 'CiCi Personalised Grey Star Laundry Bag');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Storage'), (select id from product.brand where name = '嘉娜宝Kanebo—KATE'), '', 'Green & Grey Round Table and Stool Set');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Room Accesories'), (select id from product.brand where name = '资生堂'), '', 'Personalised White Cloud Night Light');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Room Accesories'), (select id from product.brand where name = '资生堂'), '', '4 Pack Personalised Bear Socks');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Room Accesories'), (select id from product.brand where name = 'CANMAKE/井田制药'), '', 'CiCi Personalised Pink Gingham Robe');
insert into product.product(category_id, brand_id, description, name) values ((select id from product.category where name = 'Room Accesories'), (select id from product.brand where name = 'CANMAKE/井田制药'), '', 'CiCi Personalised Pink Gingham Robe - Blue');


insert into product.product_attribute_value(product_id, attribute_value_id) values ((select id from product.product where name='Green & Grey Round Table and Stool Set'), (select min(id)+16 from attribute_value));
insert into product.product_attribute_value(product_id, attribute_value_id) values ((select id from product.product where name='CiCi Personalised Grey Star Laundry Bag'), (select min(id)+17 from attribute_value));
insert into product.product_attribute_value(product_id, attribute_value_id) values ((select id from product.product where name='Personalised Blue Splash Print 3 Pack of Bandana Bibs'), (select min(id)+18 from attribute_value));
insert into product.product_attribute_value(product_id, attribute_value_id) values ((select id from product.product where name='Green & Grey Round Table and Stool Set'), (select min(id)+19 from attribute_value));
insert into product.product_attribute_value(product_id, attribute_value_id) values ((select id from product.product where name='CiCi Personalised Grey Star Laundry Bag'), (select min(id)+20 from attribute_value));
insert into product.product_attribute_value(product_id, attribute_value_id) values ((select id from product.product where name='Personalised Blue Splash Print 3 Pack of Bandana Bibs'), (select min(id)+21 from attribute_value));
insert into product.product_attribute_value(product_id, attribute_value_id) values ((select id from product.product where name='Green & Grey Round Table and Stool Set'), (select min(id)+22 from attribute_value));
insert into product.product_attribute_value(product_id, attribute_value_id) values ((select id from product.product where name='CiCi Personalised Grey Star Laundry Bag'), (select min(id)+23 from attribute_value));
insert into product.product_attribute_value(product_id, attribute_value_id) values ((select id from product.product where name='Personalised Blue Splash Print 3 Pack of Bandana Bibs'), (select min(id)+24 from attribute_value));

--product image
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='White Christening Photo Frame'), (select id from product.image_type where type='Main'), '/images/ps0001.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Ditsy Print Backpack'), (select id from product.image_type where type='Main'), '/images/ps0002.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Blue Ditsy Storage Bag'), (select id from product.image_type where type='Main'), '/images/ps0003.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Star Toy Chest'), (select id from product.image_type where type='Main'), '/images/ps0004.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Mini White Star Storage Bag'), (select id from product.image_type where type='Main'), '/images/ps0005.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Large Pink Stripe and Star Storage Box'), (select id from product.image_type where type='Main'), '/images/ps0006.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='White Personalised Rocking Chair'), (select id from product.image_type where type='Main'), '/images/ps0007.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Blue Slogan Bodysuit - I Heart Daddy'), (select id from product.image_type where type='Main'), '/images/ps0008.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Good Looks Bodysuit'), (select id from product.image_type where type='Main'), '/images/ps0009.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Pink Elephant Storage Tub'), (select id from product.image_type where type='Main'), '/images/ps0010.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Blue Elephant Storage Tub'), (select id from product.image_type where type='Main'), '/images/ps0024.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Grey Star Laundry Bag'), (select id from product.image_type where type='Main'), '/images/ps0023.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Pink Crown Sleepsuit'), (select id from product.image_type where type='Main'), '/images/ps0011.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Blue Splash Print Sleepsuit'), (select id from product.image_type where type='Main'), '/images/ps0012.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Angel Wings Sleepsuit - Pink'), (select id from product.image_type where type='Main'), '/images/ps0013.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Pink And White Checked Pyjamas'), (select id from product.image_type where type='Main'), '/images/ps0014.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Blue Splash Print 3 Pack of Bandana Bibs'), (select id from product.image_type where type='Main'), '/images/ps0015.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Grey Star Laundry Bag'), (select id from product.image_type where type='Main'), '/images/ps0016.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Green & Grey Round Table and Stool Set'), (select id from product.image_type where type='Main'), '/images/ps0017.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised White Cloud Night Light'), (select id from product.image_type where type='Main'), '/images/ps0018.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='4 Pack Personalised Bear Socks'), (select id from product.image_type where type='Main'), '/images/ps0019.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), (select id from product.image_type where type='Main'), '/images/ps0020.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe - Blue'), (select id from product.image_type where type='Main'), '/images/ps0025.jpg', 1);

insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Pink Elephant Storage Tub'), (select id from product.image_type where type='Color'), '/images/ps0010_color.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Blue Elephant Storage Tub'), (select id from product.image_type where type='Color'), '/images/ps0024_color.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Grey Star Laundry Bag'), (select id from product.image_type where type='Color'), '/images/ps0023_color.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), (select id from product.image_type where type='Color'), '/images/ps0020_color.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe - Blue'), (select id from product.image_type where type='Color'), '/images/ps0025_color.jpg', 1);

insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='White Christening Photo Frame'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Ditsy Print Backpack'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Blue Ditsy Storage Bag'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Star Toy Chest'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Mini White Star Storage Bag'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Large Pink Stripe and Star Storage Box'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='White Personalised Rocking Chair'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Blue Slogan Bodysuit - I Heart Daddy'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Good Looks Bodysuit'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Pink Elephant Storage Tub'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Blue Elephant Storage Tub'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Grey Star Laundry Bag'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Pink Crown Sleepsuit'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Blue Splash Print Sleepsuit'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Angel Wings Sleepsuit - Pink'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Pink And White Checked Pyjamas'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Blue Splash Print 3 Pack of Bandana Bibs'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Grey Star Laundry Bag'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Green & Grey Round Table and Stool Set'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised White Cloud Night Light'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='4 Pack Personalised Bear Socks'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe - Blue'), (select id from product.image_type where type='Detail'), '/images/ps0021.jpg', 1);

insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='White Christening Photo Frame'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Ditsy Print Backpack'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Blue Ditsy Storage Bag'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Star Toy Chest'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Mini White Star Storage Bag'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Large Pink Stripe and Star Storage Box'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='White Personalised Rocking Chair'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Blue Slogan Bodysuit - I Heart Daddy'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Good Looks Bodysuit'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Pink Elephant Storage Tub'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Blue Elephant Storage Tub'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Grey Star Laundry Bag'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 1);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Pink Crown Sleepsuit'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Blue Splash Print Sleepsuit'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Angel Wings Sleepsuit - Pink'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Pink And White Checked Pyjamas'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised Blue Splash Print 3 Pack of Bandana Bibs'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Grey Star Laundry Bag'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Green & Grey Round Table and Stool Set'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='Personalised White Cloud Night Light'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='4 Pack Personalised Bear Socks'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);
insert into product.product_image(product_id, image_type_id, url, ordering) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe - Blue'), (select id from product.image_type where type='Detail'), '/images/ps0022.jpg', 2);

--product_group
insert into product.product_group(product_group, type, product_id) values (1, 'Color', (select id from product.product where name='Pink Elephant Storage Tub'));
insert into product.product_group(product_group, type, product_id) values (1, 'Color', (select id from product.product where name='Blue Elephant Storage Tub'));
insert into product.product_group(product_group, type, product_id) values (1, 'Color', (select id from product.product where name='Personalised Grey Star Laundry Bag'));
insert into product.product_group(product_group, type, product_id) values (2, 'Color', (select id from product.product where name='CiCi Personalised Pink Gingham Robe'));
insert into product.product_group(product_group, type, product_id) values (2, 'Color', (select id from product.product where name='CiCi Personalised Pink Gingham Robe - Blue'));

--skus
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='White Christening Photo Frame'), 'sku00001', 10, 9.9);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Ditsy Print Backpack'), 'sku00002', 10, 2.99);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Blue Ditsy Storage Bag'), 'sku00003', 10, 3.99);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Personalised Star Toy Chest'), 'sku00004', 10, 4.99);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Mini White Star Storage Bag'), 'sku00005', 10, 5.99);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Large Pink Stripe and Star Storage Box'), 'sku00006', 10, 6.99);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='White Personalised Rocking Chair'), 'sku00007', 10, 7.99);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Blue Slogan Bodysuit - I Heart Daddy'), 'sku00008', 10, 8.99);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Good Looks Bodysuit'), 'sku00009', 10, 9.99);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Pink Elephant Storage Tub'), 'sku00010', 10, 10.45);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Blue Elephant Storage Tub'), 'sku00039', 10, 10.45);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Personalised Grey Star Laundry Bag'), 'sku00040', 10, 10.45);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Personalised Pink Crown Sleepsuit'), 'sku00011', 10, 11);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Personalised Blue Splash Print Sleepsuit'), 'sku00012', 10, 3);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Personalised Angel Wings Sleepsuit - Pink'), 'sku00013', 10, 4);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Personalised Pink And White Checked Pyjamas'), 'sku00014', 10, 3.4);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Personalised Blue Splash Print 3 Pack of Bandana Bibs'), 'sku00015', 10, 5.4);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Grey Star Laundry Bag'), 'sku00016', 10, 9.3);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Green & Grey Round Table and Stool Set'), 'sku00017', 10, 9.1);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='Personalised White Cloud Night Light'), 'sku00018', 10, 1.99);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='4 Pack Personalised Bear Socks'), 'sku00019', 5, 8.5);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='4 Pack Personalised Bear Socks'), 'sku00028', 10, 9);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='4 Pack Personalised Bear Socks'), 'sku00029', 15, 9.5);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='4 Pack Personalised Bear Socks'), 'sku00030', 20, 10);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00020', 1, 8.5);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00021', 2, 8);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00022', 3, 8.5);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00023', 4, 8);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00024', 5, 8.5);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00025', 6, 8);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00026', 7, 8.5);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00027', 8, 8);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00031', 9, 8.5);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00032', 10, 8);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00033', 11, 8.5);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00034', 12, 8);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00035', 13, 8.5);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00036', 14, 8);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00037', 15, 8.5);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe'), 'sku00038', 16, 8);
insert into product.sku(product_id, sku, stock_quantity, price) values ((select id from product.product where name='CiCi Personalised Pink Gingham Robe - Blue'), 'sku00041', 16, 8);

insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00019'), (select min(id) from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00028'), (select min(id)+1 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00029'), (select min(id)+2 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00030'), (select min(id)+3 from product.attribute_value));

insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00020'), (select min(id)+4 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00020'), (select min(id)+8 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00021'), (select min(id)+4 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00021'), (select min(id)+9 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00022'), (select min(id)+4 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00022'), (select min(id)+10 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00023'), (select min(id)+4 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00023'), (select min(id)+11 from product.attribute_value));

insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00024'), (select min(id)+5 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00024'), (select min(id)+8 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00025'), (select min(id)+5 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00025'), (select min(id)+9 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00026'), (select min(id)+5 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00026'), (select min(id)+10 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00027'), (select min(id)+5 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00027'), (select min(id)+11 from product.attribute_value));

insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00031'), (select min(id)+6 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00031'), (select min(id)+8 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00032'), (select min(id)+6 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00032'), (select min(id)+9 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00033'), (select min(id)+6 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00033'), (select min(id)+10 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00034'), (select min(id)+6 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00034'), (select min(id)+11 from product.attribute_value));

insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00035'), (select min(id)+7 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00035'), (select min(id)+8 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00036'), (select min(id)+7 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00036'), (select min(id)+9 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00037'), (select min(id)+7 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00037'), (select min(id)+10 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00038'), (select min(id)+7 from product.attribute_value));
insert into product.sku_attribute_value (sku_id, attribute_value_id) values ((select id from product.sku where sku = 'sku00038'), (select min(id)+11 from product.attribute_value));


update product.product set description = '<p><span style="font-size: 1.5em;"><b>Details</b></span></p>
<p>Scary dreams are no fun! Luckily, the brontosaurus on this soft duvet cover chases away all the bedroom monsters with his loud ''GRRR''. The blue duvet cover with little white bones all over suits every cool kid’s room and goes really well with our other brontosaurus products.</p>
<p><span style="font-size: medium;"><b>Specifications</b></span></p>
<ul>
<li>Size duvet cover: 140 x 200 cm*&nbsp;</li>
<li>Size pillow case:&nbsp;60 x 70 cm</li>
<li>Material: 100% cotton</li>
<li>Oeko-tex certified</li>
</ul>
<p><span style="font-size: x-small;">*Note: the duvet cover is slightly bigger than the actual size because the material can shrink up to 5%.</span></p>';
