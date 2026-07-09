package cl.triskeledu.aduanas.notaria.service;

import cl.triskeledu.aduanas.notaria.dto.DocumentoRequest;
import cl.triskeledu.aduanas.notaria.dto.DocumentoResponse;
import cl.triskeledu.aduanas.notaria.mapper.DocumentoMapper;
import cl.triskeledu.aduanas.notaria.model.Documento;
import cl.triskeledu.aduanas.notaria.repository.DocumentoRepository;
import cl.triskeledu.aduanas.notaria.repository.PoderRepository;
import cl.triskeledu.common.exception.DuplicateResourceException;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final PoderRepository poderRepository;
    private final DocumentoMapper documentoMapper;

    public List<DocumentoResponse> listarTodos() {
        log.info("Listando todos los documentos");
        return documentoMapper.toResponseList(documentoRepository.findAllByOrderByIdAsc());
    }

    public DocumentoResponse buscarPorId(Integer id) {
        log.info("Buscando documento por id: {}", id);
        return documentoMapper.toResponse(getDocumentoById(id));
    }

    public List<DocumentoResponse> listarPorPoder(Integer idPoder) {
        log.info("Listando documentos del poder: {}", idPoder);
        return documentoMapper.toResponseList(documentoRepository.findByIdPoder(idPoder));
    }

    @Transactional
    @SuppressWarnings("null")
    public DocumentoResponse crear(DocumentoRequest request) {
        log.info("Creando documento con folio: {}", request.getFolio());
        if (!poderRepository.existsById(request.getIdPoder())) {
            throw new EntityNotFoundException("Poder", "id", request.getIdPoder());
        }
        if (documentoRepository.findByFolio(request.getFolio()).isPresent()) {
            throw new DuplicateResourceException("Documento", "folio", request.getFolio(), request.getFolio());
        }
        Documento documento = documentoMapper.toEntity(request);
        return documentoMapper.toResponse(documentoRepository.save(documento));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando documento id: {}", id);
        documentoRepository.delete(getDocumentoById(id));
    }

    @SuppressWarnings("null")
    private Documento getDocumentoById(Integer id) {
        return documentoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Documento", "id", id));
    }
}
