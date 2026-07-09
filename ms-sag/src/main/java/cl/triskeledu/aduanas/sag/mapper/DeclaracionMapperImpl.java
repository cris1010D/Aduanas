package cl.triskeledu.aduanas.sag.mapper;

import cl.triskeledu.aduanas.sag.dto.DeclaracionRequest;
import cl.triskeledu.aduanas.sag.dto.DeclaracionResponse;
import cl.triskeledu.aduanas.sag.model.Declaracion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeclaracionMapperImpl implements DeclaracionMapper {

    @Override
    public DeclaracionResponse toResponse(Declaracion declaracion) {
        if (declaracion == null) return null;
        return DeclaracionResponse.builder()
                .id(declaracion.getId())
                .rutViajero(declaracion.getRutViajero())
                .fecha(declaracion.getFecha())
                .estado(declaracion.getEstado())
                .pasoFronterizo(declaracion.getPasoFronterizo())
                .build();
    }

    @Override
    public List<DeclaracionResponse> toResponseList(List<Declaracion> declaraciones) {
        if (declaraciones == null) return null;
        return declaraciones.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Declaracion toEntity(DeclaracionRequest request) {
        if (request == null) return null;
        return Declaracion.builder()
                .rutViajero(request.getRutViajero())
                .fecha(request.getFecha())
                .estado(request.getEstado())
                .pasoFronterizo(request.getPasoFronterizo())
                .build();
    }

    @Override
    public void updateEntity(DeclaracionRequest request, Declaracion declaracion) {
        if (request == null || declaracion == null) return;
        declaracion.setRutViajero(request.getRutViajero());
        declaracion.setFecha(request.getFecha());
        declaracion.setEstado(request.getEstado());
        declaracion.setPasoFronterizo(request.getPasoFronterizo());
    }
}
