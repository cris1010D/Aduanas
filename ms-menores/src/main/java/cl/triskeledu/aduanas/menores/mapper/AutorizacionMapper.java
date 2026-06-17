package cl.triskeledu.aduanas.menores.mapper;

import cl.triskeledu.aduanas.menores.dto.AutorizacionRequest;
import cl.triskeledu.aduanas.menores.dto.AutorizacionResponse;
import cl.triskeledu.aduanas.menores.model.Autorizacion;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AutorizacionMapper {
    AutorizacionResponse toResponse(Autorizacion autorizacion);
    List<AutorizacionResponse> toResponseList(List<Autorizacion> autorizaciones);
    @Mapping(target = "id", ignore = true)
    Autorizacion toEntity(AutorizacionRequest request);
}
