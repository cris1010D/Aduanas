package cl.triskeledu.aduanas.proceso.mapper;

import cl.triskeledu.aduanas.proceso.dto.DeclaracionAduanaResponse;
import cl.triskeledu.aduanas.proceso.model.DeclaracionAduana;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface DeclaracionAduanaMapper {

    DeclaracionAduanaResponse toResponse(DeclaracionAduana declaracion);

    List<DeclaracionAduanaResponse> toResponseList(List<DeclaracionAduana> declaraciones);
}