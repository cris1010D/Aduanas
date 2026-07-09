-- ═══════════════════════════════════════════════════════════════════════
-- Migración: campos extendidos de Viajero + tabla declaracion_aduana
-- Base de datos de ms-proceso
-- ═══════════════════════════════════════════════════════════════════════

-- Ampliar tabla viajero con documento de viaje, contacto, logística,
-- contacto de emergencia y seguro médico.
ALTER TABLE viajero
    ADD COLUMN IF NOT EXISTS tipo_documento              VARCHAR(20),
    ADD COLUMN IF NOT EXISTS pais_emisor_documento        VARCHAR(40),
    ADD COLUMN IF NOT EXISTS fecha_emision_documento      DATE,
    ADD COLUMN IF NOT EXISTS fecha_vencimiento_documento  DATE,
    ADD COLUMN IF NOT EXISTS email                        VARCHAR(100),
    ADD COLUMN IF NOT EXISTS telefono                     VARCHAR(25),
    ADD COLUMN IF NOT EXISTS direccion_estadia            VARCHAR(160),
    ADD COLUMN IF NOT EXISTS proposito_viaje              VARCHAR(20),
    ADD COLUMN IF NOT EXISTS emergencia_nombre            VARCHAR(80),
    ADD COLUMN IF NOT EXISTS emergencia_parentesco        VARCHAR(40),
    ADD COLUMN IF NOT EXISTS emergencia_telefono          VARCHAR(25),
    ADD COLUMN IF NOT EXISTS seguro_vigente                BOOLEAN,
    ADD COLUMN IF NOT EXISTS seguro_proveedor             VARCHAR(80);

-- Nueva tabla: declaración aduanera (dinero, mercancías, franquicias)
CREATE TABLE IF NOT EXISTS declaracion_aduana (
                                                  id                      SERIAL PRIMARY KEY,
                                                  rut_viajero             VARCHAR(12)   NOT NULL,
    fecha                   DATE          NOT NULL,
    paso_fronterizo         VARCHAR(40)   NOT NULL,
    porta_dinero_efectivo   BOOLEAN       NOT NULL,
    monto_dinero            NUMERIC(12,2),
    moneda_dinero           VARCHAR(10),
    mercancias_afectas      BOOLEAN       NOT NULL,
    descripcion_mercancias  VARCHAR(200),
    litros_alcohol          DOUBLE PRECISION,
    cantidad_cigarrillos    INTEGER,
    excede_franquicia       BOOLEAN       NOT NULL,
    estado                  VARCHAR(15)   NOT NULL
    );

CREATE INDEX IF NOT EXISTS idx_declaracion_aduana_rut ON declaracion_aduana(rut_viajero);