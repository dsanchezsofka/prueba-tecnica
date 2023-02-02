USE banca_test
DELETE FROM persona;
DELETE FROM cliente;

SET IDENTITY_INSERT persona ON;
INSERT INTO persona (personaId,nombre,genero,edad,identificacion,direccion,telefono)
VALUES (1,'Daniel Sanchez','M',30,'20650252','Fortaleza, Brasil','123456789');
SET IDENTITY_INSERT persona OFF;

SET IDENTITY_INSERT cliente ON;
INSERT INTO cliente (clienteId, personaId,contrasena,estado)
VALUES (1,1,'123',1);
SET IDENTITY_INSERT cliente OFF;