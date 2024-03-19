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

--tip nalaza
INSERT INTO tip_nalaza(id, naziv)
VALUES (1, "hormoni stitne");

INSERT INTO tip_nalaza(id, naziv)
VALUES (2, "urin");

INSERT INTO tip_nalaza(id, naziv)
VALUES (3, "krvna slika");

--nalaz
INSERT INTO nalazi(id, pacijent_uid, laborant_uid, doktor_uid, tip_nalaza_id, dijagnoza, vrijeme_pregleda)
VALUES (1, 2, 2, 1, 1, null, null);

INSERT INTO nalazi(id, pacijent_uid, laborant_uid, doktor_uid, tip_nalaza_id, dijagnoza, vrijeme_pregleda)
VALUES (2, 2, 2, 1, 2, "nije dobro", "2024-03-19, 10:03");

INSERT INTO nalazi(id, pacijent_uid, laborant_uid, doktor_uid, tip_nalaza_id, dijagnoza, vrijeme_pregleda)
VALUES (3, 2, 2, 1, 3, "nije lose", "2024-03-19, 12:45");

--stavke
INSERT INTO stavke(id, naziv, ref_min, ref_max, ref, mjerna_jedinica, tip_nalaza_id)
VALUES (1, "Fe", 8, 30, null, "ne znam", 3);

INSERT INTO stavke(id, naziv, ref_min, ref_max, ref, mjerna_jedinica, tip_nalaza_id)
VALUES (2, "Urea", 0, 10, null, "ne znam", 3);

INSERT INTO stavke(id, naziv, ref_min, ref_max, ref, mjerna_jedinica, tip_nalaza_id)
VALUES (3, "TSH", 0, 10, null, "ne znam", 1);

INSERT INTO stavke(id, naziv, ref_min, ref_max, ref, mjerna_jedinica, tip_nalaza_id)
VALUES (4, "bakterije", null, null, "rijetko, malo", null, 2);

--nalaz-stavke
INSERT INTO nalaz_rezultati(id, nalaz_id, stavka_id, vrijednost)
VALUES (1, 3, 1, 8.80);

INSERT INTO nalaz_rezultati(id, nalaz_id, stavka_id, vrijednost)
VALUES (2, 3, 2, 5);

INSERT INTO nalaz_rezultati(id, nalaz_id, stavka_id, vrijednost)
VALUES (3, 2, 4, "malo");

INSERT INTO nalaz_rezultati(id, nalaz_id, stavka_id, vrijednost)
VALUES (4, 1, 3, 5);



