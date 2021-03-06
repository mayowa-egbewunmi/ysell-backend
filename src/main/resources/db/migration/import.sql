--USERS
INSERT INTO USERS (id, name, email, hash, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b31'), 'Tobenna', 'chineketobenna@gmail.com', '$2a$10$4a0pl5Twxk4spAux0oN/4ezUxm4XopO./00/t55j6LC93bHZejExu', UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));
INSERT INTO USERS (id, name, email, hash, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b32'), 'Mayor', 'mayowaegbewunmi@gmail.com', '$2a$10$4a0pl5Twxk4spAux0oN/4ezUxm4XopO./00/t55j6LC93bHZejExu', UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));
INSERT INTO USERS (id, name, email, hash, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b33'), 'YSell', 'ysell@fake.com', '$2a$10$FOxS/aqf9VoKyeJVmVjUleiOsI1LKixsH2vFtG6kKW4Cu36ah5tLC', UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));

--ORGANISATIONS
insert into organisations (id, name, email, address, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b31'), 'Nexus Alliance Limited', '289B Corporation Drive', 'nexus@yahoo.com', UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));
insert into organisations (id, name, email, address, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b32'), 'Test Corporation', 'Test Corporation Address', 'testcorporation@yahoo.com', UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));

--USERS_ORGANISATIONS
INSERT INTO USERS_ORGANISATIONS VALUES (UNHEX('1b63e5256162429e96233c8a56045b31'), UNHEX('1b63e5256162429e96233c8a56045b31'));
INSERT INTO USERS_ORGANISATIONS VALUES (UNHEX('1b63e5256162429e96233c8a56045b32'), UNHEX('1b63e5256162429e96233c8a56045b32'));

--PRODUCT CATEGORIES
insert into PRODUCT_CATEGORIES (id, name, description, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b31'), 'Snacks', 'Wonderful Snacky Food', UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));
insert into PRODUCT_CATEGORIES (id, name, description, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b32'), 'Cooked Food', 'Wonderful Prepared Meal', UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));

--PRODUCT
insert into PRODUCTS (id, name, description, current_stock, cost_price, selling_price, organisation_id, product_category_id, created_by, updated_by)
values (UNHEX('1b63e5256162429e96233c8a56045b31'), 'Bread', 'Edible soft bread', 50, 80, 100, UNHEX('1b63e5256162429e96233c8a56045b31'), UNHEX('1b63e5256162429e96233c8a56045b31'), UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));
insert into PRODUCTS (id, name, description, current_stock, cost_price, selling_price, organisation_id, product_category_id, created_by, updated_by)
values (UNHEX('1b63e5256162429e96233c8a56045b32'), 'Ewa Goin', 'Wonderful local beans', 10, 50, 100, UNHEX('1b63e5256162429e96233c8a56045b31'), UNHEX('1b63e5256162429e96233c8a56045b32'), UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));
insert into PRODUCTS (id, name, description, current_stock, cost_price, selling_price, organisation_id, product_category_id, created_by, updated_by)
values (UNHEX('1b63e5256162429e96233c8a56045b33'), 'Bread', 'Wonderful soft bread', 50, 80, 100, UNHEX('1b63e5256162429e96233c8a56045b32'), UNHEX('1b63e5256162429e96233c8a56045b31'), UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));
insert into PRODUCTS (id, name, description, current_stock, cost_price, selling_price, organisation_id, product_category_id, created_by, updated_by)
values (UNHEX('1b63e5256162429e96233c8a56045b34'), 'Rice', 'Sweet edible rice', 20, 20, 100, UNHEX('1b63e5256162429e96233c8a56045b32'), UNHEX('1b63e5256162429e96233c8a56045b32'), UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));

--STOCK
insert into STOCKS (id, product_id, quantity, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b31'), UNHEX('1b63e5256162429e96233c8a56045b31'), 50, UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));
insert into STOCKS (id, product_id, quantity, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b32'), UNHEX('1b63e5256162429e96233c8a56045b32'), 10, UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));
insert into STOCKS (id, product_id, quantity, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b33'), UNHEX('1b63e5256162429e96233c8a56045b33'), 50, UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));
insert into STOCKS (id, product_id, quantity, created_by, updated_by) VALUES (UNHEX('1b63e5256162429e96233c8a56045b34'), UNHEX('1b63e5256162429e96233c8a56045b34'), 20, UNHEX('d41d8cd98f003204a9800998ecf8427e'), UNHEX('d41d8cd98f003204a9800998ecf8427e'));