package cl.triskeledu.aduanas.auth.mapper;

import cl.triskeledu.aduanas.auth.dto.SesionRequest;
import cl.triskeledu.aduanas.auth.dto.SesionResponse;
import cl.triskeledu.aduanas.auth.model.Sesion;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface SesionMapper {

    SesionResponse toResponse(Sesion sesion);

    List<SesionResponse> toResponseList(List<Sesion> sesiones);

    @Mapping(target = "id", ignore = true)
    Sesion toEntity(SesionRequest request);
}
