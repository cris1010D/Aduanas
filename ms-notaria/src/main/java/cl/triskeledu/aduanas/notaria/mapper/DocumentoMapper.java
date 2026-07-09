package cl.triskeledu.aduanas.notaria.mapper;

import cl.triskeledu.aduanas.notaria.dto.DocumentoRequest;
import cl.triskeledu.aduanas.notaria.dto.DocumentoResponse;
import cl.triskeledu.aduanas.notaria.model.Documento;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface DocumentoMapper {
    DocumentoResponse toResponse(Documento documento);
    List<DocumentoResponse> toResponseList(List<Documento> documentos);
    @Mapping(target = "id", ignore = true)
    Documento toEntity(DocumentoRequest request);
}
