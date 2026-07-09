package cl.triskeledu.aduanas.menores.mapper;

import cl.triskeledu.aduanas.menores.dto.AutorizacionRequest;
import cl.triskeledu.aduanas.menores.dto.AutorizacionResponse;
import cl.triskeledu.aduanas.menores.model.Autorizacion;
import java.util.List;

public interface AutorizacionMapper {
    AutorizacionResponse toResponse(Autorizacion autorizacion);
    List<AutorizacionResponse> toResponseList(List<Autorizacion> autorizaciones);
    Autorizacion toEntity(AutorizacionRequest request);
}
