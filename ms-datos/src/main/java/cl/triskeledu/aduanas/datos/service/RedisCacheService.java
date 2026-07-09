package cl.triskeledu.aduanas.datos.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Servicio de cache distribuida sobre Redis.
 *
 * Namespacing: todas las claves se almacenan con el prefijo "aduanas:cache:"
 * para evitar colisiones con otros sistemas conectados al mismo Redis.
 *
 * Integracion con CacheEntry (PostgreSQL):
 *   - CacheEntryService llama a este servicio al crear/actualizar una entrada.
 *   - buscarPorClave() en CacheEntryService consulta primero Redis (fast path)
 *     y solo accede a PostgreSQL si la clave no esta en Redis o ya expiro.
 *
 * Escenario de TTL:
 *   - Si la fecha de expiracion ya paso, el TTL se fija en 1 segundo
 *     (Redis elimina la clave casi de inmediato).
 *   - TTL se calcula como los segundos restantes hasta las 23:59:59
 *     del dia de expiracion.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private static final String PREFIX = "aduanas:cache:";

    private final RedisTemplate<String, Object> redisTemplate;

    // -----------------------------------------------------------
    // CRUD basico
    // -----------------------------------------------------------

    /**
     * Almacena un valor en Redis con TTL en segundos.
     *
     * @param clave      identificador logico (sin prefijo)
     * @param valor      valor a cachear (String)
     * @param ttlSeconds tiempo de vida; si &lt;= 0 se usa 1 segundo
     */
    @SuppressWarnings("null")
    public void guardar(String clave, String valor, long ttlSeconds) {
        long ttl = ttlSeconds > 0 ? ttlSeconds : 1L;
        String key = PREFIX + clave;
        redisTemplate.opsForValue().set(key, valor, ttl, TimeUnit.SECONDS);
        log.info("[REDIS] SET {} = {} (TTL {}s)", key, valor, ttl);
    }

    /**
     * Almacena un valor usando una fecha de expiracion como referencia de TTL.
     * Calcula los segundos hasta las 23:59:59 del dia indicado.
     */
    public void guardarConFecha(String clave, String valor, LocalDate expira) {
        LocalDateTime limite = expira.atTime(23, 59, 59);
        long ttl = Duration.between(LocalDateTime.now(), limite).toSeconds();
        guardar(clave, valor, ttl);
    }

    /**
     * Recupera el valor almacenado en Redis. Retorna null si no existe o expiro.
     */
    //@SuppressWarnings("null")
    public String obtener(String clave) {
        Object valor = redisTemplate.opsForValue().get(PREFIX + clave);
        if (valor == null) {
            log.debug("[REDIS] MISS {}{}", PREFIX, clave);
            return null;
        }
        log.debug("[REDIS] HIT  {}{}", PREFIX, clave);
        return valor.toString();
    }

    /**
     * Elimina una clave del cache Redis.
     */
    //@SuppressWarnings("null")
    public void eliminar(String clave) {
        Boolean deleted = redisTemplate.delete(PREFIX + clave);
        log.info("[REDIS] DEL {}{} -> {}", PREFIX, clave, Boolean.TRUE.equals(deleted) ? "OK" : "NO_EXISTS");
    }

    /**
     * Verifica si una clave existe en Redis (y no ha expirado su TTL).
     */
    //@SuppressWarnings("null")
    public boolean existe(String clave) {
        Boolean exists = redisTemplate.hasKey(PREFIX + clave);
        return Boolean.TRUE.equals(exists);
    }

    /**
     * Retorna el TTL restante en segundos de una clave. -1 si no tiene TTL,
     * -2 si no existe la clave.
     */
    //@SuppressWarnings("null")
    public Long ttlRestante(String clave) {
        return redisTemplate.getExpire(PREFIX + clave, TimeUnit.SECONDS);
    }
}
