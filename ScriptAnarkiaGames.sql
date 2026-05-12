-- ======================================================
-- BASE DE DATOS ANARKIAGAMES 
-- ======================================================

DROP DATABASE IF EXISTS anarkiagames;
CREATE DATABASE anarkiagames;
USE anarkiagames;

-- Orden correcto de eliminación
DROP TABLE IF EXISTS detalle_factura;
DROP TABLE IF EXISTS factura;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS tipo_ticket;

-- ======================================================
-- TABLA USUARIO
-- ======================================================

CREATE TABLE usuario (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(100) UNIQUE NOT NULL,
  clave VARCHAR(255) NOT NULL,
  fecha DATE NOT NULL,
  rol VARCHAR(50) NOT NULL,
  reset_password_token VARCHAR(255),
  token_expiry_date DATETIME
) ENGINE=InnoDB;

-- ======================================================
-- TABLA CLIENTE
-- ======================================================

CREATE TABLE cliente (
  id BIGINT PRIMARY KEY,
  tipo_cliente VARCHAR(50),
  FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ======================================================
-- TABLA TIPO_TICKET
-- ======================================================

CREATE TABLE tipo_ticket (
  tipo_ticket VARCHAR(50) PRIMARY KEY,
  stock_maximo INT NOT NULL,
  precio BIGINT NOT NULL,
  evento_nombre VARCHAR(255) NOT NULL,
  descripcion TEXT,
  imagen_url VARCHAR(255)
) ENGINE=InnoDB;

-- ======================================================
-- TABLA FACTURA
-- ======================================================

CREATE TABLE factura (
  cod_fac BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_usuario BIGINT NOT NULL,
  fac_emi DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total_pagado BIGINT NOT NULL,
  FOREIGN KEY (id_usuario) REFERENCES cliente(id)
) ENGINE=InnoDB;

-- ======================================================
-- TABLA DETALLE_FACTURA
-- ======================================================

CREATE TABLE detalle_factura (
  id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
  cod_fac BIGINT NOT NULL,
  tipo_ticket_vendido VARCHAR(50) NOT NULL,
  cantidad INT NOT NULL DEFAULT 1,
  precio_unitario_venta BIGINT NOT NULL,
  FOREIGN KEY (cod_fac) REFERENCES factura(cod_fac) ON DELETE CASCADE,
  FOREIGN KEY (tipo_ticket_vendido) REFERENCES tipo_ticket(tipo_ticket)
) ENGINE=InnoDB;

-- ======================================================
-- DATOS TIPO TICKET
-- ======================================================

INSERT INTO tipo_ticket
(tipo_ticket, stock_maximo, precio, evento_nombre, descripcion, imagen_url)
VALUES
('Espectador CS2',100,2000,'Torneo Counter Strike 2','Acceso espectador','/Imagenes/cs2.png'),
('Competidor CS2',32,3500,'Torneo Counter Strike 2','Modo competitivo','/Imagenes/cs2.png'),
('Espectador Smash',150,2000,'Torneo Smash','Acceso espectador','/Imagenes/smash.png'),
('Competidor Smash',64,3500,'Torneo Smash','Modo competitivo','/Imagenes/smash.png');

-- ======================================================
-- USUARIOS ADMINISTRADORES 
-- ======================================================

INSERT INTO usuario(nombre,correo,clave,fecha,rol) VALUES
('Adrian','adrian.corpe05@gmail.com','$2a$10$W1te13GabFluyLynISZezeWd5csq0bNZSC0tzrbM3PqMO/GA1pVqG',NOW(),'Administrador'),
('Diego','diegomayora24@gmail.com','$2a$10$W1te13GabFluyLynISZezeWd5csq0bNZSC0tzrbM3PqMO/GA1pVqG',NOW(),'Administrador'),
('John','jernesto.sepulveda@gmail.com','$2a$10$W1te13GabFluyLynISZezeWd5csq0bNZSC0tzrbM3PqMO/GA1pVqG',NOW(),'Administrador');

-- ======================================================
-- CLIENTES
-- ======================================================

INSERT INTO usuario(nombre,correo,clave,fecha,rol)
VALUES ('Ana Torres','ana@correo.com',
'$2a$10$f4O0CVjJJx8uNpL7z9Rbv.PLA8urVWPVPdf3/3040SWstqRlyrD3e',NOW(),'Cliente');

SET @ana_id = LAST_INSERT_ID();
INSERT INTO cliente VALUES(@ana_id,'Espectador');

INSERT INTO usuario(nombre,correo,clave,fecha,rol)
VALUES ('Luis Gomez','luis@correo.com',
'$2a$10$f4O0CVjJJx8uNpL7z9Rbv.PLA8urVWPVPdf3/3040SWstqRlyrD3e',NOW(),'Cliente');

SET @luis_id = LAST_INSERT_ID();
INSERT INTO cliente VALUES(@luis_id,'Espectador');

INSERT INTO usuario(nombre,correo,clave,fecha,rol)
VALUES ('Carla Diaz','carla@correo.com',
'$2a$10$f4O0CVjJJx8uNpL7z9Rbv.PLA8urVWPVPdf3/3040SWstqRlyrD3e',NOW(),'Cliente');

SET @carla_id = LAST_INSERT_ID();
INSERT INTO cliente VALUES(@carla_id,'Espectador');

-- ======================================================
-- FACTURAS
-- ======================================================

INSERT INTO factura(id_usuario,total_pagado)
VALUES(@ana_id,3000);

SET @factura1_id = LAST_INSERT_ID();

INSERT INTO detalle_factura
(cod_fac,tipo_ticket_vendido,cantidad,precio_unitario_venta)
VALUES(@factura1_id,'Espectador CS2',2,1500);

INSERT INTO factura(id_usuario,total_pagado)
VALUES(@luis_id,7000);

SET @factura2_id = LAST_INSERT_ID();

INSERT INTO detalle_factura VALUES(NULL,@factura2_id,'Competidor CS2',1,5000);
INSERT INTO detalle_factura VALUES(NULL,@factura2_id,'Espectador Smash',2,1000);

UPDATE cliente SET tipo_cliente='Competidor'
WHERE id=@luis_id;

INSERT INTO factura(id_usuario,total_pagado)
VALUES(@carla_id,5000);

SET @factura3_id = LAST_INSERT_ID();

INSERT INTO detalle_factura
(cod_fac,tipo_ticket_vendido,cantidad,precio_unitario_venta)
VALUES(@factura3_id,'Espectador Smash',5,1000);

-- ======================================================
-- FIN
-- ======================================================