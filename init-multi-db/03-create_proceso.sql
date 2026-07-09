-- ============================================================
-- 03-create_proceso.sql
-- MS: PROCESO (9002) | DB: proceso
-- ============================================================
\connect proceso

DROP TABLE IF EXISTS proyeccion_viajero CASCADE;
DROP TABLE IF EXISTS movimiento CASCADE;
DROP TABLE IF EXISTS viajero CASCADE;

CREATE TABLE viajero (
    id           SERIAL PRIMARY KEY,
    rut          VARCHAR(12) UNIQUE NOT NULL,
    nombre       VARCHAR(80) NOT NULL,
    pasaporte    VARCHAR(20) UNIQUE,
    nacionalidad VARCHAR(40) NOT NULL
);

CREATE TABLE movimiento (
    id              SERIAL PRIMARY KEY,
    rut_viajero     VARCHAR(12) NOT NULL REFERENCES viajero(rut),
    tipo            VARCHAR(20) NOT NULL,
    fecha           DATE NOT NULL,
    paso_fronterizo VARCHAR(40) NOT NULL
);

CREATE TABLE proyeccion_viajero (
    rut               VARCHAR(12) PRIMARY KEY,
    nombre            VARCHAR(80) NOT NULL,
    nacionalidad      VARCHAR(40) NOT NULL,
    ultimo_movimiento DATE NOT NULL,
    sincronizado      DATE NOT NULL
);

INSERT INTO viajero (rut, nombre, pasaporte, nacionalidad) VALUES
('10111213-4', 'Jorge Alvarez',   'PA123456', 'CHILENA'),
('20212223-5', 'Sofia Reyes',     'PA234567', 'ARGENTINA'),
('30313233-6', 'Diego Castro',    'PA345678', 'PERUANA'),
('40414243-7', 'Valentina Mora',  'PA456789', 'BOLIVIANA'),
('50515253-8', 'Andres Lagos',    'PA567890', 'CHILENA'),
('60616263-9', 'Camila Torres',   'PA678901', 'COLOMBIANA'),
('70717273-K', 'Ricardo Nunez',   'PA789012', 'VENEZOLANA'),
('80818283-1', 'Patricia Gomez',  'PA890123', 'ECUATORIANA'),
('90919293-2', 'Martin Vargas',   'PA901234', 'BRASILENA');

INSERT INTO movimiento (rut_viajero, tipo, fecha, paso_fronterizo) VALUES
('10111213-4', 'INGRESO',  '2025-01-05', 'CHACALLUTA'),
('20212223-5', 'EGRESO',   '2025-01-06', 'LOS LIBERTADORES'),
('30313233-6', 'INGRESO',  '2025-01-07', 'COLCHANE'),
('40414243-7', 'INGRESO',  '2025-01-08', 'CHACALLUTA'),
('50515253-8', 'EGRESO',   '2025-01-09', 'LOS LIBERTADORES'),
('60616263-9', 'INGRESO',  '2025-01-10', 'PINO HACHADO'),
('70717273-K', 'EGRESO',   '2025-01-11', 'COLCHANE'),
('80818283-1', 'INGRESO',  '2025-01-12', 'CHACALLUTA'),
('90919293-2', 'EGRESO',   '2025-01-13', 'LOS LIBERTADORES');

INSERT INTO proyeccion_viajero (rut, nombre, nacionalidad, ultimo_movimiento, sincronizado) VALUES
('10111213-4', 'Jorge Alvarez',  'CHILENA',     '2025-01-05', '2025-01-05'),
('20212223-5', 'Sofia Reyes',    'ARGENTINA',   '2025-01-06', '2025-01-06'),
('30313233-6', 'Diego Castro',   'PERUANA',     '2025-01-07', '2025-01-07'),
('40414243-7', 'Valentina Mora', 'BOLIVIANA',   '2025-01-08', '2025-01-08'),
('50515253-8', 'Andres Lagos',   'CHILENA',     '2025-01-09', '2025-01-09'),
('60616263-9', 'Camila Torres',  'COLOMBIANA',  '2025-01-10', '2025-01-10'),
('70717273-K', 'Ricardo Nunez',  'VENEZOLANA',  '2025-01-11', '2025-01-11'),
('80818283-1', 'Patricia Gomez', 'ECUATORIANA', '2025-01-12', '2025-01-12'),
('90919293-2', 'Martin Vargas',  'BRASILENA',   '2025-01-13', '2025-01-13');
