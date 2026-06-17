package cl.triskeledu.aduanas.datos.mapper;

import cl.triskeledu.aduanas.datos.dto.ConfiguracionRequest;
import cl.triskeledu.aduanas.datos.dto.ConfiguracionResponse;
import cl.triskeledu.aduanas.datos.model.Configuracion;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ConfiguracionMapper {
    ConfiguracionResponse toResponse(Configuracion configuracion);
    List<ConfiguracionResponse> toResponseList(List<Configuracion> configuraciones);
    @Mapping(target = "id", ignore = true)
    Configuracion toEntity(ConfiguracionRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEntity(ConfiguracionRequest request, @MappingTarget Configuracion configuracion);
}
