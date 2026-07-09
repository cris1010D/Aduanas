# PROMPT COMPLETO — SISTEMA DE CONTROL FRONTERIZO (ADUANAS)
# Para Claude Code — Replica exacta de la estructura del proyecto ShopCore (multi-tienda)

---

## ROL

Actua como experto en Arquitectura de Microservicios y Spring Boot.
Implementaras el "Sistema de Control Fronterizo" bajo el mismo patron profesional
del proyecto ShopCore (multi-tienda). La estructura de carpetas, capas, patrones
de codigo, scripts .bat y configuraciones deben ser identicos al proyecto de
referencia, solo cambiando los nombres de dominio (de tienda/cliente a aduanas/viajero).

---

## 1. INFORMACION GENERAL DEL PROYECTO

| Campo               | Valor                                    |
|---------------------|------------------------------------------|
| Type                | maven-project                            |
| Language            | java                                     |
| Package             | jar                                      |
| Group Id            | cl.triskeledu.aduanas                    |
| Parent Artifact     | Aduanas                                  |
| Folder              | C:\Aduanas                               |
| Spring Boot Version | 3.5.0                                    |
| Spring Cloud        | 2024.0.1                                 |
| Java Version        | 21                                       |
| Project Name        | Aduanas                                  |
| Project Description | Software de Control Fronterizo - Aduanas |

---

## 2. STACK TECNICO (identico al proyecto de referencia)

- **Java 21** + **Spring Boot 3.5.0** + **Spring Cloud 2024.0.1**
- **Maven multi-modulo**: un `pom.xml` padre + un modulo por cada MS
- **MapStruct 1.5.5.Final** para mapeo de DTOs (igual que en referencia)
- **Lombok 1.18.44**
- **PostgreSQL** en Docker, puerto **5433**
- **H2** como dependencia (igual que referencia, para compatibilidad de tests)
- **Kafka** (spring-kafka) para eventos asincronicos
- **Feign** (spring-cloud-starter-openfeign) para comunicacion sincronica
- **Eureka** (netflix-eureka-server / client) para descubrimiento
- **Spring Actuator** en todos los MS
- **Hibernate Validator 8.0.1.Final**
- **Modulo `common`**: libreria compartida con excepciones, eventos y auto-configuracion

---

## 3. ESTRUCTURA DE MODULOS (pom.xml padre)

```xml
<modules>
    <module>eureka</module>
    <module>common</module>
    <module>ms-auth</module>
    <module>ms-proceso</module>
    <module>ms-menores</module>
    <module>ms-reporte</module>
    <module>ms-auditoria</module>
    <module>ms-sag</module>
    <module>ms-pdi</module>
    <module>ms-notaria</module>
    <module>ms-monitoreo</module>
    <module>ms-datos</module>
</modules>

<groupId>cl.triskeledu.aduanas</groupId>
<artifactId>Aduanas</artifactId>
<version>0.0.1-SNAPSHOT</version>
<packaging>pom</packaging>
```

---

## 4. MICROSERVICIOS Y PUERTOS

| Modulo        | Puerto | Rol                                              |
|:---           |:---    |:---                                              |
| eureka        | 8761   | Servidor de descubrimiento                       |
| ms-auth       | 9001   | Seguridad RBAC y sesiones de oficiales           |
| ms-proceso    | 9002   | Orquestacion de flujos (admision, vehiculos)     |
| ms-menores    | 9003   | Validacion de permisos judiciales/notariales     |
| ms-reporte    | 9004   | Generacion de documentos PDF/Excel               |
| ms-auditoria  | 9005   | Logs inmutables de cada movimiento fronterizo    |
| ms-sag        | 9006   | Conector para declaraciones sanitarias/agricolas |
| ms-pdi        | 9007   | Conector para validacion de antecedentes penales |
| ms-notaria    | 9008   | Conector para verificar poderes legales          |
| ms-monitoreo  | 9009   | Salud centralizada de los MS via Actuator        |
| ms-datos      | 9010   | Gestion de persistencia y cache distribuida      |

---

