-- Za dodavanje promijeniti naziv u "data.sql"
-- Nakon pokretanja aplikacije i dodavanja kolona promijeniti naziv u nešto drugo (npr. default.sql)

INSERT INTO ankete (id, user_uid, naslov, opis, status) VALUES (1, 1, "Upitnik za pušače", "Želite prestati pušiti? Ispunite ovu anketu i saznajte kako!", 1);
INSERT INTO ankete (id, user_uid, naslov, opis, status) VALUES (2, 2, "Svakodnevne nezdrave navike", "Koje stvari radimo iz dana u dan, a ne znamo da su loše po naše zdravlje?", 0);

INSERT INTO ankete_pitanja (id, survey_id, sadrzaj) VALUES (1, 1, "Koliko dugo aktivno pušite?");
INSERT INTO ankete_pitanja (id, survey_id, sadrzaj) VALUES (2, 1, "Koje cigarete pušite?");
INSERT INTO ankete_pitanja (id, survey_id, sadrzaj) VALUES (3, 2, "Koliko u prosjeku dnevno hodate?");
INSERT INTO ankete_pitanja (id, survey_id, sadrzaj) VALUES (4, 2, "Koliko u prosjeku dnevno unosite tečnosti?");

INSERT INTO ponudjeni_odgovori (id, question_id, sadrzaj) VALUES (1, 1, "Manje od 5 godina");
INSERT INTO ponudjeni_odgovori (id, question_id, sadrzaj) VALUES (2, 1, "Više od 5 godina");
INSERT INTO ponudjeni_odgovori (id, question_id, sadrzaj) VALUES (3, 2, "Marlboro");
INSERT INTO ponudjeni_odgovori (id, question_id, sadrzaj) VALUES (4, 2, "Drina sarajevska");
INSERT INTO ponudjeni_odgovori (id, question_id, sadrzaj) VALUES (5, 3, "Manje od 10000 koraka");
INSERT INTO ponudjeni_odgovori (id, question_id, sadrzaj) VALUES (6, 3, "Više od 10000 koraka");
INSERT INTO ponudjeni_odgovori (id, question_id, sadrzaj) VALUES (7, 4, "Manje od 2 litra");
INSERT INTO ponudjeni_odgovori (id, question_id, sadrzaj) VALUES (8, 4, "Više od 2 litra");

INSERT INTO anketa_odgovori (id, answer_id) VALUES (1, 2);
INSERT INTO anketa_odgovori (id, answer_id) VALUES (2, 4);
INSERT INTO anketa_odgovori (id, answer_id) VALUES (3, 6);
INSERT INTO anketa_odgovori (id, answer_id) VALUES (4, 1);
