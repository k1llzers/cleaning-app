INSERT INTO users (id,name,surname,patronymic,email,password,role,phone_number) VALUES (1,'Leonid','Petrenko','Ihorovich','admin','$2a$10$6DI5oh7MbZX7DSkdHOfdlOc6GXj2gH8Qgyo5VCmuldGnAkEMlo3GO','ADMIN','+380930000000');
INSERT INTO users (id,name,surname,patronymic,email,password,role,phone_number) VALUES (2,'Maizie','Burnett','Viktorovivna','m.burnatt@gmail.com','$2a$10$lFW0pKbU24UkSlBFoANN0uE/FETJJCf66iDUOMZS8JYgmgeVvx6L2','USER','+380931234567');
INSERT INTO users (id,name,surname,patronymic,email,password,role,phone_number) VALUES (3,'Chaya','Burnett','Petrivna','c.burnett@outlook.com','$2a$10$3ezDfbsXuVb817/MgR9D5e2ERNHZDckq/0kqx1SwWHnYTdnSmZz7y','EMPLOYEE','+380685812781');
INSERT INTO users (id,name,surname,patronymic,email,password,role,phone_number) VALUES (4,'Bobby','Durham','Ihorovich','b.durman@gmail.com','$2a$10$khRH0cGfqeo6S8uux6o.suCG32m1qxxj60mP3m7eIK3ibWjkB4nXW','USER','+380503215691');
INSERT INTO users (id,name,surname,patronymic,email,password,role,phone_number) VALUES (5,'Micheal','Jacobson','Olegovich','m.jacobs@gmail.com','$2a$10$jt6bt5yQowuPz.W0KFvqu.Q1LdJpl0C0nRaTd2VQkby194BitHoBO','USER','+380521785665');
INSERT INTO users (id,name,surname,patronymic,email,password,role,phone_number) VALUES (6,'Kallum','Charles','Ivanovna','k.charles@i.ua','$2a$10$mLur1uQN0ZRORjvPMAo.OeBOYsTbz4h3fp/hOEoaWvBEiiNDr/5S2','EMPLOYEE','+380951234567');
INSERT INTO users (id,name,surname,patronymic,email,password,role,phone_number) VALUES (7,'Alys','Bonner','Semenivna','a.bonner@gmail.com','$2a$10$khRH0cGfqeo6S8uux6o.suCG32m1qxxj60mP3m7eIK3ibWjkB4nXW','EMPLOYEE','+380679831471');

INSERT INTO commercial_proposals (id,name,short_description,full_description,price,duration,count_of_employee,deleted,type) VALUES (1,'Подушка велика','Велика подушка з легкоочищуваного матеріалу','Подушка 70х70 з екопуху/пуху/бамбуку, без глибоких складних забруднень, без пошкоджень, що вимагають делікатної чистки. Білосніжні подушки не підпадають у цю категорію.',220,900000000000,1,'0','PER_ITEM');
INSERT INTO commercial_proposals (id,name,short_description,full_description,price,duration,count_of_employee,deleted,type) VALUES (2,'Подушка велика+','Білосніжні або шовкові подушки','Подушка 70х70 з шовку, без глибоких складних забруднень, без пошкоджень, що вимагають делікатної чистки. Білосніжні подушки підпадають у цю категорію.',570,1800000000000,1,'0','PER_ITEM');
INSERT INTO commercial_proposals (id,name,short_description,full_description,price,duration,count_of_employee,deleted,type) VALUES (3,'Диван середній','Диван 200х100х70','Диван 200х100х70 з легкоочищуваного матеріалу, без глибоких складних забруднень, без пошкоджень, що вимагають делікатної чистки. Білосніжні дивани не підпадають у цю категорію.',1100,3600000000000,2,'0','PER_ITEM');
INSERT INTO commercial_proposals (id,name,short_description,full_description,price,duration,count_of_employee,deleted,type) VALUES (4,'Диван середній+','Диван 200х100х70 з складних матеріалів','Диван 200х100х70 з шовку, без глибоких складних забруднень, без пошкоджень, що вимагають делікатної чистки. Білосніжні дивани підпадають у цю категорію.',1500,3600000000000,2,'0','PER_ITEM');
INSERT INTO commercial_proposals (id,name,short_description,full_description,price,duration,count_of_employee,deleted,type) VALUES (5,'Офіс (плитка)','Вологе прибирання офісу','Офіс з камяною підлогою або плиткою, без складних забруднень, до 3 санвузлів, прибирання коли офіс пустий.',200,10800000000000,4,'0','PER_AREA');
INSERT INTO commercial_proposals (id,name,short_description,full_description,price,duration,count_of_employee,deleted,type) VALUES (6,'Офіс (паркет)','Вологе прибирання офісу','Офіс з паркетом, без складних забруднень, до 2 санвузлів, прибирання коли офіс пустий.',250,10800000000000,6,'0','PER_AREA');
INSERT INTO commercial_proposals (id,name,short_description,full_description,price,duration,count_of_employee,deleted,type) VALUES (7,'Ремонт','Вологе прибирання після ремонту','Прибирання після ремонту – фінальна точка перед приїздом на місце нову квартиру. Навіть якщо ви проводили косметичний ремонт, на меблях, техніці, підлозі та інших поверхнях міг залишитися будівельний пил, від якого складно позбутися самостійно.',110,10800000000000,3,'0','PER_AREA');
INSERT INTO commercial_proposals (id,name,short_description,full_description,price,duration,count_of_employee,deleted,type) VALUES (8,'Вікна','Миття вікон','Миття вікон, віконних рам, віконних жалюзі, віконних решіток, віконних москітних сіток, підвіконь',170,1800000000000,1,'0','PER_AREA');

