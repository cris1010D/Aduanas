package cl.triskeledu.aduanas.auth.mapper;

import cl.triskeledu.aduanas.auth.dto.OficialCreateRequest;
import cl.triskeledu.aduanas.auth.dto.OficialResponse;
import cl.triskeledu.aduanas.auth.dto.OficialUpdateRequest;
import cl.triskeledu.aduanas.auth.model.Oficial;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OficialMapper {

    OficialResponse toResponse(Oficial oficial);

    List<OficialResponse> toResponseList(List<Oficial> oficiales);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    Oficial toEntity(OficialCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntity(OficialUpdateRequest request, @MappingTarget Oficial oficial);
}