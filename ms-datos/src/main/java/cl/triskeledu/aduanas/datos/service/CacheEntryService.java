package cl.triskeledu.aduanas.datos.service;

import cl.triskeledu.aduanas.datos.dto.CacheEntryRequest;
import cl.triskeledu.aduanas.datos.dto.CacheEntryResponse;
import cl.triskeledu.aduanas.datos.mapper.CacheEntryMapper;
import cl.triskeledu.aduanas.datos.model.CacheEntry;
import cl.triskeledu.aduanas.datos.repository.CacheEntryRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de cache_entry con integracion Redis (cache-aside pattern).
 *
 * Flujo de escritura:
 *   crear() / actualizar() → persiste en PostgreSQL (registro permanente)
 *                          → replica en Redis con TTL derivado de campo 'expira'
 *
 * Flujo de lectura (buscarPorClave):
 *   1. Consulta Redis (fast path, O(1))
 *   2. Si MISS → consulta PostgreSQL (fallback)
 *   3. Si encontrado en PG → repopula Redis con TTL restante
 *
 * La integracion es transparente: el controller no cambia.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheEntryService {

    private final CacheEntryRepository cacheEntryRepository;
    private final CacheEntryMapper     cacheEntryMapper;
    private final RedisCacheService    redisCacheService;

    public List<CacheEntryResponse> listarTodas() {
        log.info("Listando todas las entradas de cache");
        return cacheEntryMapper.toResponseList(cacheEntryRepository.findAllByOrderByIdAsc());
    }

    public CacheEntryResponse buscarPorId(Integer id) {
        log.info("Buscando cache entry por id: {}", id);
        return cacheEntryMapper.toResponse(getCacheById(id));
    }

    /**
     * Busqueda Redis-first: si la clave vive en Redis retorna inmediatamente.
     * Si no (MISS o TTL expirado) cae a PostgreSQL y repopula Redis.
     */
    public CacheEntryResponse buscarPorClave(String clave) {
        log.info("Buscando cache entry por clave: {}", clave);

        // 1. Fast path: Redis
        String valorRedis = redisCacheService.obtener(clave);
        if (valorRedis != null) {
            log.info("[CACHE] HIT Redis para clave: {}", clave);
            // Construir respuesta ligera desde Redis (sin ir a PG)
            return CacheEntryResponse.builder()
                .clave(clave)
                .valorCache(valorRedis)
                .hits(null)
                .expira(null)
                .build();
        }

        // 2. Fallback: PostgreSQL
        log.info("[CACHE] MISS Redis — consultando PostgreSQL para clave: {}", clave);
        CacheEntry entry = cacheEntryRepository.findByClave(clave)
            .orElseThrow(() -> new EntityNotFoundException("CacheEntry", "clave", clave));

        // 3. Repopular Redis con TTL restante
        redisCacheService.guardarConFecha(entry.getClave(), entry.getValorCache(), entry.getExpira());

        return cacheEntryMapper.toResponse(entry);
    }

    @Transactional
    @SuppressWarnings("null")
    public CacheEntryResponse crear(CacheEntryRequest request) {
        log.info("Creando cache entry para clave: {}", request.getClave());
        CacheEntry cacheEntry = cacheEntryMapper.toEntity(request);
        CacheEntry guardado  = cacheEntryRepository.save(cacheEntry);

        // Replicar en Redis con TTL derivado de la fecha de expiracion
        redisCacheService.guardarConFecha(
            guardado.getClave(), guardado.getValorCache(), guardado.getExpira());

        return cacheEntryMapper.toResponse(guardado);
    }

    @Transactional
    @SuppressWarnings("null")
    public CacheEntryResponse actualizar(Integer id, CacheEntryRequest request) {
        log.info("Actualizando cache entry id: {}", id);
        CacheEntry cacheEntry = getCacheById(id);
        cacheEntryMapper.updateEntity(request, cacheEntry);
        CacheEntry guardado = cacheEntryRepository.save(cacheEntry);

        // Actualizar Redis: elimina clave antigua y re-inserta con nuevo TTL
        redisCacheService.eliminar(guardado.getClave());
        redisCacheService.guardarConFecha(
            guardado.getClave(), guardado.getValorCache(), guardado.getExpira());

        return cacheEntryMapper.toResponse(guardado);
    }

    @Transactional
    //@SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando cache entry id: {}", id);
        CacheEntry entry = getCacheById(id);
        // Limpiar Redis antes de eliminar de PG
        redisCacheService.eliminar(entry.getClave());
        cacheEntryRepository.delete(entry);
    }

    @SuppressWarnings("null")
    private CacheEntry getCacheById(Integer id) {
        return cacheEntryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CacheEntry", "id", id));
    }
}
