----------------Crear la Base de datos banca:

CREATE DATABASE banca_test
USE banca_test

----------------Crear tablas en la BD:

CREATE TABLE persona
(personaId INT IDENTITY PRIMARY KEY NOT NULL,
nombre VARCHAR(25),
genero VARCHAR(1),
edad INT,
identificacion VARCHAR(25),
direccion VARCHAR(50),
telefono VARCHAR(12));

CREATE TABLE cliente
(clienteId INT IDENTITY PRIMARY KEY NOT NULL,
personaId INT FOREIGN KEY REFERENCES persona ON DELETE CASCADE,
contrasena VARCHAR(25),
estado BIT);

CREATE TABLE cuenta
(cuentaId INT IDENTITY PRIMARY KEY NOT NULL,
clienteId INT FOREIGN KEY REFERENCES cliente ON DELETE CASCADE,
numero NUMERIC(18,0),
tipo VARCHAR(25),
saldoInicial MONEY,
estado BIT);

CREATE TABLE movimientos
(movimientosId INT IDENTITY PRIMARY KEY NOT NULL,
cuentaId INT FOREIGN KEY REFERENCES cuenta ON DELETE CASCADE,
fecha VARCHAR(25),
tipo VARCHAR(25),
valor MONEY,
saldo MONEY);

-------------------------Establecer Relaciones de tablas:

ALTER TABLE cliente ADD CONSTRAINT fk1
FOREIGN KEY (personaId) REFERENCES persona (personaId)

ALTER TABLE cuenta ADD CONSTRAINT fk2
FOREIGN KEY (clienteId) REFERENCES cliente (clienteId)

ALTER TABLE movimientos ADD CONSTRAINT fk3
FOREIGN KEY (cuentaId) REFERENCES cuenta (cuentaId)