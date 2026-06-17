package cl.triskeledu.aduanas.pdi.mapper;

import cl.triskeledu.aduanas.pdi.dto.AntecedenteRequest;
import cl.triskeledu.aduanas.pdi.dto.AntecedenteResponse;
import cl.triskeledu.aduanas.pdi.model.Antecedente;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AntecedenteMapper {
    AntecedenteResponse toResponse(Antecedente antecedente);
    List<AntecedenteResponse> toResponseList(List<Antecedente> antecedentes);
    @Mapping(target = "id", ignore = true)
    Antecedente toEntity(AntecedenteRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEntity(AntecedenteRequest request, @MappingTarget Antecedente antecedente);
}
