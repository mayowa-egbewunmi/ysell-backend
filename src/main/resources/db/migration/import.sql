--USERS
INSERT INTO USERS (id, name, email, hash) VALUES ('1b63e5256162429e96233c8a56045b31', 'Tobenna', 'chineketobenna@gmail.com', '$2a$10$4a0pl5Twxk4spAux0oN/4ezUxm4XopO./00/t55j6LC93bHZejExu');
INSERT INTO USERS (id, name, email, hash) VALUES ('1b63e5256162429e96233c8a56045b32', 'Mayor', 'mayowaegbewunmi@gmail.com', '$2a$10$4a0pl5Twxk4spAux0oN/4ezUxm4XopO./00/t55j6LC93bHZejExu');
INSERT INTO USERS (id, name, email, hash) VALUES ('1b63e5256162429e96233c8a56045b33', 'YSell', 'ysell@fake.com', '$2a$10$FOxS/aqf9VoKyeJVmVjUleiOsI1LKixsH2vFtG6kKW4Cu36ah5tLC');

--ORGANISATIONS
insert into organisations (id, name, email, address) values ('1b63e5256162429e96233c8a56045b31', 'Nexus Alliance Limited', '289B Corporation Drive', 'nexus@yahoo.com');
insert into organisations (id, name, email, address) values ('1b63e5256162429e96233c8a56045b32', 'Test Corporation', 'Test Corporation Address', 'testcorporation@yahoo.com');

--USERS_ORGANISATIONS
INSERT INTO USERS_ORGANISATIONS VALUES (1, 1);
INSERT INTO USERS_ORGANISATIONS VALUES (2, 2);

--PRODUCT CATEGORIES
insert into PRODUCT_CATEGORIES (id, name, description) values ('1b63e5256162429e96233c8a56045b31', 'Snacks', 'Wonderful Snacky Food');
insert into PRODUCT_CATEGORIES (id, name, description) values ('1b63e5256162429e96233c8a56045b32', 'Cooked Food', 'Wonderful Prepared Meal');

--PRODUCT
insert into PRODUCTS (id, name, description, current_stock, cost_price, selling_price, organisation_id, product_category_id)
values ('1b63e5256162429e96233c8a56045b31', 'Bread', 'Edible soft bread', 50, 80, 100, '1b63e5256162429e96233c8a56045b31', '1b63e5256162429e96233c8a56045b31');
insert into PRODUCTS (id, name, description, current_stock, cost_price, selling_price, organisation_id, product_category_id)
values ('1b63e5256162429e96233c8a56045b32', 'Ewa Goin', 'Wonderful local beans', 10, 50, 100, '1b63e5256162429e96233c8a56045b31', '1b63e5256162429e96233c8a56045b32');
insert into PRODUCTS (id, name, description, current_stock, cost_price, selling_price, organisation_id, product_category_id)
values ('1b63e5256162429e96233c8a56045b33', 'Bread', 'Wonderful soft bread', 50, 80, 100, '1b63e5256162429e96233c8a56045b32', '1b63e5256162429e96233c8a56045b31');
insert into PRODUCTS (id, name, description, current_stock, cost_price, selling_price, organisation_id, product_category_id)
values ('1b63e5256162429e96233c8a56045b34', 'Rice', 'Sweet edible rice', 20, 20, 100, '1b63e5256162429e96233c8a56045b32', '1b63e5256162429e96233c8a56045b32');

--STOCK
insert into STOCKS (id, product_id, quantity) values ('1b63e5256162429e96233c8a56045b31', '1b63e5256162429e96233c8a56045b31', 50);
insert into STOCKS (id, product_id, quantity) values ('1b63e5256162429e96233c8a56045b32', '1b63e5256162429e96233c8a56045b32', 10);
insert into STOCKS (id, product_id, quantity) values ('1b63e5256162429e96233c8a56045b33', '1b63e5256162429e96233c8a56045b33', 50);
insert into STOCKS (id, product_id, quantity) values ('1b63e5256162429e96233c8a56045b34', '1b63e5256162429e96233c8a56045b34', 20);