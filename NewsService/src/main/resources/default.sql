-- Za dodavanje promijeniti naziv u "data.sql"
-- Nakon pokretanja aplikacije i dodavanja kolona promijeniti naziv u nešto drugo (npr. default.sql)

INSERT INTO novosti (id, naslov, sadrzaj, slika, user_uid) VALUES (1, "Jedno dete pojeo pacov", "Pacovi oveljiki", "nemaslike.jpg", "123");
INSERT INTO novosti (id, naslov, sadrzaj, slika, user_uid) VALUES (2, "Koju posluku porat", "Morat cete ovo malo na montazu", "nemaslike.jpg", "124");


INSERT INTO notifikacije (id, tip_notifikacije, sadrzaj, user_uid) VALUES (1, "alert", "Imate termin u subotu u 8:00h", "123");
INSERT INTO notifikacije (id, tip_notifikacije, sadrzaj, user_uid) VALUES (2, "alert", "Imate termin u petak u 12:00h", "124");

INSERT INTO produzenje_terapije (id, lijek, napomena, kolicina, pacijent_uid, doktor_uid) VALUES (1, "Aspirin", "1 u 6 dana", 2, "100", "201");
INSERT INTO produzenje_terapije (id, lijek, napomena, kolicina, pacijent_uid, doktor_uid) VALUES (2, "Controloc", "2x dnevno", 1, "101", "201");