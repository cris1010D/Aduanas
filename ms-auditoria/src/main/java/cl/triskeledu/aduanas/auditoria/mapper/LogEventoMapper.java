package cl.triskeledu.aduanas.auditoria.mapper;

import cl.triskeledu.aduanas.auditoria.dto.LogEventoRequest;
import cl.triskeledu.aduanas.auditoria.dto.LogEventoResponse;
import cl.triskeledu.aduanas.auditoria.model.LogEvento;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface LogEventoMapper {
    LogEventoResponse toResponse(LogEvento logEvento);
    List<LogEventoResponse> toResponseList(List<LogEvento> logEventos);
    @Mapping(target = "id", ignore = true)
    LogEvento toEntity(LogEventoRequest request);
}