## 5. REGLAS DE NEGOCIO

- Maximo **3 tablas** por microservicio, maximo **5 campos** por tabla
- Cada MS tiene **1 tabla de proyeccion** para sincronizacion via Kafka
- Tablas de proyeccion usan **claves alternas** (rut, nombre_ms, clave) como PK — NUNCA IDs numericos
- **Regla de los 9**: minimo **9 registros reales** por tabla en los INSERT de poblamiento
- Todo en **minusculas**, sin acentos ni letra "n con tilde"
- Fechas en formato **ISO (YYYY-MM-DD)**
- **ISO 25010**: tiempo de respuesta <= 2s (p95), disponibilidad 99.9%

---

## 6. ESTRUCTURA DE CARPETAS (replica exacta de multi-tienda)

```
C:\Aduanas\
|-- pom.xml                         <- POM padre multi-modulo
|-- compile.bat
|-- install.bat
|-- run-all.bat
|-- run-eureka.bat
|-- run-auth.bat
|-- run-proceso.bat
|-- run-menores.bat
|-- run-reporte.bat
|-- run-auditoria.bat
|-- run-sag.bat
|-- run-pdi.bat
|-- run-notaria.bat
|-- run-monitoreo.bat
|-- run-datos.bat
|-- .env
|-- .vscode/
|   |-- launch.json
|   `-- settings.json
|-- init-multi-db/
|   |-- 01-init.sql
|   |-- 02-create_auth.sql
|   |-- 03-create_proceso.sql
|   |-- 04-create_menores.sql
|   |-- 05-create_reporte.sql
|   |-- 06-create_auditoria.sql
|   |-- 07-create_sag.sql
|   |-- 08-create_pdi.sql
|   |-- 09-create_notaria.sql
|   |-- 10-create_monitoreo.sql
|   `-- 11-create_datos.sql
|-- eureka/
|   |-- pom.xml
|   `-- src/main/
|       |-- java/cl/triskeledu/aduanas/eureka/
|       |   `-- AduanasEurekaApplication.java
|       `-- resources/application.yml
|-- common/
|   |-- pom.xml
|   `-- src/main/java/cl/triskeledu/common/
|       |-- CommonAutoConfiguration.java
|       |-- exception/
|       |   |-- EntityNotFoundException.java
|       |   |-- DuplicateResourceException.java
|       |   `-- ReferentialIntegrityException.java
|       |-- handler/
|       |   `-- GlobalExceptionHandler.java
|       |-- dto/
|       |   `-- ApiError.java
|       `-- event/
|           |-- DomainEvent.java
|           |-- OficialCreatedEvent.java
|           |-- OficialUpdatedEvent.java
|           |-- OficialDeletedEvent.java
|           |-- ViajeroCreatedEvent.java
|           |-- ViajeroUpdatedEvent.java
|           `-- ViajeroDeletedEvent.java
`-- ms-auth/                        <- Patron repetido en cada MS
    |-- pom.xml
    `-- src/main/
        |-- java/cl/triskeledu/aduanas/auth/
        |   |-- AduanasAuthApplication.java
        |   |-- client/             <- Feign Clients hacia otros MS
        |   |-- config/
        |   |   `-- KafkaTopicConfig.java
        |   |-- controller/
        |   |-- dto/
        |   |   |-- *Request.java
        |   |   `-- *Response.java
        |   |-- event/
        |   |   `-- *EventProducer.java
        |   |-- mapper/             <- MapStruct interfaces
        |   |-- model/              <- Entidades JPA (@Entity)
        |   |-- repository/         <- Interfaces JpaRepository
        |   `-- service/
        `-- resources/
            `-- application.yml
