-- ============================================================
-- 07-create_sag.sql
-- MS: SAG (9006) | DB: sag
-- ============================================================
\connect sag

DROP TABLE IF EXISTS proyeccion_declaracion CASCADE;
DROP TABLE IF EXISTS item_declaracion CASCADE;
DROP TABLE IF EXISTS declaracion CASCADE;

CREATE TABLE declaracion (
    id              SERIAL PRIMARY KEY,
    rut_viajero     VARCHAR(12) NOT NULL,
    fecha           DATE NOT NULL,
    estado          VARCHAR(20) NOT NULL,
    paso_fronterizo VARCHAR(40) NOT NULL
);

CREATE TABLE item_declaracion (
    id             SERIAL PRIMARY KEY,
    id_declaracion INT NOT NULL REFERENCES declaracion(id),
    descripcion    VARCHAR(80) NOT NULL,
    cantidad       INT NOT NULL,
    riesgo         VARCHAR(15) NOT NULL
);

CREATE TABLE proyeccion_declaracion (
    rut_viajero     VARCHAR(12) PRIMARY KEY,
    ultima_fecha    DATE NOT NULL,
    estado          VARCHAR(20) NOT NULL,
    paso_fronterizo VARCHAR(40) NOT NULL,
    sincronizado    DATE NOT NULL
);

INSERT INTO declaracion (rut_viajero, fecha, estado, paso_fronterizo) VALUES
('10111213-4', '2025-01-05', 'APROBADO',   'CHACALLUTA'),
('20212223-5', '2025-01-06', 'RECHAZADO',  'LOS LIBERTADORES'),
('30313233-6', '2025-01-07', 'APROBADO',   'COLCHANE'),
('40414243-7', '2025-01-08', 'PENDIENTE',  'CHACALLUTA'),
('50515253-8', '2025-01-09', 'APROBADO',   'LOS LIBERTADORES'),
('60616263-9', '2025-01-10', 'RECHAZADO',  'PINO HACHADO'),
('70717273-K', '2025-01-11', 'APROBADO',   'COLCHANE'),
('80818283-1', '2025-01-12', 'PENDIENTE',  'CHACALLUTA'),
('90919293-2', '2025-01-13', 'APROBADO',   'LOS LIBERTADORES');

INSERT INTO item_declaracion (id_declaracion, descripcion, cantidad, riesgo) VALUES
(1, 'Frutas frescas manzana',      5, 'BAJO'),
(2, 'Carne de res sin certificado',3, 'ALTO'),
(3, 'Semillas de maiz importadas', 2, 'MEDIO'),
(4, 'Lacteos envasados',           4, 'BAJO'),
(5, 'Embutidos certificados',      6, 'BAJO'),
(6, 'Plantas ornamentales vivas',  8, 'ALTO'),
(7, 'Miel envasada importada',     3, 'MEDIO'),
(8, 'Cereales procesados',         5, 'BAJO'),
(9, 'Huevos frescos sin sello',    2, 'ALTO');

INSERT INTO proyeccion_declaracion (rut_viajero, ultima_fecha, estado, paso_fronterizo, sincronizado) VALUES
('10111213-4', '2025-01-05', 'APROBADO',  'CHACALLUTA',       '2025-01-05'),
('20212223-5', '2025-01-06', 'RECHAZADO', 'LOS LIBERTADORES', '2025-01-06'),
('30313233-6', '2025-01-07', 'APROBADO',  'COLCHANE',         '2025-01-07'),
('40414243-7', '2025-01-08', 'PENDIENTE', 'CHACALLUTA',       '2025-01-08'),
('50515253-8', '2025-01-09', 'APROBADO',  'LOS LIBERTADORES', '2025-01-09'),
('60616263-9', '2025-01-10', 'RECHAZADO', 'PINO HACHADO',     '2025-01-10'),
('70717273-K', '2025-01-11', 'APROBADO',  'COLCHANE',         '2025-01-11'),
('80818283-1', '2025-01-12', 'PENDIENTE', 'CHACALLUTA',       '2025-01-12'),
('90919293-2', '2025-01-13', 'APROBADO',  'LOS LIBERTADORES', '2025-01-13');
