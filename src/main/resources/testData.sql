-- empty if exists
DELETE FROM executors WHERE order_id > 0;
DELETE FROM order_commercial_proposals_mapping WHERE order_id > 0;
DELETE FROM orders WHERE id > 0;
DELETE FROM review WHERE id > 0;
DELETE FROM address WHERE id > 0;
DELETE FROM user WHERE id > 0;
DELETE FROM commercial_proposal WHERE id > 0;

-- add user
INSERT INTO user (id, name, surname, patronymic, password, email, phone_number, role)
VALUES (1, 'admin', 'admin', 'admin', '$2a$10$6DI5oh7MbZX7DSkdHOfdlOc6GXj2gH8Qgyo5VCmuldGnAkEMlo3GO', 'admin', '+380930000000', 'Admin');

INSERT INTO user (id, name, surname, password, email, phone_number) -- password: Qw3rty*
VALUES (2, 'Sergii', 'Right', '$2a$10$lFW0pKbU24UkSlBFoANN0uE/FETJJCf66iDUOMZS8JYgmgeVvx6L2', 'e@mail.com', '+380931234567');

INSERT INTO user (id, name, surname, patronymic, password, email) -- password: P4ssw()rd
VALUES (3, 'Petro', 'Vovk', 'Petrovych', '$2a$10$3ezDfbsXuVb817/MgR9D5e2ERNHZDckq/0kqx1SwWHnYTdnSmZz7y', 'p@outlook.com');

INSERT INTO user (id, name, password, email) -- password: password
VALUES (4, 'Pavlo', '$2a$10$khRH0cGfqeo6S8uux6o.suCG32m1qxxj60mP3m7eIK3ibWjkB4nXW', 'p@gmail.com');

INSERT INTO user (id, name, surname, password, email) -- password: us3r
VALUES (5, 'User', 'US', '$2a$10$jt6bt5yQowuPz.W0KFvqu.Q1LdJpl0C0nRaTd2VQkby194BitHoBO', 'user@gmail.com');

INSERT INTO user (id, name, surname, patronymic, password, email, phone_number) -- password: qwerty
VALUES (6, 'Ivan', 'Ivanov', 'Ivanovych', '$2a$10$mLur1uQN0ZRORjvPMAo.OeBOYsTbz4h3fp/hOEoaWvBEiiNDr/5S2', 'i@i.ua', '+380951234567');

-- add employee
INSERT INTO user (id, name, surname, patronymic, password, email, phone_number, role) -- password: 123456
VALUES (7, 'Olga', 'Odina', 'Olegivna', '$2a$10$1vHYoD9tQQdlFhEp8u4eYusYMC8Di5AQs.LkjhE9.iSF0CF1.fdAO', 'olga@cleaner.ua', '+380971234567', 'Employee');

INSERT INTO user (id, name, surname, password, email, phone_number, role) -- password: exec
VALUES (8, 'Hannah', 'Montana', '$2a$10$A9yk6sVHrAlTrHLJNboWN.udLs1RqgqCnEFaSa9JN.lB5eJnP.4Z2', 'hannah@montana.com', '+380981234567', 'Employee');

INSERT INTO user (id, name, password, email, phone_number, role) -- password: exec
VALUES (9, 'Zendaya', '$2a$10$A9yk6sVHrAlTrHLJNboWN.udLs1RqgqCnEFaSa9JN.lB5eJnP.4Z2', 'me@zendaya.com', '+380901234567', 'Employee');

-- add commercial_proposal
INSERT INTO commercial_proposal (id, name, short_description, full_description, count_of_employee, duration, price, type) -- 15m/900s (+000000000)
VALUES (1, 'Подушка велика', 'Велика подушка з легкоочищуваного матеріалу', 'Подушка 70х70 з екопуху/пуху/бамбуку, без глибоких складних забруднень, без пошкоджень, що вимагають делікатної чистки. Білосніжні подушки не підпадають у цю категорію.', 1, 900000000000, 220, 'PER_ITEM');

INSERT INTO commercial_proposal (id, name, short_description, full_description, count_of_employee, duration, price, type) -- 30m/1800s
VALUES (2, 'Подушка велика+', 'Білосніжні або шовкові подушки', 'Подушка 70х70 з шовку, без глибоких складних забруднень, без пошкоджень, що вимагають делікатної чистки. Білосніжні подушки підпадають у цю категорію.', 1, 1800000000000, 570, 'PER_ITEM');

INSERT INTO commercial_proposal (id, name, short_description, full_description, count_of_employee, duration, price, type) -- 1h/3600s
VALUES (3, 'Диван середній', 'Диван 200х100х70', 'Диван 200х100х70 з легкоочищуваного матеріалу, без глибоких складних забруднень, без пошкоджень, що вимагають делікатної чистки. Білосніжні дивани не підпадають у цю категорію.', 2, 3600000000000, 1100, 'PER_ITEM');

INSERT INTO commercial_proposal (id, name, short_description, full_description, count_of_employee, duration, price, type) -- 1h/3600s
VALUES (4, 'Диван середній+', 'Диван 200х100х70 з складних матеріалів', 'Диван 200х100х70 з шовку, без глибоких складних забруднень, без пошкоджень, що вимагають делікатної чистки. Білосніжні дивани підпадають у цю категорію.', 2, 3600000000000, 1500, 'PER_ITEM');

INSERT INTO commercial_proposal (id, name, short_description, full_description, count_of_employee, duration, price, type) -- 3h/10800s
VALUES (5, 'Офіс (плитка)', 'Вологе прибирання офісу', 'Офіс з кам\яною підлогою або плиткою, без складних забруднень, до 3 санвузлів, прибирання коли офіс пустий.', 4, 10800000000000, 200, 'PER_AREA'); 

