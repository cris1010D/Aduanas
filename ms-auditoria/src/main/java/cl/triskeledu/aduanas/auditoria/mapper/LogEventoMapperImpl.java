package cl.triskeledu.aduanas.auditoria.mapper;

import cl.triskeledu.aduanas.auditoria.dto.LogEventoRequest;
import cl.triskeledu.aduanas.auditoria.dto.LogEventoResponse;
import cl.triskeledu.aduanas.auditoria.model.LogEvento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LogEventoMapperImpl implements LogEventoMapper {

    @Override
    public LogEventoResponse toResponse(LogEvento logEvento) {
        if (logEvento == null) return null;
        return LogEventoResponse.builder()
                .id(logEvento.getId())
                .rutOficial(logEvento.getRutOficial())
                .accion(logEvento.getAccion())
                .fecha(logEvento.getFecha())
                .msOrigen(logEvento.getMsOrigen())
                .build();
    }

    @Override
    public List<LogEventoResponse> toResponseList(List<LogEvento> logEventos) {
        if (logEventos == null) return null;
        return logEventos.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public LogEvento toEntity(LogEventoRequest request) {
        if (request == null) return null;
        return LogEvento.builder()
                .rutOficial(request.getRutOficial())
                .accion(request.getAccion())
                .fecha(request.getFecha())
                .msOrigen(request.getMsOrigen())
                .build();
    }
}