INSERT INTO addresses (id,city,street,house_number,flat_number,zip,user_id) VALUES (1,'Київ','Хрещатик','1Д','1','01001',2);
INSERT INTO addresses (id,city,street,house_number,flat_number,zip,user_id) VALUES (2,'Київ','Гончаренка','10Г','12','02345',2);
INSERT INTO addresses (id,city,street,house_number,flat_number,zip,user_id) VALUES (3,'Львів','Петлюри','10',NULL,'01231',NULL);
INSERT INTO addresses (id,city,street,house_number,flat_number,zip,user_id) VALUES (4,'Київ','Лумумби','10','40К','01671',NULL);
INSERT INTO addresses (id,city,street,house_number,flat_number,zip,user_id) VALUES (5,'Дніпро','Тичини','10В','15К','01501',3);

INSERT INTO reviews (id,cleaning_rate,employee_rate,details) VALUES (1,5,5,'Все було ідеально');
INSERT INTO reviews (id,cleaning_rate,employee_rate,details) VALUES (2,4,5,'Все було добре');
INSERT INTO reviews (id,cleaning_rate,employee_rate,details) VALUES (3,2,1,'Все було жахливо. Хамське выдношення та погана якість прибирання');

INSERT INTO orders (id,price,order_time,creation_time,client,comment,address,review,status,duration) VALUES (5,1100,'2023-09-01 12:00:00.000000','2023-08-22 18:00:00.000000',2,NULL,5,2,'DONE',3650000000000);
INSERT INTO orders (id,price,order_time,creation_time,client,comment,address,review,status,duration) VALUES (6,1000,'2023-09-27 12:00:00.000000','2023-09-22 18:00:00.000000',2,'Нікого не буде вдома під час прибирання',3,3,'DONE',3100000000000);
INSERT INTO orders (id,price,order_time,creation_time,client,comment,address,review,status,duration) VALUES (7,1900,'2023-10-27 12:00:00.000000','2023-10-22 18:00:00.000000',3,'-',4,1,'DONE',3600000000000);
INSERT INTO orders (id,price,order_time,creation_time,client,comment,address,review,status,duration) VALUES (8,1900,'2024-01-01 15:00:00.000000','2023-12-13 13:07:05.000000',2,'Не чистити подушки',4,NULL,'NOT_VERIFIED',3600000000000);

INSERT INTO order_commercial_proposals_mapping (commercial_proposal_id,order_id,quantity) VALUES (1,5,2);
INSERT INTO order_commercial_proposals_mapping (commercial_proposal_id,order_id,quantity) VALUES (2,5,2);
INSERT INTO order_commercial_proposals_mapping (commercial_proposal_id,order_id,quantity) VALUES (3,5,6);
INSERT INTO order_commercial_proposals_mapping (commercial_proposal_id,order_id,quantity) VALUES (3,7,2);
INSERT INTO order_commercial_proposals_mapping (commercial_proposal_id,order_id,quantity) VALUES (4,6,7);
INSERT INTO order_commercial_proposals_mapping (commercial_proposal_id,order_id,quantity) VALUES (5,6,1);
INSERT INTO order_commercial_proposals_mapping (commercial_proposal_id,order_id,quantity) VALUES (6,6,9);
INSERT INTO order_commercial_proposals_mapping (commercial_proposal_id,order_id,quantity) VALUES (7,7,27);
INSERT INTO order_commercial_proposals_mapping (commercial_proposal_id,order_id,quantity) VALUES (8,8,7);
INSERT INTO order_commercial_proposals_mapping (commercial_proposal_id,order_id,quantity) VALUES (8,8,4);

INSERT INTO executors (order_id,user_id) VALUES (5,3);
INSERT INTO executors (order_id,user_id) VALUES (6,3);
INSERT INTO executors (order_id,user_id) VALUES (5,6);
INSERT INTO executors (order_id,user_id) VALUES (6,6);
INSERT INTO executors (order_id,user_id) VALUES (8,6);
INSERT INTO executors (order_id,user_id) VALUES (5,7);
INSERT INTO executors (order_id,user_id) VALUES (7,7);
