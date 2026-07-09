package cl.triskeledu.aduanas.auditoria.mapper;

import cl.triskeledu.aduanas.auditoria.dto.LogEventoRequest;
import cl.triskeledu.aduanas.auditoria.dto.LogEventoResponse;
import cl.triskeledu.aduanas.auditoria.model.LogEvento;
import java.util.List;

public interface LogEventoMapper {
    LogEventoResponse toResponse(LogEvento logEvento);
    List<LogEventoResponse> toResponseList(List<LogEvento> logEventos);
    LogEvento toEntity(LogEventoRequest request);
}
