-- Za dodavanje promijeniti naziv u "data.sql"
-- Nakon pokretanja aplikacije i dodavanja kolona promijeniti naziv u nešto drugo (npr. default.sql)

INSERT INTO pitanja (user_uid, naslov, sadrzaj, anonimnost) VALUES (1, "Ishrana", "Koliki je preporučeni dnevni unos kalorija?", 1);
INSERT INTO pitanja (user_uid, naslov, sadrzaj, anonimnost) VALUES (2, "Slatkiši", "Zdravi slatkiši???", 0);


INSERT INTO komentari (user_uid, question_id, sadrzaj, anonimnost) VALUES (2, 1, "Zavisi od spola", 1);
INSERT INTO komentari (user_uid, question_id, sadrzaj, anonimnost) VALUES (1, 2, "Kolac od hurmi", 0);