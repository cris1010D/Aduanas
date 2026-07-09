package cl.triskeledu.aduanas.notaria.service;

import cl.triskeledu.aduanas.notaria.dto.DocumentoRequest;
import cl.triskeledu.aduanas.notaria.dto.DocumentoResponse;
import cl.triskeledu.aduanas.notaria.mapper.DocumentoMapper;
import cl.triskeledu.aduanas.notaria.model.Documento;
import cl.triskeledu.aduanas.notaria.repository.DocumentoRepository;
import cl.triskeledu.aduanas.notaria.repository.PoderRepository;
import cl.triskeledu.common.exception.DuplicateResourceException;
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
@DisplayName("DocumentoService - Pruebas Unitarias")
class DocumentoServiceTest {

    @Mock private DocumentoRepository documentoRepository;
    @Mock private PoderRepository poderRepository;
    @Mock private DocumentoMapper documentoMapper;

    @InjectMocks
    private DocumentoService documentoService;

    private Documento documento;
    private DocumentoResponse documentoResponse;
    private DocumentoRequest documentoRequest;

    @BeforeEach
    void setUp() {
        documento = Documento.builder()
                .id(1).idPoder(1).tipo("ESCRITURA")
                .folio("F-2024-001").fechaEmision(LocalDate.of(2024, 1, 15))
                .build();

        documentoResponse = DocumentoResponse.builder()
                .id(1).idPoder(1).tipo("ESCRITURA")
                .folio("F-2024-001").fechaEmision(LocalDate.of(2024, 1, 15))
                .build();

        documentoRequest = DocumentoRequest.builder()
                .idPoder(1).tipo("ESCRITURA")
                .folio("F-2024-001").fechaEmision(LocalDate.of(2024, 1, 15))
                .build();
    }

    // -------------------------------------------------------
    // listarTodos
    // -------------------------------------------------------

    @Test
    @DisplayName("listarTodos - retorna lista de documentos")
    void listarTodos_retornaLista() {
        when(documentoRepository.findAllByOrderByIdAsc()).thenReturn(List.of(documento));
        when(documentoMapper.toResponseList(List.of(documento))).thenReturn(List.of(documentoResponse));

        List<DocumentoResponse> resultado = documentoService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFolio()).isEqualTo("F-2024-001");
        verify(documentoRepository).findAllByOrderByIdAsc();
    }

    // -------------------------------------------------------
    // buscarPorId
    // -------------------------------------------------------

    @Test
    @DisplayName("buscarPorId - retorna documento existente")
    void buscarPorId_existente_retornaDocumento() {
        when(documentoRepository.findById(1)).thenReturn(Optional.of(documento));
        when(documentoMapper.toResponse(documento)).thenReturn(documentoResponse);

        DocumentoResponse resultado = documentoService.buscarPorId(1);

        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getTipo()).isEqualTo("ESCRITURA");
    }

    @Test
    @DisplayName("buscarPorId - lanza EntityNotFoundException si no existe")
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(documentoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentoService.buscarPorId(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    // -------------------------------------------------------
    // listarPorPoder
    // -------------------------------------------------------

    @Test
    @DisplayName("listarPorPoder - retorna documentos del poder indicado")
    void listarPorPoder_retornaDocumentos() {
        when(documentoRepository.findByIdPoder(1)).thenReturn(List.of(documento));
        when(documentoMapper.toResponseList(List.of(documento))).thenReturn(List.of(documentoResponse));

        List<DocumentoResponse> resultado = documentoService.listarPorPoder(1);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getIdPoder()).isEqualTo(1);
    }

    // -------------------------------------------------------
    // crear
    // -------------------------------------------------------

    @Test
    @DisplayName("crear - persiste documento cuando poder existe y folio es unico")
    @SuppressWarnings("null")
    void crear_exitoso_guardaDocumento() {
        when(poderRepository.existsById(1)).thenReturn(true);
        when(documentoRepository.findByFolio("F-2024-001")).thenReturn(Optional.empty());
        when(documentoMapper.toEntity(documentoRequest)).thenReturn(documento);
        when(documentoRepository.save(documento)).thenReturn(documento);
        when(documentoMapper.toResponse(documento)).thenReturn(documentoResponse);

        DocumentoResponse resultado = documentoService.crear(documentoRequest);

        assertThat(resultado.getFolio()).isEqualTo("F-2024-001");
        assertThat(resultado.getTipo()).isEqualTo("ESCRITURA");
        verify(documentoRepository).save(documento);
    }

    @Test
    @DisplayName("crear - lanza EntityNotFoundException si el poder no existe")
    @SuppressWarnings("null")
    void crear_poderInexistente_lanzaExcepcion() {
        when(poderRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> documentoService.crear(documentoRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Poder");

        verify(documentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear - lanza DuplicateResourceException si folio ya existe")
    @SuppressWarnings("null")
    void crear_folioDuplicado_lanzaExcepcion() {
        when(poderRepository.existsById(1)).thenReturn(true);
        when(documentoRepository.findByFolio("F-2024-001")).thenReturn(Optional.of(documento));

        assertThatThrownBy(() -> documentoService.crear(documentoRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("F-2024-001");

        verify(documentoRepository, never()).save(any());
    }

    // -------------------------------------------------------
    // eliminar
    // -------------------------------------------------------

    @Test
    @DisplayName("eliminar - elimina documento existente")
    @SuppressWarnings("null")
    void eliminar_exitoso_eliminaDocumento() {
        when(documentoRepository.findById(1)).thenReturn(Optional.of(documento));

        documentoService.eliminar(1);

        verify(documentoRepository).delete(documento);
    }

    @Test
    @DisplayName("eliminar - lanza EntityNotFoundException si documento no existe")
    @SuppressWarnings("null")
    void eliminar_noExiste_lanzaExcepcion() {
        when(documentoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> documentoService.eliminar(99))
                .isInstanceOf(EntityNotFoundException.class);

        verify(documentoRepository, never()).delete(any());
    }
}
