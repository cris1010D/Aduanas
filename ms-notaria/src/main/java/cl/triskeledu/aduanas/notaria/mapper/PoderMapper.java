package cl.triskeledu.aduanas.notaria.mapper;

import cl.triskeledu.aduanas.notaria.dto.PoderRequest;
import cl.triskeledu.aduanas.notaria.dto.PoderResponse;
import cl.triskeledu.aduanas.notaria.model.Poder;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PoderMapper {
    PoderResponse toResponse(Poder poder);
    List<PoderResponse> toResponseList(List<Poder> poderes);
    @Mapping(target = "id", ignore = true)
    Poder toEntity(PoderRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEntity(PoderRequest request, @MappingTarget Poder poder);
}