INSERT INTO commercial_proposal (id, name, short_description, full_description, count_of_employee, duration, price, type) -- 3h/10800s
VALUES (6, 'Офіс (паркет)', 'Вологе прибирання офісу', 'Офіс з паркетом, без складних забруднень, до 2 санвузлів, прибирання коли офіс пустий.', 6, 10800000000000, 250, 'PER_AREA');

INSERT INTO commercial_proposal (id, name, short_description, full_description, count_of_employee, duration, price, type) -- 3h/10800s
VALUES (7, 'Ремонт', 'Вологе прибирання після ремонту', 'Прибирання після ремонту – фінальна точка перед приїздом на місце нову квартиру. Навіть якщо ви проводили косметичний ремонт, на меблях, техніці, підлозі та інших поверхнях міг залишитися будівельний пил, від якого складно позбутися самостійно.', 3, 10800000000000, 110, 'PER_AREA');

INSERT INTO commercial_proposal (id, name, short_description, full_description, count_of_employee, duration, price, type) -- 30m/1800s
VALUES (8, 'Вікна', 'Миття вікон', 'Миття вікон, віконних рам, віконних жалюзі, віконних решіток, віконних москітних сіток, підвіконь', 1, 1800000000000, 170, 'PER_AREA');

-- add address
INSERT INTO address (id, city, street, house_number, flat_number, zip, user_id, deleted)
VALUES (1, 'Київ', 'Хрещатик', '1Д', '1', '01001', 2, false);

INSERT INTO address (id, city, street, house_number, flat_number, zip, user_id, deleted)
VALUES (2, 'Київ', 'Гончаренка', '10Г', '12', '02345', 2, false);

INSERT INTO address (id, city, street, house_number, zip, user_id, deleted)
VALUES (3, 'Львів', 'Петлюри', '10', '01231', 2, true);

INSERT INTO address (id, city, street, house_number, flat_number, zip, user_id, deleted)
VALUES (4, 'Київ', 'Лумумби', '10', '40К', '01671', 2, true);

INSERT INTO address (id, city, street, house_number, flat_number, zip, user_id, deleted)
VALUES (5, 'Дніпро', 'Тичини', '10В', '15К', '01501', 3, false);

-- add order reviews
INSERT INTO review (id, cleaning_rate, employee_rate, details)
VALUES (1, 5, 5, 'Все було ідеально');

INSERT INTO review (id, cleaning_rate, employee_rate, details)
VALUES (2, 4, 5, 'Все було добре');

INSERT INTO review (id, cleaning_rate, employee_rate, details)
VALUES (3, 2, 1, 'Все було жахливо. Хамське выдношення та погана якість прибирання');

-- add orders 
INSERT INTO orders (id, duration, price, address, client, creation_time, order_time, review, status)
VALUES (1, 3650000000000, 1100, 5, 2, '2023-8-22 18:00', '2023-9-1 12:00', 2, 'DONE');

INSERT INTO orders (id, duration, price, address, client, creation_time, order_time, review, comment, status)
VALUES (2, 3100000000000, 1000, 3, 2, '2023-9-22 18:00', '2023-9-27 12:00', 3, 'Нікого не буде вдома під час прибирання', 'DONE');

INSERT INTO orders (id, duration, price, address, client, creation_time, order_time, review, comment, status)
VALUES (3, 3600000000000, 1900, 4, 2, '2023-10-22 18:00', '2023-10-27 12:00', 1, '-', 'DONE');

INSERT INTO orders (id, duration, price, address, client, creation_time, order_time, comment, status)
VALUES (4, 3600000000000, 1900, 4, 2, NOW(), '2024-1-1 15:00', 'Не чистити подушки', 'NOT_VERIFIED');

-- add order_commercial_proposals_mapping
INSERT INTO order_commercial_proposals_mapping (order_id, commercial_proposal_id, quantity)
VALUES (1, 1, 2);

INSERT INTO order_commercial_proposals_mapping (order_id, commercial_proposal_id, quantity)
VALUES (1, 2, 2);

INSERT INTO order_commercial_proposals_mapping (order_id, commercial_proposal_id, quantity)
VALUES (1, 3, 6);

INSERT INTO order_commercial_proposals_mapping (order_id, commercial_proposal_id, quantity)
VALUES (2, 4, 7);

INSERT INTO order_commercial_proposals_mapping (order_id, commercial_proposal_id, quantity)
VALUES (2, 5, 1);

INSERT INTO order_commercial_proposals_mapping (order_id, commercial_proposal_id, quantity)
VALUES (2, 6, 9);

INSERT INTO order_commercial_proposals_mapping (order_id, commercial_proposal_id, quantity)
VALUES (3, 7, 27);

INSERT INTO order_commercial_proposals_mapping (order_id, commercial_proposal_id, quantity)
VALUES (3, 8, 7);

INSERT INTO order_commercial_proposals_mapping (order_id, commercial_proposal_id, quantity)
VALUES (4, 3, 2);

INSERT INTO order_commercial_proposals_mapping (order_id, commercial_proposal_id, quantity)
VALUES (4, 8, 4);

-- executors
INSERT INTO executors (order_id, user_id)
VALUES (1, 7);

INSERT INTO executors (order_id, user_id)
VALUES (1, 8);

INSERT INTO executors (order_id, user_id)
VALUES (1, 9);

INSERT INTO executors (order_id, user_id)
VALUES (2, 7);

INSERT INTO executors (order_id, user_id)
VALUES (2, 8);

INSERT INTO executors (order_id, user_id)
VALUES (3, 9);

INSERT INTO executors (order_id, user_id)
VALUES (4, 8);
