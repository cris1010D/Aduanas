package cl.triskeledu.aduanas.pdi.mapper;

import cl.triskeledu.aduanas.pdi.dto.ConsultaRequest;
import cl.triskeledu.aduanas.pdi.dto.ConsultaResponse;
import cl.triskeledu.aduanas.pdi.model.Consulta;
import java.util.List;

public interface ConsultaMapper {
    ConsultaResponse toResponse(Consulta consulta);
    List<ConsultaResponse> toResponseList(List<Consulta> consultas);
    Consulta toEntity(ConsultaRequest request);
}
