-- ============================================================
-- 11-create_datos.sql
-- MS: DATOS (9010) | DB: datos
-- ============================================================
\connect datos

DROP TABLE IF EXISTS proyeccion_cache CASCADE;
DROP TABLE IF EXISTS cache_entry CASCADE;
DROP TABLE IF EXISTS configuracion CASCADE;

CREATE TABLE configuracion (
    id        SERIAL PRIMARY KEY,
    clave     VARCHAR(60) UNIQUE NOT NULL,
    valor     VARCHAR(120) NOT NULL,
    ms_duenio VARCHAR(30) NOT NULL,
    activo    BOOLEAN DEFAULT TRUE
);

CREATE TABLE cache_entry (
    id          SERIAL PRIMARY KEY,
    clave       VARCHAR(60) NOT NULL REFERENCES configuracion(clave),
    valor_cache VARCHAR(120) NOT NULL,
    expira      DATE NOT NULL,
    hits        INT DEFAULT 0
);

CREATE TABLE proyeccion_cache (
    clave        VARCHAR(60) PRIMARY KEY,
    valor_cache  VARCHAR(120) NOT NULL,
    expira       DATE NOT NULL,
    ms_duenio    VARCHAR(30) NOT NULL,
    sincronizado DATE NOT NULL
);

INSERT INTO configuracion (clave, valor, ms_duenio, activo) VALUES
('auth.token.expiracion',       '3600',            'ms-auth',      TRUE),
('auth.max.sesiones',           '5',               'ms-auth',      TRUE),
('proceso.pasos.activos',       'CHACALLUTA,COLCHANE,LOS_LIBERTADORES,PINO_HACHADO', 'ms-proceso', TRUE),
('menores.edad.maxima',         '17',              'ms-menores',   TRUE),
('reporte.formato.default',     'PDF',             'ms-reporte',   TRUE),
('sag.riesgo.niveles',          'BAJO,MEDIO,ALTO', 'ms-sag',       TRUE),
('pdi.fuentes.habilitadas',     'REGISTRO_CIVIL,INTERPOL', 'ms-pdi', TRUE),
('notaria.tipos.poder',         'PODER_SIMPLE,PODER_NOTARIAL,PODER_ESPECIAL', 'ms-notaria', TRUE),
('monitoreo.intervalo.segundos','30',              'ms-monitoreo', TRUE);

INSERT INTO cache_entry (clave, valor_cache, expira, hits) VALUES
('auth.token.expiracion',       '3600',            '2025-12-31', 142),
('auth.max.sesiones',           '5',               '2025-12-31', 98),
('proceso.pasos.activos',       'CHACALLUTA,COLCHANE,LOS_LIBERTADORES,PINO_HACHADO', '2025-12-31', 310),
('menores.edad.maxima',         '17',              '2025-12-31', 55),
('reporte.formato.default',     'PDF',             '2025-12-31', 87),
('sag.riesgo.niveles',          'BAJO,MEDIO,ALTO', '2025-12-31', 204),
('pdi.fuentes.habilitadas',     'REGISTRO_CIVIL,INTERPOL', '2025-12-31', 173),
('notaria.tipos.poder',         'PODER_SIMPLE,PODER_NOTARIAL,PODER_ESPECIAL', '2025-12-31', 66),
('monitoreo.intervalo.segundos','30',              '2025-12-31', 512);

INSERT INTO proyeccion_cache (clave, valor_cache, expira, ms_duenio, sincronizado) VALUES
('auth.token.expiracion',       '3600',            '2025-12-31', 'ms-auth',      '2025-01-05'),
('auth.max.sesiones',           '5',               '2025-12-31', 'ms-auth',      '2025-01-05'),
('proceso.pasos.activos',       'CHACALLUTA,COLCHANE,LOS_LIBERTADORES,PINO_HACHADO', '2025-12-31', 'ms-proceso',   '2025-01-05'),
('menores.edad.maxima',         '17',              '2025-12-31', 'ms-menores',   '2025-01-05'),
('reporte.formato.default',     'PDF',             '2025-12-31', 'ms-reporte',   '2025-01-05'),
('sag.riesgo.niveles',          'BAJO,MEDIO,ALTO', '2025-12-31', 'ms-sag',       '2025-01-05'),
('pdi.fuentes.habilitadas',     'REGISTRO_CIVIL,INTERPOL', '2025-12-31', 'ms-pdi',      '2025-01-05'),
('notaria.tipos.poder',         'PODER_SIMPLE,PODER_NOTARIAL,PODER_ESPECIAL', '2025-12-31', 'ms-notaria',   '2025-01-05'),
('monitoreo.intervalo.segundos','30',              '2025-12-31', 'ms-monitoreo', '2025-01-05');
