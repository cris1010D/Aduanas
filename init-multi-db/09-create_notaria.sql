-- ============================================================
-- 09-create_notaria.sql
-- MS: NOTARIA (9008) | DB: notaria
-- ============================================================
\connect notaria

DROP TABLE IF EXISTS proyeccion_poder CASCADE;
DROP TABLE IF EXISTS documento CASCADE;
DROP TABLE IF EXISTS poder CASCADE;

CREATE TABLE poder (
    id             SERIAL PRIMARY KEY,
    rut_titular    VARCHAR(12) NOT NULL,
    rut_apoderado  VARCHAR(12) NOT NULL,
    notaria_origen VARCHAR(60) NOT NULL,
    vigencia       DATE NOT NULL
);

CREATE TABLE documento (
    id            SERIAL PRIMARY KEY,
    id_poder      INT NOT NULL REFERENCES poder(id),
    tipo          VARCHAR(30) NOT NULL,
    folio         VARCHAR(20) UNIQUE NOT NULL,
    fecha_emision DATE NOT NULL
);

CREATE TABLE proyeccion_poder (
    rut_titular    VARCHAR(12) PRIMARY KEY,
    rut_apoderado  VARCHAR(12) NOT NULL,
    vigencia       DATE NOT NULL,
    notaria_origen VARCHAR(60) NOT NULL,
    sincronizado   DATE NOT NULL
);

INSERT INTO poder (rut_titular, rut_apoderado, notaria_origen, vigencia) VALUES
('11223344-5', '12345678-9', 'NOTARIA ARICA 1',        '2025-12-31'),
('22334455-6', '98765432-1', 'NOTARIA SANTIAGO 5',     '2025-06-30'),
('33445566-7', '11111111-1', 'NOTARIA IQUIQUE 2',      '2025-09-15'),
('44556677-8', '22222222-2', 'NOTARIA ANTOFAGASTA 3',  '2026-03-01'),
('55667788-9', '44444444-4', 'NOTARIA CONCEPCION 2',   '2025-08-20'),
('66778899-0', '55555555-5', 'NOTARIA VALPARAISO 1',   '2025-11-10'),
('77889900-1', '66666666-6', 'NOTARIA TEMUCO 1',       '2026-01-15'),
('88990011-2', '77777777-7', 'NOTARIA LA SERENA 3',    '2025-07-31'),
('99001122-3', '33333333-3', 'NOTARIA RANCAGUA 1',     '2025-10-05');

INSERT INTO documento (id_poder, tipo, folio, fecha_emision) VALUES
(1, 'PODER_SIMPLE',     'FOL-2025-001', '2025-01-05'),
(2, 'PODER_NOTARIAL',   'FOL-2025-002', '2025-01-06'),
(3, 'PODER_SIMPLE',     'FOL-2025-003', '2025-01-07'),
(4, 'PODER_ESPECIAL',   'FOL-2025-004', '2025-01-08'),
(5, 'PODER_NOTARIAL',   'FOL-2025-005', '2025-01-09'),
(6, 'PODER_SIMPLE',     'FOL-2025-006', '2025-01-10'),
(7, 'PODER_ESPECIAL',   'FOL-2025-007', '2025-01-11'),
(8, 'PODER_NOTARIAL',   'FOL-2025-008', '2025-01-12'),
(9, 'PODER_SIMPLE',     'FOL-2025-009', '2025-01-13');

INSERT INTO proyeccion_poder (rut_titular, rut_apoderado, vigencia, notaria_origen, sincronizado) VALUES
('11223344-5', '12345678-9', '2025-12-31', 'NOTARIA ARICA 1',       '2025-01-05'),
('22334455-6', '98765432-1', '2025-06-30', 'NOTARIA SANTIAGO 5',    '2025-01-06'),
('33445566-7', '11111111-1', '2025-09-15', 'NOTARIA IQUIQUE 2',     '2025-01-07'),
('44556677-8', '22222222-2', '2026-03-01', 'NOTARIA ANTOFAGASTA 3', '2025-01-08'),
('55667788-9', '44444444-4', '2025-08-20', 'NOTARIA CONCEPCION 2',  '2025-01-09'),
('66778899-0', '55555555-5', '2025-11-10', 'NOTARIA VALPARAISO 1',  '2025-01-10'),
('77889900-1', '66666666-6', '2026-01-15', 'NOTARIA TEMUCO 1',      '2025-01-11'),
('88990011-2', '77777777-7', '2025-07-31', 'NOTARIA LA SERENA 3',   '2025-01-12'),
('99001122-3', '33333333-3', '2025-10-05', 'NOTARIA RANCAGUA 1',    '2025-01-13');
