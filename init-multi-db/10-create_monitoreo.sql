-- ============================================================
-- 10-create_monitoreo.sql
-- MS: MONITOREO (9009) | DB: monitoreo
-- NOTA: ms-monitoreo NO usa JPA ni Kafka, solo Actuator + Feign
--       Estas tablas son gestionadas directamente via SQL / scripts
-- ============================================================
\connect monitoreo

DROP TABLE IF EXISTS proyeccion_estado CASCADE;
DROP TABLE IF EXISTS metrica CASCADE;
DROP TABLE IF EXISTS estado_ms CASCADE;

CREATE TABLE estado_ms (
    id          SERIAL PRIMARY KEY,
    nombre_ms   VARCHAR(30) UNIQUE NOT NULL,
    estado      VARCHAR(15) NOT NULL,
    fecha_check DATE NOT NULL,
    puerto      INT NOT NULL
);

CREATE TABLE metrica (
    id                  SERIAL PRIMARY KEY,
    nombre_ms           VARCHAR(30) NOT NULL REFERENCES estado_ms(nombre_ms),
    tiempo_respuesta_ms INT NOT NULL,
    disponibilidad      NUMERIC(5,2) NOT NULL,
    fecha               DATE NOT NULL
);

CREATE TABLE proyeccion_estado (
    nombre_ms           VARCHAR(30) PRIMARY KEY,
    estado              VARCHAR(15) NOT NULL,
    tiempo_respuesta_ms INT NOT NULL,
    disponibilidad      NUMERIC(5,2) NOT NULL,
    sincronizado        DATE NOT NULL
);

INSERT INTO estado_ms (nombre_ms, estado, fecha_check, puerto) VALUES
('ms-auth',      'UP', '2025-01-13', 9001),
('ms-proceso',   'UP', '2025-01-13', 9002),
('ms-menores',   'UP', '2025-01-13', 9003),
('ms-reporte',   'UP', '2025-01-13', 9004),
('ms-auditoria', 'UP', '2025-01-13', 9005),
('ms-sag',       'UP', '2025-01-13', 9006),
('ms-pdi',       'UP', '2025-01-13', 9007),
('ms-notaria',   'UP', '2025-01-13', 9008),
('ms-datos',     'UP', '2025-01-13', 9010);

INSERT INTO metrica (nombre_ms, tiempo_respuesta_ms, disponibilidad, fecha) VALUES
('ms-auth',      120, 99.95, '2025-01-13'),
('ms-proceso',   145, 99.90, '2025-01-13'),
('ms-menores',   98,  99.99, '2025-01-13'),
('ms-reporte',   310, 99.85, '2025-01-13'),
('ms-auditoria', 87,  99.99, '2025-01-13'),
('ms-sag',       200, 99.80, '2025-01-13'),
('ms-pdi',       175, 99.92, '2025-01-13'),
('ms-notaria',   130, 99.95, '2025-01-13'),
('ms-datos',     95,  99.99, '2025-01-13');

INSERT INTO proyeccion_estado (nombre_ms, estado, tiempo_respuesta_ms, disponibilidad, sincronizado) VALUES
('ms-auth',      'UP', 120, 99.95, '2025-01-13'),
('ms-proceso',   'UP', 145, 99.90, '2025-01-13'),
('ms-menores',   'UP', 98,  99.99, '2025-01-13'),
('ms-reporte',   'UP', 310, 99.85, '2025-01-13'),
('ms-auditoria', 'UP', 87,  99.99, '2025-01-13'),
('ms-sag',       'UP', 200, 99.80, '2025-01-13'),
('ms-pdi',       'UP', 175, 99.92, '2025-01-13'),
('ms-notaria',   'UP', 130, 99.95, '2025-01-13'),
('ms-datos',     'UP', 95,  99.99, '2025-01-13');
