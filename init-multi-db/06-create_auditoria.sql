-- ============================================================
-- 06-create_auditoria.sql
-- MS: AUDITORIA (9005) | DB: auditoria
-- ============================================================
\connect auditoria

DROP TABLE IF EXISTS proyeccion_evento CASCADE;
DROP TABLE IF EXISTS detalle_log CASCADE;
DROP TABLE IF EXISTS log_evento CASCADE;

CREATE TABLE log_evento (
    id          SERIAL PRIMARY KEY,
    rut_oficial VARCHAR(12) NOT NULL,
    accion      VARCHAR(60) NOT NULL,
    fecha       DATE NOT NULL,
    ms_origen   VARCHAR(30) NOT NULL
);

CREATE TABLE detalle_log (
    id          SERIAL PRIMARY KEY,
    id_log      INT NOT NULL REFERENCES log_evento(id),
    entidad     VARCHAR(40) NOT NULL,
    campo       VARCHAR(40) NOT NULL,
    valor_nuevo VARCHAR(80) NOT NULL
);

CREATE TABLE proyeccion_evento (
    rut_oficial   VARCHAR(12) PRIMARY KEY,
    ultima_accion VARCHAR(60) NOT NULL,
    fecha         DATE NOT NULL,
    ms_origen     VARCHAR(30) NOT NULL,
    sincronizado  DATE NOT NULL
);

INSERT INTO log_evento (rut_oficial, accion, fecha, ms_origen) VALUES
('12345678-9', 'CREAR_VIAJERO',       '2025-01-05', 'ms-proceso'),
('98765432-1', 'ACTUALIZAR_OFICIAL',  '2025-01-06', 'ms-auth'),
('11111111-1', 'CREAR_AUTORIZACION',  '2025-01-07', 'ms-menores'),
('22222222-2', 'GENERAR_REPORTE',     '2025-01-08', 'ms-reporte'),
('44444444-4', 'CREAR_DECLARACION',   '2025-01-09', 'ms-sag'),
('55555555-5', 'CONSULTAR_PDI',       '2025-01-10', 'ms-pdi'),
('66666666-6', 'VERIFICAR_PODER',     '2025-01-11', 'ms-notaria'),
('77777777-7', 'ELIMINAR_SESION',     '2025-01-12', 'ms-auth'),
('12345678-9', 'ACTUALIZAR_VIAJERO',  '2025-01-13', 'ms-proceso');

INSERT INTO detalle_log (id_log, entidad, campo, valor_nuevo) VALUES
(1, 'viajero',      'rut',          '10111213-4'),
(2, 'oficial',      'rol',          'SUPERVISOR'),
(3, 'autorizacion', 'tipo',         'JUDICIAL'),
(4, 'reporte',      'formato',      'PDF'),
(5, 'declaracion',  'estado',       'APROBADO'),
(6, 'antecedente',  'resultado',    'SIN_REGISTROS'),
(7, 'poder',        'vigencia',     '2026-01-01'),
(8, 'sesion',       'activo',       'FALSE'),
(9, 'viajero',      'nacionalidad', 'CHILENA');

INSERT INTO proyeccion_evento (rut_oficial, ultima_accion, fecha, ms_origen, sincronizado) VALUES
('12345678-9', 'ACTUALIZAR_VIAJERO',  '2025-01-13', 'ms-proceso',  '2025-01-13'),
('98765432-1', 'ACTUALIZAR_OFICIAL',  '2025-01-06', 'ms-auth',     '2025-01-06'),
('11111111-1', 'CREAR_AUTORIZACION',  '2025-01-07', 'ms-menores',  '2025-01-07'),
('22222222-2', 'GENERAR_REPORTE',     '2025-01-08', 'ms-reporte',  '2025-01-08'),
('44444444-4', 'CREAR_DECLARACION',   '2025-01-09', 'ms-sag',      '2025-01-09'),
('55555555-5', 'CONSULTAR_PDI',       '2025-01-10', 'ms-pdi',      '2025-01-10'),
('66666666-6', 'VERIFICAR_PODER',     '2025-01-11', 'ms-notaria',  '2025-01-11'),
('77777777-7', 'ELIMINAR_SESION',     '2025-01-12', 'ms-auth',     '2025-01-12'),
('33333333-3', 'CREAR_VIAJERO',       '2025-01-05', 'ms-proceso',  '2025-01-05');
