--USERS
INSERT INTO USERS VALUES (1, {ts '2020-08-01 13:45:59.9690'}, 1, null, null,  true, 'chineketobenna@gmail.com', '$2a$10$4a0pl5Twxk4spAux0oN/4ezUxm4XopO./00/t55j6LC93bHZejExu', 'Tobenna');
INSERT INTO USERS VALUES (2, {ts '2020-08-01 13:45:59.9690'}, 1, null, null,  true, 'mayowaegbewunmi@gmail.com', '$2a$10$4a0pl5Twxk4spAux0oN/4ezUxm4XopO./00/t55j6LC93bHZejExu', 'Mayor');
INSERT INTO USERS VALUES (3, {ts '2020-08-01 13:45:59.9690'}, 1, null, null,  true, 'ysell', '$2a$10$FOxS/aqf9VoKyeJVmVjUleiOsI1LKixsH2vFtG6kKW4Cu36ah5tLC', 'YSell');

--ORGANISATIONS
insert into organisations values (1, {ts '2020-08-01 13:45:59.9690'}, 1, null, null, '289B Corporation Drive', 'nexus@yahoo.com', null, 'Nexus Alliance Limited');
insert into organisations values (2, {ts '2020-08-01 13:45:59.9690'}, 1, null, null, 'Test Corporation Address', 'testcorporation@yahoo.com', null, 'Test Corporation');

--USERS_ORGANISATIONS
INSERT INTO USERS_ORGANISATIONS VALUES (1, 1);
INSERT INTO USERS_ORGANISATIONS VALUES (2, 2);

--PRODUCT CATEGORIES
insert into PRODUCT_CATEGORIES values (1, {ts '2020-08-01 13:45:59.9690'}, 1, null, null, 'Wonderful Snacky Food', 'Snacks');
insert into PRODUCT_CATEGORIES values (2, {ts '2020-08-01 13:45:59.9690'}, 1, null, null, 'Wonderful Prepared Meal', 'Cooked Food');

--PRODUCT
insert into PRODUCTS values (1, {ts '2020-08-01 13:45:59.9690'}, 1, null, null, 100, 'Great Loaf', 'Bread', 1000, 1, 1);
insert into PRODUCTS values (2, {ts '2020-08-01 13:45:59.9690'}, 1, null, null, 50, 'Great Ewa Goin', 'Beans', 500, 1, 2);
insert into PRODUCTS values (3, {ts '2020-08-01 13:45:59.9690'}, 1, null, null, 100, 'Great Loaf', 'Bread', 1000, 2, 1);

--STOCK
insert into STOCKS values (1, {ts '2020-08-01 13:45:59.9690'}, 1, null, null, 100, 1);
insert into STOCKS values (2, {ts '2020-08-01 13:45:59.9690'}, 1, null, null, 50, 2);
insert into STOCKS values (3, {ts '2020-08-01 13:45:59.9690'}, 1, null, null, 100, 3);