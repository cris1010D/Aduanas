package cl.triskeledu.aduanas.menores.mapper;

import cl.triskeledu.aduanas.menores.dto.MenorRequest;
import cl.triskeledu.aduanas.menores.dto.MenorResponse;
import cl.triskeledu.aduanas.menores.model.Menor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenorMapperImpl implements MenorMapper {

    @Override
    public MenorResponse toResponse(Menor menor) {
        if (menor == null) return null;
        return MenorResponse.builder()
                .id(menor.getId())
                .rut(menor.getRut())
                .nombre(menor.getNombre())
                .fechaNac(menor.getFechaNac())
                .rutTutor(menor.getRutTutor())
                .build();
    }

    @Override
    public List<MenorResponse> toResponseList(List<Menor> menores) {
        if (menores == null) return null;
        return menores.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Menor toEntity(MenorRequest request) {
        if (request == null) return null;
        return Menor.builder()
                .rut(request.getRut())
                .nombre(request.getNombre())
                .fechaNac(request.getFechaNac())
                .rutTutor(request.getRutTutor())
                .build();
    }

    @Override
    public void updateEntity(MenorRequest request, Menor menor) {
        if (request == null || menor == null) return;
        menor.setRut(request.getRut());
        menor.setNombre(request.getNombre());
        menor.setFechaNac(request.getFechaNac());
        menor.setRutTutor(request.getRutTutor());
    }
}
