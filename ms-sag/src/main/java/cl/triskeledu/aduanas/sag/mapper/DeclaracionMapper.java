package cl.triskeledu.aduanas.sag.mapper;

import cl.triskeledu.aduanas.sag.dto.DeclaracionRequest;
import cl.triskeledu.aduanas.sag.dto.DeclaracionResponse;
import cl.triskeledu.aduanas.sag.model.Declaracion;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface DeclaracionMapper {
    DeclaracionResponse toResponse(Declaracion declaracion);
    List<DeclaracionResponse> toResponseList(List<Declaracion> declaraciones);
    @Mapping(target = "id", ignore = true)
    Declaracion toEntity(DeclaracionRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEntity(DeclaracionRequest request, @MappingTarget Declaracion declaracion);
}
