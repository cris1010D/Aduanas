package cl.triskeledu.common.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import java.util.Collections;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Manejador global de excepciones para toda la API REST.
 *
 * @RestControllerAdvice intercepta todas las excepciones no capturadas en los
 * controladores y las transforma en respuestas HTTP coherentes con el envelope
 * ApiError. Esto desacopla la lógica de manejo de errores de los controladores,
 * centralizando la responsabilidad en un único punto (principio DRY + SRP).
 *
 * Orden de prioridad de los handlers: Spring usa el más específico primero.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
 
    // 404 - Entidad no encontrada
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(
            EntityNotFoundException ex, HttpServletRequest request) {
 
        log.warn("Entidad no encontrada: {} | ruta: {}", ex.getMessage(), request.getRequestURI());
 
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .errors(null)
                .path(request.getRequestURI())
                .build()
        );
    }
 
    // 409 - Recurso duplicado
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicate(
            DuplicateResourceException ex, HttpServletRequest request) {
 
        log.warn("Recurso duplicado: {} | ruta: {}", ex.getMessage(), request.getRequestURI());
 
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .errors(null)
                .path(request.getRequestURI())
                .build()
        );
    }
 
    // 409 - Integridad referencial
    @ExceptionHandler(ReferentialIntegrityException.class)
    public ResponseEntity<ApiError> handleReferentialIntegrity(
            ReferentialIntegrityException ex, HttpServletRequest request) {
 
        log.warn("Integridad referencial violada: {} | ruta: {}", ex.getMessage(), request.getRequestURI());
 
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .errors(null)
                .path(request.getRequestURI())
                .build()
        );
    }

    // 409 - Errores fisicos de integridad en base de datos
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(
            org.springframework.dao.DataIntegrityViolationException ex, HttpServletRequest request) {

        log.error("Violacion de integridad en BD detectada: {} | ruta: {}", ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message("No se puede eliminar o modificar el recurso porque tiene registros asociados activos en la base de datos.")
                .errors(null)
                .path(request.getRequestURI())
                .build()
        );
    }

    // ============================================================
    // MANEJO DE EXCEPCIONES DE SEGURIDAD (NORMA ISO 25010)
    // ============================================================

    /**
     * Captura fallas de autenticación (Token ausente, inválido, expirado o credenciales erróneas).
     * Retorna HTTP 401 Unauthorized usando el ApiError corporativo.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("No Autorizado")
                .message("Credenciales ausentes, vencidas o no válidas para el sistema de Aduanas.")
                .path(request.getRequestURI())
                .errors(Collections.emptyList()) // Lista vacía ya que no es un error de validación de campos
                .build();
                
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Captura denegaciones de acceso (El usuario está autenticado pero no posee el rol o permiso).
     * Retorna HTTP 403 Forbidden garantizando el principio de mínimo privilegio.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Acceso Denegado")
                .message("Su cuenta no dispone de los privilegios requeridos para ejecutar esta acción en Aduanas.")
                .path(request.getRequestURI())
                .errors(Collections.emptyList()) // Lista vacía ya que no es un error de validación de campos
                .build();
                
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
 
    // 400 - Errores de validacion (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
 
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
 
        log.warn("Error de validacion en ruta: {} | campos: {}", request.getRequestURI(), errores);
 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Error de validacion en los campos")
                .errors(errores)
                .path(request.getRequestURI())
                .build()
        );
    }

    // 404 - Endpoint no encontrado
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFound(
            NoResourceFoundException ex, HttpServletRequest request) {

        log.warn("Endpoint no encontrado: {}", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message("El endpoint solicitado no existe")
                .errors(null)
                .path(request.getRequestURI())
                .build()
        );
    }
 
    // 500 - Error generico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(
            Exception ex, HttpServletRequest request) {
 
        log.error("Error interno inesperado en ruta: {} | mensaje: {}", 
                  request.getRequestURI(), ex.getMessage(), ex);
 
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Ocurrio un error interno inesperado. Por favor, intente mas tarde.")
                .errors(null)
                .path(request.getRequestURI())
                .build()
        );
    }
}
