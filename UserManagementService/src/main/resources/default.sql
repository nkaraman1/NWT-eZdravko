-- Za dodavanje promijeniti naziv u "data.sql"
-- Nakon pokretanja aplikacije i dodavanja kolona promijeniti naziv u nešto drugo (npr. default.sql)

INSERT INTO role (id, naziv_role, potreban_kod, kod) VALUES (1, "Doktor", 1, "rrd0kt0rrr");
INSERT INTO role (id, naziv_role, potreban_kod, kod) VALUES (2, "Lab. tehnicar", 1, "rrt3hn1c4r");
INSERT INTO role (id, naziv_role, potreban_kod, kod) VALUES (3, "Apotekar", 1, "rrap0t3k4r");
INSERT INTO role (id, naziv_role, potreban_kod, kod) VALUES (4, "Pacijent", 0, null);

INSERT INTO korisnici (id, uid, adresa_stanovanja, broj_knjizice, broj_telefona, datum_rodjenja, email, ime, password, prezime, slika, spol, role_id)
VALUES                (1, 1, "Adresa", "12345", "062123456", '2001-01-10', "esmajic2@etf.unsa.ba", "Elvedin", "password123", "Smajić",
"img_path", 0, 1);

INSERT INTO korisnici (id, uid, adresa_stanovanja, broj_knjizice, broj_telefona, datum_rodjenja, email, ime, password, prezime, slika, spol, role_id)
VALUES                (2, 2, "Adresa", "12345", "061235771", '1980-05-31', "nnekic1@etf.unsa.ba", "Neko", "password123", "Nekić",
"img_path", 0, 2);