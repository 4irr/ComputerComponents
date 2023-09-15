delete from acceptance;
delete from placement;
delete from sale;
delete from clearance;
delete from user;
delete from accessories;

insert into user(userid, username, password, dtype, is_blocked) values
(1, 'admin', '$2a$10$3j45L2zunr/d8eViTVnM5.aUTF0PyxloyMKKI0aVJ9O.59s8FSGP.', 'Admin', 0),
(2, 'user1', '$2a$10$3j45L2zunr/d8eViTVnM5.aUTF0PyxloyMKKI0aVJ9O.59s8FSGP.', 'Worker', 0);

insert into accessories(accessoriesid, cell, name, price, type, weight) values
(10, 'A1', 'Процессор', 1500, 'Процессоры', '0.3'),
(11, 'A2', 'Видеокарта', 2500, 'Видеокарты', '1.3'),
(12, 'A3', 'ОЗУ', 500, 'ОЗУ' , '0.2');

insert into clearance(operation_id, date, type, user_userid) values
(10, '2023.05.07', 'Прибытие', '1'),
(11, '2023.05.07', 'Размещение', '1'),
(12, '2023.05.07', 'Отпуск', '1');

insert into acceptance(sender, operation_id, accessories_accessoriesid) values
('ООО ИКС', 10, 11);

insert into placement(operation_id, accessories_accessoriesid) values
    (11, 11);

insert into sale(operation_id, accessories_accessoriesid) values
    (12, 11);