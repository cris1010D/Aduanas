-- ============================================================
-- 02-create_auth.sql
-- MS: AUTH (9001) | DB: auth
-- ============================================================
\connect auth

DROP TABLE IF EXISTS proyeccion_oficial CASCADE;
DROP TABLE IF EXISTS sesion CASCADE;
DROP TABLE IF EXISTS oficial CASCADE;

CREATE TABLE oficial (
    id       SERIAL PRIMARY KEY,
    rut      VARCHAR(12) UNIQUE NOT NULL,
    nombre   VARCHAR(80) NOT NULL,
    rol      VARCHAR(30) NOT NULL,
    activo   BOOLEAN DEFAULT TRUE, -- Se agregó la coma faltante al final de esta línea
    password VARCHAR(255) NOT NULL DEFAULT '$2b$10$/t0.GWIwJghQ3XI/XmadYuJzujQU7nPYAPRSlYxCDT/fbC8EtrY7G' -- Hash BCrypt verificado para la clave '123456'
);

CREATE TABLE sesion (
    id          SERIAL PRIMARY KEY,
    rut_oficial VARCHAR(12) NOT NULL REFERENCES oficial(rut),
    token       VARCHAR(120) NOT NULL,
    inicio      DATE NOT NULL,
    expira      DATE NOT NULL
);

CREATE TABLE proyeccion_oficial (
    rut          VARCHAR(12) PRIMARY KEY,
    nombre       VARCHAR(80) NOT NULL,
    rol          VARCHAR(30) NOT NULL,
    activo       BOOLEAN DEFAULT TRUE,
    sincronizado DATE NOT NULL
);

-- Roles validos: SUPERVISOR | OFICIAL | INSPECTOR  (password de todos: 123456)
INSERT INTO oficial (rut, nombre, rol, activo, password) VALUES
('12345678-9', 'Carlos Rojas',    'SUPERVISOR', TRUE,  '$2b$10$/t0.GWIwJghQ3XI/XmadYuJzujQU7nPYAPRSlYxCDT/fbC8EtrY7G'),
('98765432-1', 'Ana Pizarro',     'OFICIAL',    TRUE,  '$2b$10$/t0.GWIwJghQ3XI/XmadYuJzujQU7nPYAPRSlYxCDT/fbC8EtrY7G'),
('11111111-1', 'Luis Mendez',     'OFICIAL',    TRUE,  '$2b$10$/t0.GWIwJghQ3XI/XmadYuJzujQU7nPYAPRSlYxCDT/fbC8EtrY7G'),
('22222222-2', 'Maria Soto',      'INSPECTOR',  TRUE,  '$2b$10$/t0.GWIwJghQ3XI/XmadYuJzujQU7nPYAPRSlYxCDT/fbC8EtrY7G'),
('33333333-3', 'Pedro Fuentes',   'OFICIAL',    FALSE, '$2b$10$/t0.GWIwJghQ3XI/XmadYuJzujQU7nPYAPRSlYxCDT/fbC8EtrY7G'),
('44444444-4', 'Rosa Herrera',    'SUPERVISOR', TRUE,  '$2b$10$/t0.GWIwJghQ3XI/XmadYuJzujQU7nPYAPRSlYxCDT/fbC8EtrY7G'),
('55555555-5', 'Juan Carrasco',   'INSPECTOR',  TRUE,  '$2b$10$/t0.GWIwJghQ3XI/XmadYuJzujQU7nPYAPRSlYxCDT/fbC8EtrY7G'),
('66666666-6', 'Claudia Vera',    'OFICIAL',    TRUE,  '$2b$10$/t0.GWIwJghQ3XI/XmadYuJzujQU7nPYAPRSlYxCDT/fbC8EtrY7G'),
('77777777-7', 'Sergio Espinoza', 'OFICIAL',    FALSE, '$2b$10$/t0.GWIwJghQ3XI/XmadYuJzujQU7nPYAPRSlYxCDT/fbC8EtrY7G');

INSERT INTO sesion (rut_oficial, token, inicio, expira) VALUES
('12345678-9', 'tok-abc-001', '2025-01-10', '2025-02-10'),
('98765432-1', 'tok-abc-002', '2025-01-11', '2025-02-11'),
('11111111-1', 'tok-abc-003', '2025-01-12', '2025-02-12'),
('22222222-2', 'tok-abc-004', '2025-01-13', '2025-02-13'),
('44444444-4', 'tok-abc-005', '2025-01-14', '2025-02-14'),
('55555555-5', 'tok-abc-006', '2025-01-15', '2025-02-15'),
('66666666-6', 'tok-abc-007', '2025-01-16', '2025-02-16'),
('12345678-9', 'tok-abc-008', '2025-02-01', '2025-03-01'),
('98765432-1', 'tok-abc-009', '2025-02-02', '2025-03-02');

INSERT INTO proyeccion_oficial (rut, nombre, rol, activo, sincronizado) VALUES
('12345678-9', 'Carlos Rojas',    'SUPERVISOR', TRUE,  '2025-01-10'),
('98765432-1', 'Ana Pizarro',     'OFICIAL',    TRUE,  '2025-01-11'),
('11111111-1', 'Luis Mendez',     'OFICIAL',    TRUE,  '2025-01-12'),
('22222222-2', 'Maria Soto',      'INSPECTOR',  TRUE,  '2025-01-13'),
('33333333-3', 'Pedro Fuentes',   'OFICIAL',    FALSE, '2025-01-14'),
('44444444-4', 'Rosa Herrera',    'SUPERVISOR', TRUE,  '2025-01-15'),
('55555555-5', 'Juan Carrasco',   'INSPECTOR',  TRUE,  '2025-01-16'),
('66666666-6', 'Claudia Vera',    'OFICIAL',    TRUE,  '2025-01-17'),
('77777777-7', 'Sergio Espinoza', 'OFICIAL',    FALSE, '2025-01-18');