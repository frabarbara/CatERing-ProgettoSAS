DELETE FROM MenuItems WHERE true;
DELETE FROM MenuSections WHERE true;
DELETE FROM MenuFeatures WHERE true;
DELETE FROM Menus WHERE true;
DELETE FROM scheduled_tasks WHERE true;
DELETE FROM tasks WHERE true;
DELETE FROM Services WHERE true;
DELETE FROM cooks_availabilities WHERE true;
DELETE FROM turns WHERE true;

/* FILL Menus */
LOCK TABLES `Menus` WRITE;
/*!40000 ALTER TABLE `Menus`
    DISABLE KEYS */;
INSERT INTO `Menus`
VALUES (80, 'Coffee break mattutino', 2, 1),
       (82, 'Coffee break pomeridiano', 2, 1),
       (86, 'Cena di compleanno pesce', 3, 1);
/*!40000 ALTER TABLE `Menus`
    ENABLE KEYS */;
UNLOCK TABLES;

/* Fill MenuSections */
LOCK TABLES `MenuSections` WRITE;
/*!40000 ALTER TABLE `MenuSections`
    DISABLE KEYS */;
INSERT INTO `MenuSections`
VALUES (41, 86, 'Antipasti', 0),
       (42, 86, 'Primi', 1),
       (43, 86, 'Secondi', 2),
       (44, 86, 'Dessert', 3);
/*!40000 ALTER TABLE `MenuSections`
    ENABLE KEYS */;
UNLOCK TABLES;

/* Fill MenuItems */
LOCK TABLES `MenuItems` WRITE;
/*!40000 ALTER TABLE `MenuItems`
    DISABLE KEYS */;
INSERT INTO `MenuItems`
VALUES (96, 80, 0, 'Croissant vuoti', 9, 0),
       (97, 80, 0, 'Croissant alla marmellata', 9, 1),
       (98, 80, 0, 'Pane al cioccolato mignon', 10, 2),
       (99, 80, 0, 'Panini al latte con prosciutto crudo', 12, 4),
       (100, 80, 0, 'Panini al latte con prosciutto cotto', 12, 5),
       (101, 80, 0, 'Panini al latte con formaggio spalmabile alle erbe', 12, 6),
       (102, 80, 0, 'Girelle all\'uvetta mignon', 11, 3),
       (103, 82, 0, 'Biscotti', 13, 1),
       (104, 82, 0, 'Lingue di gatto', 14, 2),
       (105, 82, 0, 'Bigné alla crema', 15, 3),
       (106, 82, 0, 'Bigné al caffè', 15, 4),
       (107, 82, 0, 'Pizzette', 16, 5),
       (108, 82, 0, 'Croissant al prosciutto crudo mignon', 9, 6),
       (109, 82, 0, 'Tramezzini tonno e carciofini mignon', 17, 7),
       (112, 86, 41, 'Vitello tonnato', 1, 0),
       (113, 86, 41, 'Carpaccio di spada', 2, 1),
       (114, 86, 41, 'Alici marinate', 3, 2),
       (115, 86, 42, 'Penne alla messinese', 5, 0),
       (116, 86, 42, 'Risotto alla zucca', 20, 1),
       (117, 86, 43, 'Salmone al forno', 8, 0),
       (118, 86, 44, 'Sorbetto al limone', 18, 0),
       (119, 86, 44, 'Torta Saint Honoré', 19, 1);
/*!40000 ALTER TABLE `MenuItems`
    ENABLE KEYS */;
UNLOCK TABLES;

/* Fill MenuFeatures */
LOCK TABLES `MenuFeatures` WRITE;
/*!40000 ALTER TABLE `MenuFeatures`
    DISABLE KEYS */;
INSERT INTO `MenuFeatures`
VALUES (80, 'Richiede cuoco', 0),
       (80, 'Buffet', 0),
       (80, 'Richiede cucina', 0),
       (80, 'Finger food', 0),
       (80, 'Piatti caldi', 0),
       (82, 'Richiede cuoco', 0),
       (82, 'Buffet', 0),
       (82, 'Richiede cucina', 0),
       (82, 'Finger food', 0),
       (82, 'Piatti caldi', 0),
       (86, 'Richiede cuoco', 0),
       (86, 'Buffet', 0),
       (86, 'Richiede cucina', 0),
       (86, 'Finger food', 0),
       (86, 'Piatti caldi', 0);
/*!40000 ALTER TABLE `MenuFeatures`
    ENABLE KEYS */;
UNLOCK TABLES;

/* FIll Services */
LOCK TABLES `Services` WRITE;
/*!40000 ALTER TABLE `Services`
    DISABLE KEYS */;
INSERT INTO `Services`
VALUES (1, 2, 'Cena', 86, 0, '2020-08-13', '20:00:00', '23:30:00', 25),
       (2, 1, 'Coffee break mattino', 0, 80, '2020-09-25', '10:30:00', '11:30:00', 100),
       (3, 1, 'Colazione di lavoro', 0, 0, '2020-09-25', '13:00:00', '14:00:00', 80),
       (4, 1, 'Coffee break pomeriggio', 0, 82, '2020-09-25', '16:00:00', '16:30:00', 100),
       (5, 1, 'Cena sociale', 0, 0, '2020-09-25', '20:00:00', '22:30:00', 40),
       (6, 3, 'Pranzo giorno 1', 0, 0, '2020-10-02', '12:00:00', '15:00:00', 200),
       (7, 3, 'Pranzo giorno 2', 0, 0, '2020-10-03', '12:00:00', '15:00:00', 300),
       (8, 3, 'Pranzo giorno 3', 0, 0, '2020-10-04', '12:00:00', '15:00:00', 400);
/*!40000 ALTER TABLE `Services`
    ENABLE KEYS */;
UNLOCK TABLES;

/* FILL turns */
LOCK TABLES `turns` WRITE;
ALTER TABLE `turns` AUTO_INCREMENT = 0;
INSERT INTO `turns` (feedback, capacity)
VALUES ('Prof meritiamo il 30', 10),
       ('', 5),
       ('', 20);
UNLOCK TABLES;

/* FILL tasks */
LOCK TABLES `tasks` WRITE;
ALTER TABLE `tasks` AUTO_INCREMENT = 0;
INSERT INTO tasks (service_id, recipe_id, qty, position)
VALUES (6, 5, 10, 0);
UNLOCK TABLES;

/* FILL cooks_availabilities */
LOCK TABLES `cooks_availabilities` WRITE;
INSERT INTO catering.cooks_availabilities (cook_id, turn_id)
VALUES (4, 2),
       (4, 1),
       (5, 2),
       (6, 1);
UNLOCK TABLES;