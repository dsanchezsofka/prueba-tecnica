USE banca_test
DELETE FROM persona WHERE personaId IN (1);
SET IDENTITY_INSERT persona ON;
INSERT INTO persona (personaId,nombre,genero,edad,identificacion,direccion,telefono)
VALUES (1,'Daniel Sanchez','M',30,'20650252','Fortaleza, Brasil','123456789');
SET IDENTITY_INSERT persona OFF;