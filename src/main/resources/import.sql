-- Account amministratore di default
-- username: admin
-- password: admin (codificata con BCrypt, lo stesso encoder usato dall'applicazione)
-- NOTA: Hibernate esegue import.sql una riga per volta, quindi ogni statement DEVE stare su una singola riga.

INSERT INTO users (id, name, surname, email, phone_number, nation) VALUES (1, 'Admin', 'Admin', 'admin@touristhouse.it', NULL, NULL);
INSERT INTO credentials (id, username, password, role, user_id) VALUES (1, 'admin', '$2a$10$CXiH0dLZy5pNCV4m9rlItOQnN2PoKQh5M.19J/42HEVG.N7HwMOV.', 'ADMIN', 1);

-- Utente registrato di default
-- username: user
-- password: user (codificata con BCrypt, lo stesso encoder usato dall'applicazione)
INSERT INTO users (id, name, surname, email, phone_number, nation) VALUES (2, 'User', 'User', 'user@touristhouse.it', NULL, NULL);
INSERT INTO credentials (id, username, password, role, user_id) VALUES (2, 'user', '$2a$10$gUCmdlWeyJ/Hx0nJ/t6QjOePjvkHuTF5i.uAEfKWoNUlbfiQmaAWq', 'DEFAULT', 2);

-- Allinea le sequenze Hibernate all'id appena inserito, cosi' i successivi inserimenti non collidono.
SELECT setval('users_seq', 2, true);
SELECT setval('credentials_seq', 2, true);

-- Camere di esempio (id omesso: la colonna e' IDENTITY, quindi la genera il database)
-- Camera con superficie definita
INSERT INTO camera (numero_camera, tipologia, capienza_massima, prezzo_base_notte, superficie_mq, stato_attuale) VALUES (101, 'DOPPIA', 2, 80.0, 25, 'ATTIVA');
-- Camera senza superficie (superficie_mq NULL)
INSERT INTO camera (numero_camera, tipologia, capienza_massima, prezzo_base_notte, superficie_mq, stato_attuale) VALUES (102, 'MATRIMONIALE', 3, 95.0, NULL, 'ATTIVA');

-- Regole di prezzo di esempio (id omesso: la colonna e' IDENTITY, quindi la genera il database)
INSERT INTO regola_prezzo (nome, data_inizio, data_fine, variazione_prezzo, minimo_notti_soggiorno) VALUES ('Alta stagione estiva', '2027-06-01', '2027-09-15', '+25%', 3);
INSERT INTO regola_prezzo (nome, data_inizio, data_fine, variazione_prezzo, minimo_notti_soggiorno) VALUES ('Bassa stagione invernale', '2027-01-10', '2027-03-31', '-15%', 2);

-- Associazione Camera <-> RegolaPrezzo (tabella di join generata da @ManyToMany: camere id 1 e 2, regole id 1 e 2)
INSERT INTO camera_regole_prezzo (camere_id, regole_prezzo_id) VALUES (1, 1);
INSERT INTO camera_regole_prezzo (camere_id, regole_prezzo_id) VALUES (2, 1);
INSERT INTO camera_regole_prezzo (camere_id, regole_prezzo_id) VALUES (2, 2);

-- Prenotazione di esempio dell'utente admin (id 1) sulla camera 101 (id 1)
INSERT INTO prenotazione (data_check_in, data_check_out, numero_adulti, numero_bambini, prezzo_totale, stato, stato_pagamento, note_cliente, camera_id, utente_id) VALUES ('2027-06-10', '2027-06-14', 2, 0, 400.0, 'CONFERMATA', 'COMPLETATO', 'Richiesta camera ai piani alti.', 1, 1);

-- Recensione di esempio dell'utente admin (id 1)
INSERT INTO recensione (voto, testo, data_creazione, utente_id) VALUES (5, 'Soggiorno splendido: camere pulitissime e personale gentilissimo.', '2026-11-07 10:30:00', 1);
