package cl.triskeledu.aduanas.proceso.mapper;

import cl.triskeledu.aduanas.proceso.dto.ViajeroRequest;
import cl.triskeledu.aduanas.proceso.dto.ViajeroResponse;
import cl.triskeledu.aduanas.proceso.model.Viajero;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ViajeroMapper {

    ViajeroResponse toResponse(Viajero viajero);

    List<ViajeroResponse> toResponseList(List<Viajero> viajeros);

    @Mapping(target = "id", ignore = true)
    Viajero toEntity(ViajeroRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(ViajeroRequest request, @MappingTarget Viajero viajero);
}
