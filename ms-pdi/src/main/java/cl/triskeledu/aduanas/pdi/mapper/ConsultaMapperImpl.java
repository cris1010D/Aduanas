package cl.triskeledu.aduanas.pdi.mapper;

import cl.triskeledu.aduanas.pdi.dto.ConsultaRequest;
import cl.triskeledu.aduanas.pdi.dto.ConsultaResponse;
import cl.triskeledu.aduanas.pdi.model.Consulta;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConsultaMapperImpl implements ConsultaMapper {

    @Override
    public ConsultaResponse toResponse(Consulta consulta) {
        if (consulta == null) return null;
        return ConsultaResponse.builder()
                .id(consulta.getId())
                .rut(consulta.getRut())
                .rutOficial(consulta.getRutOficial())
                .fecha(consulta.getFecha())
                .motivo(consulta.getMotivo())
                .build();
    }

    @Override
    public List<ConsultaResponse> toResponseList(List<Consulta> consultas) {
        if (consultas == null) return null;
        return consultas.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Consulta toEntity(ConsultaRequest request) {
        if (request == null) return null;
        return Consulta.builder()
                .rut(request.getRut())
                .rutOficial(request.getRutOficial())
                .fecha(request.getFecha())
                .motivo(request.getMotivo())
                .build();
    }
}
