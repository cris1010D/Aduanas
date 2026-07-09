package cl.triskeledu.aduanas.proceso.mapper;

import cl.triskeledu.aduanas.proceso.dto.AutorizacionMenorResponse;
import cl.triskeledu.aduanas.proceso.model.AutorizacionMenor;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AutorizacionMenorMapper {

    AutorizacionMenorResponse toResponse(AutorizacionMenor autorizacionMenor);

    List<AutorizacionMenorResponse> toResponseList(List<AutorizacionMenor> autorizaciones);

    @Mapping(target = "id", ignore = true)
    AutorizacionMenor toEntity(AutorizacionMenorResponse response);
}
