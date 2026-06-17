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

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheEntryService {

    private final CacheEntryRepository cacheEntryRepository;
    private final CacheEntryMapper cacheEntryMapper;

    public List<CacheEntryResponse> listarTodas() {
        log.info("Listando todas las entradas de cache");
        return cacheEntryMapper.toResponseList(cacheEntryRepository.findAllByOrderByIdAsc());
    }

    public CacheEntryResponse buscarPorId(Integer id) {
        log.info("Buscando cache entry por id: {}", id);
        return cacheEntryMapper.toResponse(getCacheById(id));
    }

    public CacheEntryResponse buscarPorClave(String clave) {
        log.info("Buscando cache entry por clave: {}", clave);
        return cacheEntryMapper.toResponse(
            cacheEntryRepository.findByClave(clave)
                .orElseThrow(() -> new EntityNotFoundException("CacheEntry", "clave", clave))
        );
    }

    @Transactional
    @SuppressWarnings("null")
    public CacheEntryResponse crear(CacheEntryRequest request) {
        log.info("Creando cache entry para clave: {}", request.getClave());
        CacheEntry cacheEntry = cacheEntryMapper.toEntity(request);
        return cacheEntryMapper.toResponse(cacheEntryRepository.save(cacheEntry));
    }

    @Transactional
    @SuppressWarnings("null")
    public CacheEntryResponse actualizar(Integer id, CacheEntryRequest request) {
        log.info("Actualizando cache entry id: {}", id);
        CacheEntry cacheEntry = getCacheById(id);
        cacheEntryMapper.updateEntity(request, cacheEntry);
        return cacheEntryMapper.toResponse(cacheEntryRepository.save(cacheEntry));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando cache entry id: {}", id);
        cacheEntryRepository.delete(getCacheById(id));
    }

    @SuppressWarnings("null")
    private CacheEntry getCacheById(Integer id) {
        return cacheEntryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CacheEntry", "id", id));
    }
}