```

---

## 7. POM.XML PADRE (estructura completa)

```xml
<project>
  <groupId>cl.triskeledu.aduanas</groupId>
  <artifactId>Aduanas</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>pom</packaging>

  <properties>
    <java.version>21</java.version>
    <spring-boot.version>3.5.0</spring-boot.version>
    <spring-cloud.version>2024.0.1</spring-cloud.version>
    <mapstruct.version>1.5.5.Final</mapstruct.version>
    <lombok.version>1.18.44</lombok.version>
    <lombok-mapstruct-binding.version>0.2.0</lombok-mapstruct-binding.version>
    <postgresql-connector-j.version>42.7.3</postgresql-connector-j.version>
    <com-h2database.version>2.3.232</com-h2database.version>
    <hibernate-validator.version>8.0.1.Final</hibernate-validator.version>
    <jakarta.el.version>4.0.2</jakarta.el.version>
    <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
    <maven-clean-plugin.version>3.3.1</maven-clean-plugin.version>
    <spring-kafka.version>${spring-boot.version}</spring-kafka.version>
  </properties>

  <!-- dependencyManagement: spring-boot-dependencies BOM,
       spring-cloud-dependencies BOM, postgresql, h2, mapstruct,
       mapstruct-processor, lombok, spring-boot-starter-validation,
       hibernate-validator, jakarta.el, ms-common, spring-kafka -->

  <!-- build/plugins: maven-compiler-plugin con annotationProcessorPaths
       (mapstruct-processor, lombok, lombok-mapstruct-binding),
       maven-clean-plugin, spring-boot-maven-plugin -->
</project>
```

---

## 8. MODULO COMMON (libreria compartida)

### CommonAutoConfiguration.java
```java
@AutoConfiguration
@ComponentScan(basePackages = "cl.triskeledu.common")
public class CommonAutoConfiguration { }
```

Registrar en: `src/main/resources/META-INF/spring/
org.springframework.boot.autoconfigure.AutoConfiguration.imports`

### Excepciones requeridas
```java
// EntityNotFoundException
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity, String field, Object value) {
        super(String.format("No se encontro %s con %s: '%s'", entity, field, value));
    }
}

// DuplicateResourceException
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String entity, String field, Object value, Object detail) {
        super(String.format("Ya existe un registro de %s con %s: '%s'", entity, field, value));
    }
}

// ReferentialIntegrityException (igual que en referencia)
```

### GlobalExceptionHandler
Maneja: `EntityNotFoundException` (404), `DuplicateResourceException` (409),
`DataIntegrityViolationException` (409), `MethodArgumentNotValidException` (400),
`NoResourceFoundException` (404), `Exception` (500).
Responde con `ApiError` (timestamp, status, error, message, errors, path).

### Eventos de dominio en common/event/
Seguir el mismo patron que en referencia:
- Interfaz `DomainEvent` con metodo `getAggregateId()`
- Un evento por operacion CRUD por entidad principal:
  - `OficialCreatedEvent`, `OficialUpdatedEvent`, `OficialDeletedEvent`
  - `ViajeroCreatedEvent`, `ViajeroUpdatedEvent`, `ViajeroDeletedEvent`
  - `MenorCreatedEvent`, `DeclaracionCreatedEvent`, `AntecedentCreatedEvent`
  - etc. (uno por entidad principal de cada MS)
- Usar `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder`

---

## 9. PATRON DE CADA MICROSERVICIO (basado en ms-clientes de referencia)

### 9.1 pom.xml del MS
```xml
<parent>
  <groupId>cl.triskeledu.aduanas</groupId>
  <artifactId>Aduanas</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <relativePath>../pom.xml</relativePath>
</parent>

<groupId>cl.triskeledu.aduanas</groupId>
<artifactId>cl.triskeledu.aduanas-{nombre}</artifactId>
<version>0.0.1-SNAPSHOT</version>

<!-- Dependencias: ms-common, spring-boot-starter-web, spring-boot-starter-data-jpa,
     postgresql (runtime), h2, spring-boot-starter-validation, spring-boot-starter-actuator,
     lombok (provided), mapstruct, mapstruct-processor, spring-boot-starter-test (test),
     spring-cloud-starter-netflix-eureka-client, spring-cloud-starter-openfeign,
     spring-boot-devtools (optional), spring-kafka -->

<!-- build/plugins: spring-boot-maven-plugin con mainClass,
     maven-compiler-plugin con annotationProcessorPaths -->
