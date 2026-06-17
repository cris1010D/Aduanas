-- ============================================================
-- 05-create_reporte.sql
-- MS: REPORTE (9004) | DB: reporte
-- ============================================================
\connect reporte

DROP TABLE IF EXISTS proyeccion_reporte CASCADE;
DROP TABLE IF EXISTS detalle_reporte CASCADE;
DROP TABLE IF EXISTS reporte CASCADE;

CREATE TABLE reporte (
    id          SERIAL PRIMARY KEY,
    tipo        VARCHAR(30) NOT NULL,
    fecha       DATE NOT NULL,
    rut_oficial VARCHAR(12) NOT NULL,
    formato     VARCHAR(10) NOT NULL
);

CREATE TABLE detalle_reporte (
    id          SERIAL PRIMARY KEY,
    id_reporte  INT NOT NULL REFERENCES reporte(id),
    descripcion VARCHAR(120) NOT NULL,
    cantidad    INT NOT NULL,
    fecha_dato  DATE NOT NULL
);

CREATE TABLE proyeccion_reporte (
    tipo            VARCHAR(30) PRIMARY KEY,
    ultimo_generado DATE NOT NULL,
    rut_oficial     VARCHAR(12) NOT NULL,
    formato         VARCHAR(10) NOT NULL,
    sincronizado    DATE NOT NULL
);

INSERT INTO reporte (tipo, fecha, rut_oficial, formato) VALUES
('INGRESO_DIARIO',    '2025-01-05', '12345678-9', 'PDF'),
('EGRESO_DIARIO',     '2025-01-06', '98765432-1', 'PDF'),
('MENORES_SEMANA',    '2025-01-07', '11111111-1', 'EXCEL'),
('ANTECEDENTES_MES',  '2025-01-08', '22222222-2', 'PDF'),
('SAG_DIARIO',        '2025-01-09', '44444444-4', 'PDF'),
('AUDITORIA_SEMANAL', '2025-01-10', '55555555-5', 'EXCEL'),
('VEHICULOS_DIARIO',  '2025-01-11', '66666666-6', 'PDF'),
('PODERES_MES',       '2025-01-12', '12345678-9', 'PDF'),
('RESUMEN_MENSUAL',   '2025-01-13', '98765432-1', 'EXCEL');

INSERT INTO detalle_reporte (id_reporte, descripcion, cantidad, fecha_dato) VALUES
(1, 'Viajeros ingresados por CHACALLUTA',       45, '2025-01-05'),
(2, 'Viajeros egresados por LOS LIBERTADORES',  32, '2025-01-06'),
(3, 'Menores con autorizacion vigente',          18, '2025-01-07'),
(4, 'Consultas PDI realizadas en enero',         60, '2025-01-08'),
(5, 'Declaraciones SAG procesadas',              27, '2025-01-09'),
(6, 'Eventos auditados en la semana',            94, '2025-01-10'),
(7, 'Vehiculos registrados en COLCHANE',         13, '2025-01-11'),
(8, 'Poderes notariales verificados en enero',   22, '2025-01-12'),
(9, 'Resumen total de movimientos enero',       210, '2025-01-13');

INSERT INTO proyeccion_reporte (tipo, ultimo_generado, rut_oficial, formato, sincronizado) VALUES
('INGRESO_DIARIO',    '2025-01-05', '12345678-9', 'PDF',   '2025-01-05'),
('EGRESO_DIARIO',     '2025-01-06', '98765432-1', 'PDF',   '2025-01-06'),
('MENORES_SEMANA',    '2025-01-07', '11111111-1', 'EXCEL', '2025-01-07'),
('ANTECEDENTES_MES',  '2025-01-08', '22222222-2', 'PDF',   '2025-01-08'),
('SAG_DIARIO',        '2025-01-09', '44444444-4', 'PDF',   '2025-01-09'),
('AUDITORIA_SEMANAL', '2025-01-10', '55555555-5', 'EXCEL', '2025-01-10'),
('VEHICULOS_DIARIO',  '2025-01-11', '66666666-6', 'PDF',   '2025-01-11'),
('PODERES_MES',       '2025-01-12', '12345678-9', 'PDF',   '2025-01-12'),
('RESUMEN_MENSUAL',   '2025-01-13', '98765432-1', 'EXCEL', '2025-01-13');
