package cl.triskeledu.aduanas.datos.service;

import cl.triskeledu.aduanas.datos.dto.CacheEntryRequest;
import cl.triskeledu.aduanas.datos.dto.CacheEntryResponse;
import cl.triskeledu.aduanas.datos.mapper.CacheEntryMapper;
import cl.triskeledu.aduanas.datos.model.CacheEntry;
import cl.triskeledu.aduanas.datos.repository.CacheEntryRepository;
import cl.triskeledu.common.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CacheEntryService - Pruebas Unitarias (cache-aside pattern con Redis)")
class CacheEntryServiceTest {

    @Mock private CacheEntryRepository cacheEntryRepository;
    @Mock private CacheEntryMapper cacheEntryMapper;
    @Mock private RedisCacheService redisCacheService;

    @InjectMocks
    private CacheEntryService cacheEntryService;

    private CacheEntry cacheEntry;
    private CacheEntryResponse cacheEntryResponse;
    private CacheEntryRequest cacheEntryRequest;

    @BeforeEach
    void setUp() {
        LocalDate expira = LocalDate.now().plusDays(30);

        cacheEntry = CacheEntry.builder()
                .id(1).clave("config.timeout").valorCache("5000")
                .expira(expira).hits(0)
                .build();

        cacheEntryResponse = CacheEntryResponse.builder()
                .id(1).clave("config.timeout").valorCache("5000")
                .expira(expira).hits(0)
                .build();

        cacheEntryRequest = CacheEntryRequest.builder()
                .clave("config.timeout").valorCache("5000")
                .expira(expira).hits(0)
                .build();
    }