```

### 9.2 Application.java
```java
package cl.triskeledu.aduanas.{nombre};

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Aduanas{Nombre}Application {
    public static void main(String[] args) {
        SpringApplication.run(Aduanas{Nombre}Application.class, args);
    }
}
```

### 9.3 application.yml (patron de ms-clientes, adaptado)
```yaml
server:
  port: 900X
  error:
    include-stacktrace: never

spring:
  application:
    name: ms-{nombre}
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5433/{nombre_db}
    username: postgres
    password: 123
  jpa:
    hibernate:
      ddl-auto: update
    database: postgresql
    open-in-view: false
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      group-id: ${spring.application.name}
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        "[spring.json.trusted.packages]": "cl.triskeledu.*"
        "[spring.json.type.mapping]": "oficialCreated:cl.triskeledu.common.event.OficialCreatedEvent"

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true
    instance-id: "${spring.application.name}:${random.value}"
    lease-renewal-interval-in-seconds: 15
    lease-expiration-duration-in-seconds: 60

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
  info:
    env:
      enabled: true

info:
  app:
    name: ms-{nombre}
    version: 0.0.1
    description: Microservicio de {nombre} - Aduanas
```

### 9.4 Capas (identicas a la referencia)

**model/** — Entidades JPA
```java
@Entity
@Table(name = "oficial")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Oficial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 30)
    private String rol;

    @Column(nullable = false)
    private Boolean activo;
}
```

**repository/** — Interfaces JpaRepository
```java
@Repository
public interface OficialRepository extends JpaRepository<Oficial, Integer> {
    Optional<Oficial> findByRut(String rut);
    List<Oficial> findByActivoTrue();
    boolean existsByRut(String rut);
    List<Oficial> findAllByOrderByIdAsc();
}
```

**dto/** — Request y Response con validaciones
```java
// Request con @NotBlank, @Size, @Email, @NotNull segun corresponda
// Response con @JsonIgnoreProperties(ignoreUnknown = true) donde aplique
```

**mapper/** — Interfaces MapStruct
```java
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OficialMapper {
    OficialResponse toResponse(Oficial oficial);
    List<OficialResponse> toResponseList(List<Oficial> oficiales);
    @Mapping(target = "id", ignore = true)
    Oficial toEntity(OficialRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEntity(OficialRequest request, @MappingTarget Oficial oficial);
}
```

**config/KafkaTopicConfig.java**
```java
@Configuration
public class KafkaTopicConfig {
    public static final String OFICIAL_CREATED_TOPIC = "auth.oficial.created";
    public static final String OFICIAL_UPDATED_TOPIC = "auth.oficial.updated";
    public static final String OFICIAL_DELETED_TOPIC = "auth.oficial.deleted";

    @Bean public NewTopic oficialCreatedTopic() {
        return TopicBuilder.name(OFICIAL_CREATED_TOPIC).partitions(1).replicas(1).build();
    }
    // idem para updated y deleted
}
```

**event/*EventProducer.java**
```java
@Slf4j
@Component
@RequiredArgsConstructor
public class OficialEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOficialCreated(OficialCreatedEvent event) {
        log.info("Enviando OficialCreatedEvent para oficial rut: {}", event.getRut());
        kafkaTemplate.send(KafkaTopicConfig.OFICIAL_CREATED_TOPIC,
                event.getRut(), event);
        log.info("OficialCreatedEvent enviado correctamente");
    }
    // idem para updated y deleted
}
```

**client/** — Feign Clients
```java
@FeignClient(name = "ms-auth", path = "/api/v1")
public interface AuthClient {
    @GetMapping("/oficiales/rut/{rut}")
    OficialResponse findByRut(@PathVariable("rut") String rut);
}
```

**service/** — Logica de negocio con @Slf4j, @Service, @Transactional, @RequiredArgsConstructor
- Metodos privados de validacion (getById, validateRutUnico, validateExisteEnOtroMS via Feign)
- CRUD completo: listarTodos, listarActivos, buscarPorId, buscarPorRut, crear, actualizar, eliminar
- Publicar eventos Kafka en crear, actualizar, eliminar
- Validaciones via Feign antes de crear/actualizar/eliminar

**controller/** — REST con @RestController, @RequestMapping("/api/v1/{entidad}"), @RequiredArgsConstructor
- GET /api/v1/{entidad}             → listarTodos (200)
- GET /api/v1/{entidad}/{id}        → buscarPorId (200)
- GET /api/v1/{entidad}/activos     → listarActivos (200)
- GET /api/v1/{entidad}/rut/{rut}   → buscarPorRut (200)
- POST /api/v1/{entidad}            → crear (201)
- PUT /api/v1/{entidad}/{id}        → actualizar (200)
- DELETE /api/v1/{entidad}/{id}     → eliminar (204)

---

## 10. ESQUEMA DE BASE DE DATOS POR MICROSERVICIO

### MS: AUTH (DB: auth) — Puerto 9001
```sql
CREATE TABLE oficial (
    id      SERIAL PRIMARY KEY,
    rut     VARCHAR(12) UNIQUE NOT NULL,
    nombre  VARCHAR(80) NOT NULL,
    rol     VARCHAR(30) NOT NULL,
    activo  BOOLEAN DEFAULT TRUE
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
```

### MS: PROCESO (DB: proceso) — Puerto 9002
```sql
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
```

### MS: MENORES (DB: menores) — Puerto 9003
```sql
CREATE TABLE menor (
    id        SERIAL PRIMARY KEY,
    rut       VARCHAR(12) UNIQUE NOT NULL,
    nombre    VARCHAR(80) NOT NULL,
    fecha_nac DATE NOT NULL,
    rut_tutor VARCHAR(12) NOT NULL
);
CREATE TABLE autorizacion (
    id             SERIAL PRIMARY KEY,
    rut_menor      VARCHAR(12) NOT NULL REFERENCES menor(rut),
    tipo           VARCHAR(30) NOT NULL,
    vigencia       DATE NOT NULL,
    notaria_origen VARCHAR(60) NOT NULL
);
CREATE TABLE proyeccion_permiso (
    rut_menor      VARCHAR(12) PRIMARY KEY,
    tipo           VARCHAR(30) NOT NULL,
    vigencia       DATE NOT NULL,
    notaria_origen VARCHAR(60) NOT NULL,
    sincronizado   DATE NOT NULL
);
```

### MS: REPORTE (DB: reporte) — Puerto 9004
```sql
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
```

### MS: AUDITORIA (DB: auditoria) — Puerto 9005
```sql
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
```

### MS: SAG (DB: sag) — Puerto 9006
```sql
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
```

### MS: PDI (DB: pdi) — Puerto 9007
```sql
CREATE TABLE antecedente (
    id             SERIAL PRIMARY KEY,
    rut            VARCHAR(12) UNIQUE NOT NULL,
    resultado      VARCHAR(20) NOT NULL,
    fecha_consulta DATE NOT NULL,
    fuente         VARCHAR(40) NOT NULL
);
CREATE TABLE consulta (
    id          SERIAL PRIMARY KEY,
    rut         VARCHAR(12) NOT NULL REFERENCES antecedente(rut),
    rut_oficial VARCHAR(12) NOT NULL,
    fecha       DATE NOT NULL,
    motivo      VARCHAR(60) NOT NULL
);
CREATE TABLE proyeccion_antecedente (
    rut            VARCHAR(12) PRIMARY KEY,
    resultado      VARCHAR(20) NOT NULL,
    fecha_consulta DATE NOT NULL,
    fuente         VARCHAR(40) NOT NULL,
    sincronizado   DATE NOT NULL
);
```

### MS: NOTARIA (DB: notaria) — Puerto 9008
```sql
CREATE TABLE poder (
    id             SERIAL PRIMARY KEY,
    rut_titular    VARCHAR(12) NOT NULL,
    rut_apoderado  VARCHAR(12) NOT NULL,
    notaria_origen VARCHAR(60) NOT NULL,
    vigencia       DATE NOT NULL
);
CREATE TABLE documento (
    id            SERIAL PRIMARY KEY,
    id_poder      INT NOT NULL REFERENCES poder(id),
    tipo          VARCHAR(30) NOT NULL,
    folio         VARCHAR(20) UNIQUE NOT NULL,
    fecha_emision DATE NOT NULL
);
CREATE TABLE proyeccion_poder (
    rut_titular    VARCHAR(12) PRIMARY KEY,
    rut_apoderado  VARCHAR(12) NOT NULL,
    vigencia       DATE NOT NULL,
    notaria_origen VARCHAR(60) NOT NULL,
    sincronizado   DATE NOT NULL
);
```

### MS: MONITOREO (DB: monitoreo) — Puerto 9009
```sql
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
```

### MS: DATOS (DB: datos) — Puerto 9010
```sql
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
```

---

## 11. SCRIPTS .BAT (replica exacta de multi-tienda)

### compile.bat
```bat
@echo off
echo.
echo === COMPILANDO MICROSERVICIOS ===
echo.
call cd C:\Aduanas\ms-auth
call mvn clean install -U
call cd C:\Aduanas\ms-proceso
call mvn clean install -U
call cd C:\Aduanas\ms-menores
call mvn clean install -U
call cd C:\Aduanas\ms-reporte
call mvn clean install -U
call cd C:\Aduanas\ms-auditoria
call mvn clean install -U
call cd C:\Aduanas\ms-sag
call mvn clean install -U
call cd C:\Aduanas\ms-pdi
call mvn clean install -U
call cd C:\Aduanas\ms-notaria
call mvn clean install -U
call cd C:\Aduanas\ms-monitoreo
call mvn clean install -U
call cd C:\Aduanas\ms-datos
call mvn clean install -U
echo.
echo === COMPILACION COMPLETADA ===
pause
```

### install.bat
```bat
@echo off
echo.
echo === REINSTALACION DE DEPENDENCIAS MAVEN ===
echo.
echo Eliminando carpeta .m2 ...
rmdir /s /q %USERPROFILE%\.m2
echo Eliminando carpetas target ...
rmdir /s /q C:\Aduanas\eureka\target
rmdir /s /q C:\Aduanas\common\target
rmdir /s /q C:\Aduanas\ms-auth\target
rmdir /s /q C:\Aduanas\ms-proceso\target
rmdir /s /q C:\Aduanas\ms-menores\target
rmdir /s /q C:\Aduanas\ms-reporte\target
rmdir /s /q C:\Aduanas\ms-auditoria\target
rmdir /s /q C:\Aduanas\ms-sag\target
rmdir /s /q C:\Aduanas\ms-pdi\target
rmdir /s /q C:\Aduanas\ms-notaria\target
rmdir /s /q C:\Aduanas\ms-monitoreo\target
rmdir /s /q C:\Aduanas\ms-datos\target
echo Descargando dependencias nuevamente con Maven ...
mvn clean install -U -DskipTests
echo.
echo === PROCESO COMPLETADO ===
pause
```

### run-all.bat
```bat
@echo off
echo ===== Iniciando Eureka Server =====
start "eureka" mvn -f eureka spring-boot:run
timeout /t 5 /nobreak > nul
echo ===== Iniciando Microservicios =====
start "ms-auth"      mvn -f ms-auth      spring-boot:run
start "ms-proceso"   mvn -f ms-proceso   spring-boot:run
start "ms-menores"   mvn -f ms-menores   spring-boot:run
start "ms-reporte"   mvn -f ms-reporte   spring-boot:run
start "ms-auditoria" mvn -f ms-auditoria spring-boot:run
start "ms-sag"       mvn -f ms-sag       spring-boot:run
start "ms-pdi"       mvn -f ms-pdi       spring-boot:run
start "ms-notaria"   mvn -f ms-notaria   spring-boot:run
start "ms-monitoreo" mvn -f ms-monitoreo spring-boot:run
start "ms-datos"     mvn -f ms-datos     spring-boot:run
echo Todos los servicios han sido lanzados.
```

### run-{nombre}.bat (uno por MS)
```bat
mvn -f ms-{nombre} spring-boot:run
```

---

## 12. VSCODE launch.json (replica de multi-tienda)

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Debug Eureka",
      "request": "launch",
      "cwd": "${workspaceFolder}/eureka",
      "mainClass": "cl.triskeledu.aduanas.eureka.AduanasEurekaApplication",
      "projectName": "cl-triskeledu-aduanas-eureka",
      "envFile": "${workspaceFolder}/.env"
    },
    {
      "type": "java",
      "name": "Debug ms-auth",
      "request": "launch",
      "cwd": "${workspaceFolder}/ms-auth",
      "mainClass": "cl.triskeledu.aduanas.auth.AduanasAuthApplication",
      "projectName": "cl-triskeledu-aduanas-auth",
      "envFile": "${workspaceFolder}/.env"
    },
    {
      "type": "java",
      "name": "Debug ms-proceso",
      "request": "launch",
      "cwd": "${workspaceFolder}/ms-proceso",
      "mainClass": "cl.triskeledu.aduanas.proceso.AduanasProcesoApplication",
      "projectName": "cl-triskeledu-aduanas-proceso",
      "envFile": "${workspaceFolder}/.env"
    }
    // ... idem para los demas MS
  ],
  "compounds": [
    {
      "name": "Debug All Services",
      "configurations": [
        "Debug Eureka", "Debug ms-auth", "Debug ms-proceso",
        "Debug ms-menores", "Debug ms-reporte", "Debug ms-auditoria",
        "Debug ms-sag", "Debug ms-pdi", "Debug ms-notaria",
        "Debug ms-monitoreo", "Debug ms-datos"
      ]
    }
  ]
}
```

---

## 13. init-multi-db (scripts SQL)

### 01-init.sql — Crear las 10 BDs si no existen
```sql
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
```

### 02..11 — Un archivo por MS con:
1. `\c {nombre_db}`
2. `DROP TABLE IF EXISTS ... CASCADE` (en orden inverso de FK)
3. `CREATE TABLE ...` con PK, FK, constraints
4. `INSERT INTO ...` con **minimo 9 registros reales** por tabla, fechas ISO (YYYY-MM-DD)

---

## 14. NOMENCLATURA DE CLASES (regla mandatoria)

Sin guiones en nombres Java. Usar CamelCase estricto:

| Modulo         | Clase principal                        |
|----------------|----------------------------------------|
| eureka         | `AduanasEurekaApplication`             |
| ms-auth        | `AduanasAuthApplication`               |
| ms-proceso     | `AduanasProcesoApplication`            |
| ms-menores     | `AduanasMenoresApplication`            |
| ms-reporte     | `AduanasReporteApplication`            |
| ms-auditoria   | `AduanasAuditoriaApplication`          |
| ms-sag         | `AduanasSagApplication`                |
| ms-pdi         | `AduanasPdiApplication`                |
| ms-notaria     | `AduanasNotariaApplication`            |
| ms-monitoreo   | `AduanasMonitoreoApplication`          |
| ms-datos       | `AduanasDatosApplication`              |

Package base de cada MS: `cl.triskeledu.aduanas.{nombre}`

---

## 15. RESTRICCIONES FINALES

- NO usar guiones en nombres de clases o paquetes Java
- NO usar IDs numericos como PK en tablas de proyeccion
- NO usar puerto 5432 — siempre **5433**
- `ms-monitoreo` NO lleva `data-jpa` ni `postgresql` ni `spring-kafka`; solo Actuator + Feign
- Minimo **9 registros** por tabla en los INSERT (Regla de los 9 del profesor)
- `ddl-auto: update` en todos los MS (no `validate`, para que JPA cree las tablas)
- `common` debe tener `spring-boot-maven-plugin` con `<skip>true</skip>`
- Todos los MS deben tener `@EnableDiscoveryClient` y `@EnableFeignClients`
- Todos los archivos `.bat` deben ser funcionales y replicar exactamente el estilo de multi-tienda
- Todo en **minusculas**, sin acentos, sin "n con tilde", fechas ISO
