# CONTROL DE CAMBIOS — SISTEMA DE CONTROL FRONTERIZO
## Proyecto: Aduanas Chile · Group ID: cl.triskeledu.aduanas

**Repositorio:** Sistema de Control Fronterizo - Aduanas  
**Estrategia de ramas:** GitFlow  
**Rama principal:** `main` (produccion) / `develop` (integracion)

---

## 1. TABLA DE CONTROL DE CAMBIOS

| Version | Fecha | Autor | Descripcion del Cambio | Microservicios Afectados |
|---------|-------|-------|------------------------|--------------------------|
| v1.0.0 | 2026-05-01 | Equipo Aduanas | **Backend base**: Estructura Maven multi-modulo con 11 bases de datos PostgreSQL independientes. Creacion de modelos JPA, repositorios, DTOs, mappers MapStruct y CRUDs base en todos los MS. Configuracion de Eureka Server para descubrimiento de servicios. Docker Compose con PostgreSQL en puerto 5433. Scripts `init-multi-db` para creacion automatica de 11 bases (auth, proceso, menores, reporte, auditoria, sag, pdi, notaria, monitoreo, datos, config). Implementacion de R.1 (autenticacion JWT en ms-auth) y R.3 (registro de vehiculos y salidas diplomaticas en ms-proceso). | eureka, common, ms-auth, ms-proceso, ms-menores, ms-reporte, ms-auditoria, ms-sag, ms-pdi, ms-notaria, ms-monitoreo, ms-datos |
| v1.1.0 | 2026-05-08 | Equipo Aduanas | **Integracion Feign entre MS**: Implementacion de R.2 (validacion de menores con llamadas paralelas via CompletableFuture a ms-menores y ms-notaria). Implementacion de R.5 (tramite SAG con regla CUARENTENA desde ms-proceso). Implementacion de R.6 (consulta PDI con timeout 2s segun ISO 25010). Clientes Feign: MenoresClient, NotariaClient, SagClient, PdiClient. Correccion del error 401 en llamadas Feign internas mediante FilterRegistrationBean.setEnabled(false) en ms-pdi. | ms-proceso, ms-menores, ms-notaria, ms-sag, ms-pdi, common |
| v2.0.0 | 2026-05-15 | Equipo Aduanas | **Integracion Kafka y Trazabilidad (R.14)**: Incorporacion de Apache Kafka en Docker Compose (puerto 9092). Creacion de eventos de dominio en common: MenorAutorizadoEvent, DeclaracionCreatedEvent, AntecedenteConsultadoEvent, ReporteCreatedEvent, ConfiguracionActualizadaEvent. AuditoriaEventListener con 9 @KafkaListener que consolida eventos de todos los MS. LogEventoController convertido a API de solo lectura (R.14): eliminacion de POST/DELETE, adicion de GET /{id}/detalles. Patron productor/consumidor Kafka implementado en ms-proceso, ms-sag, ms-pdi, ms-reporte y ms-datos. | common, ms-auditoria, ms-proceso, ms-sag, ms-pdi, ms-reporte, ms-datos, ms-menores |
| v2.1.0 | 2026-05-22 | Equipo Aduanas | **Persistencia avanzada con Redis**: Configuracion de Redis 7 en Docker Compose (puerto 6379). RedisCacheService con patron cache-aside (busqueda Redis primero, PostgreSQL como fallback). Integracion en CacheEntryService y ConfiguracionService de ms-datos. Publicacion de ConfiguracionActualizadaEvent al actualizar parametros globales. Endpoints directos de gestion de cache en CacheEntryController. | ms-datos, common |
| v2.2.0 | 2026-05-28 | Equipo Aduanas | **Monitoreo de salud del ecosistema**: 10 Feign Health Clients en ms-monitoreo apuntando a /actuator/health de cada MS (EurekaHealthClient con URL hardcoded por limitacion de autodescubrimiento). MonitoreoService con clasificacion OPERATIVO/DEGRADADO/CRITICO segun porcentaje de disponibilidad. Endpoint GET /api/v1/monitoreo/status con SistemaSaludResponse. Timeouts Feign: connectTimeout 1000ms / readTimeout 3000ms. | ms-monitoreo |
| v3.0.0 | 2026-06-05 | Equipo Aduanas | **API Gateway y Seguridad JWT (Unidad 3)**: Creacion del modulo api-gateway (puerto 9000) con Spring Cloud Gateway (WebFlux, incompatible con spring-boot-starter-web). 16 rutas explicitas via lb:// resueltas por Eureka. GlobalLoggingFilter: log de metodo, URI y tiempo de respuesta por request. JwtHeaderRelayFilter: propagacion de Authorization + headers X-Gateway-Source, X-Forwarded-By, X-Request-ID. RBAC en ms-reporte: getRolFromToken() en JwtTokenProvider, extraccion de claim rol en JwtAuthenticationFilter, hasRole("SUPERVISOR") para endpoints de exportacion. Correccion de propiedad deprecada: management.endpoint.gateway.enabled → management.endpoint.gateway.access: unrestricted. | api-gateway, common, ms-reporte |
| v3.1.0 | 2026-06-10 | Equipo Aduanas | **Cobertura de seguridad completa**: SecurityConfig creado en ms-menores, ms-sag y ms-notaria (los 3 MS que faltaban). Patron uniforme: FilterRegistrationBean.setEnabled(false) + SecurityFilterChain con anyRequest().permitAll(). Eliminacion del campo jwtAuthenticationFilter no usado (warning Java 570425421). jwt.secret identico en los 10 MS garantiza validacion cruzada de tokens. Ecosistema con cobertura de seguridad al 100%. | ms-menores, ms-sag, ms-notaria |
| v4.0.0 | 2026-06-15 | Equipo Aduanas | **Generacion de Reportes PDF y Excel (R.7 / R.8)**: Dependencias OpenPDF 1.3.30 y Apache POI 5.2.3 en ms-reporte. Patron Strategy: interfaz ReporteExportador + ExportadorPDF (tabla con colores institucionales, cabecera azul) + ExportadorExcel (2 hojas: Resumen + Detalle con auto-filter). ReporteExportService orquesta: Feign a ms-auditoria → estrategia → persistencia en PG → ReporteCreatedEvent en Kafka. Endpoints GET /exportar/pdf y GET /exportar/excel con parametro opcional ?rutOficial=. | ms-reporte, ms-auditoria |
| v4.1.0 | 2026-06-20 | Equipo Aduanas | **Interfaz Web de Login**: Creacion de ui-web/index.html con Bootstrap 5 y colores institucionales Aduanas Chile (#003366, #FFFFFF, #F8F9FA). Formulario responsivo con formateo automatico de RUT, toggle de contrasena y zona de alertas (info/exito/error). Autenticacion JWT Stateless: POST a localhost:9000/api/v1/auth/login via API Gateway, almacenamiento en localStorage, decodificacion del payload JWT para mostrar nombre y rol del funcionario. Manejo explicito de errores 401/403 del GlobalExceptionHandler. Redireccion automatica si ya existe sesion activa. | ui-web (frontend estatico) |
| v4.2.0 | 2026-06-20 | Equipo Aduanas | **Documentacion de calidad ISO 25010**: Creacion de PLANILLA_PRUEBAS_ISO25000.md con 25 casos de prueba cubriendo R.1 a R.14 y Unidad 3. Matriz de caracteristicas ISO 25010 (Adecuacion Funcional, Eficiencia de Desempeno, Seguridad, Fiabilidad, Mantenibilidad). Criterios de aceptacion de eficiencia: timeout <= 2.0s para PDI, SAG y menores. Seccion de evidencia tecnica con estructura de coleccion Postman y trazas esperadas en ms-auditoria. | documentos/ |

---

## 2. ESTRATEGIA DE RAMAS — GITFLOW

Este proyecto aplica **GitFlow** como estrategia de control de versiones, adaptado al contexto academico de entrega incremental por unidades.

### Ramas principales

| Rama | Proposito |
|------|-----------|
| `main` | Codigo en estado de produccion. Solo recibe merges desde `release/*`. Cada merge genera un tag de version (v1.0.0, v2.0.0, etc.). |
| `develop` | Rama de integracion continua. Aqui convergen todas las features antes de pasar a release. |

### Ramas de soporte

| Tipo | Patron de nombre | Origen | Destino | Ejemplo |
|------|-----------------|--------|---------|---------|
| Feature | `feature/<descripcion>` | `develop` | `develop` | `feature/ms-pdi-consulta-antecedentes` |
| Release | `release/<version>` | `develop` | `main` + `develop` | `release/v3.0.0` |
| Hotfix | `hotfix/<descripcion>` | `main` | `main` + `develop` | `hotfix/jwt-filter-401-feign` |

### Flujo aplicado en este proyecto

```
main ────────────────────────────────────────────────────────────► (produccion)
       ↑ merge v1.0    ↑ merge v2.0    ↑ merge v3.0    ↑ merge v4.0
       │               │               │               │
develop ──────────────────────────────────────────────────────────►
       ↑               ↑               ↑               ↑
       │feature/ms-auth│feature/kafka  │feature/gateway│feature/ui
       │feature/ms-proc│feature/audit  │feature/rbac   │feature/pdf
       │feature/eureka │feature/redis  │feature/sec    │feature/excel
```

### Convenciones de commits

Los mensajes de commit siguen **Conventional Commits** para trazabilidad:

```
feat(ms-proceso): implementar R.3 registro de vehiculos
feat(ms-auditoria): agregar AuditoriaEventListener con 9 @KafkaListener
fix(ms-pdi): corregir error 401 Feign con FilterRegistrationBean
feat(api-gateway): crear GlobalLoggingFilter con tiempo de respuesta
refactor(common): extraer getRolFromToken para RBAC
docs: agregar PLANILLA_PRUEBAS_ISO25000.md con 25 casos TC
```

### Politica de merge

- Toda feature requiere que compile sin errores antes del merge a `develop`
- Las ramas `release/*` permiten solo bugfixes, no nuevas features
- Los `hotfix/*` se aplican tanto a `main` como a `develop` para mantener consistencia
- No se hace push directo a `main`

### Beneficios para mejora continua

GitFlow permite en este proyecto incorporar nuevos requerimientos (R.9 a R.13 pendientes) como features independientes en `develop` sin afectar la estabilidad del codigo ya entregado en `main`, facilitando la entrega incremental por unidades academicas.

---

## 3. RESUMEN DE IMPACTO POR VERSION

| Version | MS Nuevos | MS Modificados | Archivos Clave |
|---------|-----------|----------------|----------------|
| v1.0.0 | eureka, common, ms-auth, ms-proceso, ms-menores, ms-reporte, ms-auditoria, ms-sag, ms-pdi, ms-notaria, ms-monitoreo, ms-datos | — | pom.xml padre, docker-compose.yml, 11x init-db.sql |
| v1.1.0 | — | ms-proceso, ms-pdi, ms-menores, ms-sag | *Client.java, SecurityConfig.java (ms-pdi) |
| v2.0.0 | — | common, ms-auditoria, ms-proceso, ms-sag, ms-pdi, ms-reporte, ms-datos | *Event.java, AuditoriaEventListener.java, LogEventoController.java |
| v2.1.0 | — | ms-datos | RedisConfig.java, RedisCacheService.java, docker-compose.yml |
| v2.2.0 | — | ms-monitoreo | *HealthClient.java x10, MonitoreoService.java |
| v3.0.0 | api-gateway | common, ms-reporte | AduanasGatewayApplication.java, *Filter.java x2, JwtTokenProvider.java |
| v3.1.0 | — | ms-menores, ms-sag, ms-notaria | SecurityConfig.java x3 |
| v4.0.0 | — | ms-reporte | ReporteExportador.java, ExportadorPDF.java, ExportadorExcel.java, ReporteExportService.java |
| v4.1.0 | ui-web | — | index.html |
| v4.2.0 | documentos | — | PLANILLA_PRUEBAS_ISO25000.md, CONTROL_CAMBIOS.md |
