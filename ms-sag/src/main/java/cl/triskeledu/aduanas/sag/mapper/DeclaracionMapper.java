package cl.triskeledu.aduanas.sag.mapper;

import cl.triskeledu.aduanas.sag.dto.DeclaracionRequest;
import cl.triskeledu.aduanas.sag.dto.DeclaracionResponse;
import cl.triskeledu.aduanas.sag.model.Declaracion;
import java.util.List;

public interface DeclaracionMapper {
    DeclaracionResponse toResponse(Declaracion declaracion);
    List<DeclaracionResponse> toResponseList(List<Declaracion> declaraciones);
    Declaracion toEntity(DeclaracionRequest request);
    void updateEntity(DeclaracionRequest request, Declaracion declaracion);
}
