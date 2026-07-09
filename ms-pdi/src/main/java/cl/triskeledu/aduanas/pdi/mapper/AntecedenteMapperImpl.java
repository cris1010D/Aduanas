package cl.triskeledu.aduanas.pdi.mapper;

import cl.triskeledu.aduanas.pdi.dto.AntecedenteRequest;
import cl.triskeledu.aduanas.pdi.dto.AntecedenteResponse;
import cl.triskeledu.aduanas.pdi.model.Antecedente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AntecedenteMapperImpl implements AntecedenteMapper {

    @Override
    public AntecedenteResponse toResponse(Antecedente antecedente) {
        if (antecedente == null) return null;
        return AntecedenteResponse.builder()
                .id(antecedente.getId())
                .rut(antecedente.getRut())
                .resultado(antecedente.getResultado())
                .fechaConsulta(antecedente.getFechaConsulta())
                .fuente(antecedente.getFuente())
                .build();
    }

    @Override
    public List<AntecedenteResponse> toResponseList(List<Antecedente> antecedentes) {
        if (antecedentes == null) return null;
        return antecedentes.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Antecedente toEntity(AntecedenteRequest request) {
        if (request == null) return null;
        return Antecedente.builder()
                .rut(request.getRut())
                .resultado(request.getResultado())
                .fechaConsulta(request.getFechaConsulta())
                .fuente(request.getFuente())
                .build();
    }

    @Override
    public void updateEntity(AntecedenteRequest request, Antecedente antecedente) {
        if (request == null || antecedente == null) return;
        antecedente.setRut(request.getRut());
        antecedente.setResultado(request.getResultado());
        antecedente.setFechaConsulta(request.getFechaConsulta());
        antecedente.setFuente(request.getFuente());
    }
}
