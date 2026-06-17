-- ============================================================
-- 04-create_menores.sql
-- MS: MENORES (9003) | DB: menores
-- ============================================================
\connect menores

DROP TABLE IF EXISTS proyeccion_permiso CASCADE;
DROP TABLE IF EXISTS autorizacion CASCADE;
DROP TABLE IF EXISTS menor CASCADE;

CREATE TABLE menor (
    id        SERIAL PRIMARY KEY,
    rut       VARCHAR(12) UNIQUE NOT NULL,
    nombre    VARCHAR(80) NOT NULL,
    fecha_nac DATE NOT NULL,
    rut_tutor VARCHAR(12) NOT NULL
);

CREATE TABLE autorizacion (
    id             SERIAL PRIMARY KEY,
    rut_menor      VARCHAR(12) NOT NULL REFERENCES menor(rut),
    tipo           VARCHAR(30) NOT NULL,
    vigencia       DATE NOT NULL,
    notaria_origen VARCHAR(60) NOT NULL
);

CREATE TABLE proyeccion_permiso (
    rut_menor      VARCHAR(12) PRIMARY KEY,
    tipo           VARCHAR(30) NOT NULL,
    vigencia       DATE NOT NULL,
    notaria_origen VARCHAR(60) NOT NULL,
    sincronizado   DATE NOT NULL
);

INSERT INTO menor (rut, nombre, fecha_nac, rut_tutor) VALUES
('11223344-5', 'Emilia Rojas',    '2015-03-12', '12345678-9'),
('22334455-6', 'Tomas Pizarro',   '2013-07-22', '98765432-1'),
('33445566-7', 'Isidora Mendez',  '2016-11-05', '11111111-1'),
('44556677-8', 'Martin Soto',     '2014-01-30', '22222222-2'),
('55667788-9', 'Javiera Fuentes', '2017-09-18', '33333333-3'),
('66778899-0', 'Agustina Vera',   '2012-05-25', '44444444-4'),
('77889900-1', 'Cristobal Lagos', '2015-08-14', '55555555-5'),
('88990011-2', 'Sofia Carrasco',  '2011-02-07', '66666666-6'),
('99001122-3', 'Mateo Herrera',   '2016-12-19', '77777777-7');

INSERT INTO autorizacion (rut_menor, tipo, vigencia, notaria_origen) VALUES
('11223344-5', 'JUDICIAL',  '2025-06-30', 'NOTARIA ARICA 1'),
('22334455-6', 'NOTARIAL',  '2025-03-15', 'NOTARIA SANTIAGO 5'),
('33445566-7', 'JUDICIAL',  '2025-12-31', 'NOTARIA IQUIQUE 2'),
('44556677-8', 'NOTARIAL',  '2025-04-20', 'NOTARIA ANTOFAGASTA 3'),
('55667788-9', 'JUDICIAL',  '2025-08-10', 'NOTARIA VALPARAISO 1'),
('66778899-0', 'NOTARIAL',  '2025-10-01', 'NOTARIA CONCEPCION 2'),
('77889900-1', 'JUDICIAL',  '2026-01-20', 'NOTARIA TEMUCO 1'),
('88990011-2', 'NOTARIAL',  '2025-07-15', 'NOTARIA LA SERENA 3'),
('99001122-3', 'JUDICIAL',  '2025-11-30', 'NOTARIA RANCAGUA 1');

INSERT INTO proyeccion_permiso (rut_menor, tipo, vigencia, notaria_origen, sincronizado) VALUES
('11223344-5', 'JUDICIAL', '2025-06-30', 'NOTARIA ARICA 1',       '2025-01-05'),
('22334455-6', 'NOTARIAL', '2025-03-15', 'NOTARIA SANTIAGO 5',    '2025-01-06'),
('33445566-7', 'JUDICIAL', '2025-12-31', 'NOTARIA IQUIQUE 2',     '2025-01-07'),
('44556677-8', 'NOTARIAL', '2025-04-20', 'NOTARIA ANTOFAGASTA 3', '2025-01-08'),
('55667788-9', 'JUDICIAL', '2025-08-10', 'NOTARIA VALPARAISO 1',  '2025-01-09'),
('66778899-0', 'NOTARIAL', '2025-10-01', 'NOTARIA CONCEPCION 2',  '2025-01-10'),
('77889900-1', 'JUDICIAL', '2026-01-20', 'NOTARIA TEMUCO 1',      '2025-01-11'),
('88990011-2', 'NOTARIAL', '2025-07-15', 'NOTARIA LA SERENA 3',   '2025-01-12'),
('99001122-3', 'JUDICIAL', '2025-11-30', 'NOTARIA RANCAGUA 1',    '2025-01-13');
