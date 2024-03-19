-- Za dodavanje promijeniti naziv u "data.sql"
-- Nakon pokretanja aplikacije i dodavanja kolona promijeniti naziv u nešto drugo (npr. default.sql)

--dnevnik
INSERT INTO dnevnik_unosi(id, user_uid, datum, visina, tezina, bmi, puls, unos_vode, broj_koraka)
VALUES (1, 1, '2024-03-17', 165.5, 58, 23, 65, 2, 10000);

INSERT INTO dnevnik_unosi(id, user_uid, datum, visina, tezina, bmi, puls, unos_vode, broj_koraka)
VALUES (2, 1, '2024-03-19', 165.5, 58, 23, 70, 1.5, 6500);

--pregledi
INSERT INTO Pregledi(id, pacijent_uid, doktor_uid, dijagnoza, termin_pregleda)
VALUES (1, 2, 1, "promaha", "2024-03-15, 17:58");

INSERT INTO Pregledi(id, pacijent_uid, doktor_uid, dijagnoza, termin_pregleda)
VALUES (2, 2, 1, "ograisanje", "2024-03-19, 10:03");

--uputnice
INSERT INTO Uputnice(id, pregled_id, specijalista_uid, komentar, trajanje)
VALUES (1, 1, 2, 'molim savjet strucnjaka', "14?");

INSERT INTO Uputnice(id, pregled_id, specijalista_uid, komentar, trajanje)
VALUES (2, 1, 2, 'molim savjet strucnjaka', "14?");

