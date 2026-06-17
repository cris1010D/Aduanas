-- ============================================================
-- 01-init.sql
-- Crea las 10 bases de datos del Sistema de Control Fronterizo
-- Ejecutar conectado a la base 'postgres' como superusuario
-- ============================================================

SELECT 'CREATE DATABASE auth'      WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'auth')      \gexec
SELECT 'CREATE DATABASE proceso'   WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'proceso')   \gexec
SELECT 'CREATE DATABASE menores'   WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'menores')   \gexec
SELECT 'CREATE DATABASE reporte'   WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'reporte')   \gexec
SELECT 'CREATE DATABASE auditoria' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'auditoria') \gexec
SELECT 'CREATE DATABASE sag'       WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'sag')       \gexec
SELECT 'CREATE DATABASE pdi'       WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'pdi')       \gexec
SELECT 'CREATE DATABASE notaria'   WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'notaria')   \gexec
SELECT 'CREATE DATABASE monitoreo' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'monitoreo') \gexec
SELECT 'CREATE DATABASE datos'     WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'datos')     \gexec