    // -------------------------------------------------------
    // listarTodas
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodas - retorna lista de entradas de cache")
    void listarTodas_retornaLista() {
        when(cacheEntryRepository.findAllByOrderByIdAsc()).thenReturn(List.of(cacheEntry));
        when(cacheEntryMapper.toResponseList(List.of(cacheEntry))).thenReturn(List.of(cacheEntryResponse));

        List<CacheEntryResponse> resultado = cacheEntryService.listarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getClave()).isEqualTo("config.timeout");
    }

    // -------------------------------------------------------
    // buscarPorId
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId - retorna cache entry existente")
    void buscarPorId_existente_retornaEntry() {
        when(cacheEntryRepository.findById(1)).thenReturn(Optional.of(cacheEntry));
        when(cacheEntryMapper.toResponse(cacheEntry)).thenReturn(cacheEntryResponse);

        CacheEntryResponse resultado = cacheEntryService.buscarPorId(1);

        assertThat(resultado.getClave()).isEqualTo("config.timeout");
    }

    @Test
    @DisplayName("buscarPorId - lanza EntityNotFoundException si no existe")
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(cacheEntryRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cacheEntryService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    // -------------------------------------------------------
    // buscarPorClave — cache-aside pattern
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorClave - Redis HIT: retorna valor desde Redis sin consultar PostgreSQL")
    void buscarPorClave_redisHit_retornaDesdeRedis() {
        when(redisCacheService.obtener("config.timeout")).thenReturn("5000");

        CacheEntryResponse resultado = cacheEntryService.buscarPorClave("config.timeout");

        assertThat(resultado.getClave()).isEqualTo("config.timeout");
        assertThat(resultado.getValorCache()).isEqualTo("5000");
        // No debe consultar PostgreSQL en cache HIT
        verify(cacheEntryRepository, never()).findByClave(any());
    }

    @Test
    @DisplayName("buscarPorClave - Redis MISS: cae a PostgreSQL y repopula Redis")
    void buscarPorClave_redisMiss_consultaPostgresYRepopulaRedis() {
        when(redisCacheService.obtener("config.timeout")).thenReturn(null);
        when(cacheEntryRepository.findByClave("config.timeout")).thenReturn(Optional.of(cacheEntry));
        when(cacheEntryMapper.toResponse(cacheEntry)).thenReturn(cacheEntryResponse);

        CacheEntryResponse resultado = cacheEntryService.buscarPorClave("config.timeout");

        assertThat(resultado.getClave()).isEqualTo("config.timeout");
        // Debe haber repoblado Redis tras el MISS
        verify(redisCacheService).guardarConFecha(
                eq("config.timeout"), eq("5000"), any(LocalDate.class));
    }

    @Test
    @DisplayName("buscarPorClave - Redis MISS + PG MISS: lanza EntityNotFoundException")
    void buscarPorClave_redisMissYPgMiss_lanzaExcepcion() {
        when(redisCacheService.obtener("clave.inexistente")).thenReturn(null);
        when(cacheEntryRepository.findByClave("clave.inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cacheEntryService.buscarPorClave("clave.inexistente"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("clave.inexistente");
    }

    // -------------------------------------------------------
    // crear
    // -------------------------------------------------------

    @Test
    @DisplayName("crear - persiste en PostgreSQL y replica en Redis")
    @SuppressWarnings("null")
    void crear_exitoso_persisteYReplicaRedis() {
        when(cacheEntryMapper.toEntity(cacheEntryRequest)).thenReturn(cacheEntry);
        when(cacheEntryRepository.save(cacheEntry)).thenReturn(cacheEntry);
        when(cacheEntryMapper.toResponse(cacheEntry)).thenReturn(cacheEntryResponse);

        CacheEntryResponse resultado = cacheEntryService.crear(cacheEntryRequest);

        assertThat(resultado.getClave()).isEqualTo("config.timeout");
        verify(cacheEntryRepository).save(cacheEntry);
        // Debe replicar en Redis con TTL derivado de la fecha de expiracion
        verify(redisCacheService).guardarConFecha(
                eq("config.timeout"), eq("5000"), any(LocalDate.class));
    }

    // -------------------------------------------------------
    // actualizar
    // -------------------------------------------------------

    @Test
    @DisplayName("actualizar - actualiza PG y refresca Redis (elimina clave antigua + re-inserta)")
    @SuppressWarnings("null")
    void actualizar_exitoso_actualizaPgYRefrescaRedis() {
        CacheEntryRequest requestActualizado = CacheEntryRequest.builder()
                .clave("config.timeout").valorCache("10000")
                .expira(LocalDate.now().plusDays(60)).hits(5)
                .build();

        when(cacheEntryRepository.findById(1)).thenReturn(Optional.of(cacheEntry));
        when(cacheEntryRepository.save(cacheEntry)).thenReturn(cacheEntry);
        when(cacheEntryMapper.toResponse(cacheEntry)).thenReturn(cacheEntryResponse);

        cacheEntryService.actualizar(1, requestActualizado);

        // Debe eliminar la clave antigua en Redis y re-insertar con nuevo TTL
        verify(redisCacheService).eliminar("config.timeout");
        verify(redisCacheService).guardarConFecha(
                eq("config.timeout"), any(), any(LocalDate.class));
    }

    @Test
    @DisplayName("actualizar - lanza EntityNotFoundException si no existe")
    void actualizar_noExiste_lanzaExcepcion() {
        when(cacheEntryRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cacheEntryService.actualizar(99, cacheEntryRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // -------------------------------------------------------
    // eliminar
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar - limpia Redis y luego elimina de PostgreSQL")
    @SuppressWarnings("null")
    void eliminar_exitoso_limpiaRedisYEliminaPg() {
        when(cacheEntryRepository.findById(1)).thenReturn(Optional.of(cacheEntry));

        cacheEntryService.eliminar(1);

        // Debe limpiar Redis ANTES de eliminar de PG
        verify(redisCacheService).eliminar("config.timeout");
        verify(cacheEntryRepository).delete(cacheEntry);
    }

    @Test
    @DisplayName("eliminar - lanza EntityNotFoundException si no existe")
    @SuppressWarnings("null")
    void eliminar_noExiste_lanzaExcepcion() {
        when(cacheEntryRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cacheEntryService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);

        verify(redisCacheService, never()).eliminar(any());
        verify(cacheEntryRepository, never()).delete(any());
    }
}
