package cl.triskeledu.aduanas.pdi.mapper;

import cl.triskeledu.aduanas.pdi.dto.ConsultaRequest;
import cl.triskeledu.aduanas.pdi.dto.ConsultaResponse;
import cl.triskeledu.aduanas.pdi.model.Consulta;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ConsultaMapper {
    ConsultaResponse toResponse(Consulta consulta);
    List<ConsultaResponse> toResponseList(List<Consulta> consultas);
    @Mapping(target = "id", ignore = true)
    Consulta toEntity(ConsultaRequest request);
}
