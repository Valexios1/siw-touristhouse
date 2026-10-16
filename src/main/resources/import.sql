-- Account amministratore di default
-- username: admin
-- password: admin (codificata con BCrypt, lo stesso encoder usato dall'applicazione)
-- NOTA: Hibernate esegue import.sql una riga per volta, quindi ogni statement DEVE stare su una singola riga.

INSERT INTO users (id, name, surname, email, phone_number, nation) VALUES (1, 'Admin', 'Admin', 'admin@touristhouse.it', NULL, NULL);
INSERT INTO credentials (id, username, password, role, user_id) VALUES (1, 'admin', '$2a$10$CXiH0dLZy5pNCV4m9rlItOQnN2PoKQh5M.19J/42HEVG.N7HwMOV.', 'ADMIN', 1);

-- Allinea le sequenze Hibernate all'id appena inserito, cosi' i successivi inserimenti non collidono.
SELECT setval('users_seq', 1, true);
SELECT setval('credentials_seq', 1, true);

-- Camere di esempio (id omesso: la colonna e' IDENTITY, quindi la genera il database)
-- Camera con superficie definita
INSERT INTO camera (numero_camera, tipologia, capienza_massima, prezzo_base_notte, superficie_mq, stato_attuale) VALUES (101, 'DOPPIA', 2, 80.0, 25, 'ATTIVA');
-- Camera senza superficie (superficie_mq NULL)
INSERT INTO camera (numero_camera, tipologia, capienza_massima, prezzo_base_notte, superficie_mq, stato_attuale) VALUES (102, 'MATRIMONIALE', 3, 95.0, NULL, 'ATTIVA');
