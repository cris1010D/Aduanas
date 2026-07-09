package cl.triskeledu.aduanas.menores.mapper;

import cl.triskeledu.aduanas.menores.dto.AutorizacionRequest;
import cl.triskeledu.aduanas.menores.dto.AutorizacionResponse;
import cl.triskeledu.aduanas.menores.model.Autorizacion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AutorizacionMapperImpl implements AutorizacionMapper {

    @Override
    public AutorizacionResponse toResponse(Autorizacion autorizacion) {
        if (autorizacion == null) return null;
        return AutorizacionResponse.builder()
                .id(autorizacion.getId())
                .rutMenor(autorizacion.getRutMenor())
                .tipo(autorizacion.getTipo())
                .vigencia(autorizacion.getVigencia())
                .notariaOrigen(autorizacion.getNotariaOrigen())
                .build();
    }

    @Override
    public List<AutorizacionResponse> toResponseList(List<Autorizacion> autorizaciones) {
        if (autorizaciones == null) return null;
        return autorizaciones.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Autorizacion toEntity(AutorizacionRequest request) {
        if (request == null) return null;
        return Autorizacion.builder()
                .rutMenor(request.getRutMenor())
                .tipo(request.getTipo())
                .vigencia(request.getVigencia())
                .notariaOrigen(request.getNotariaOrigen())
                .build();
    }
}
