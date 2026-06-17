package cl.triskeledu.aduanas.menores.mapper;

import cl.triskeledu.aduanas.menores.dto.MenorRequest;
import cl.triskeledu.aduanas.menores.dto.MenorResponse;
import cl.triskeledu.aduanas.menores.model.Menor;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface MenorMapper {
    MenorResponse toResponse(Menor menor);
    List<MenorResponse> toResponseList(List<Menor> menores);
    @Mapping(target = "id", ignore = true)
    Menor toEntity(MenorRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEntity(MenorRequest request, @MappingTarget Menor menor);
}
