package cl.triskeledu.aduanas.menores.mapper;

import cl.triskeledu.aduanas.menores.dto.MenorRequest;
import cl.triskeledu.aduanas.menores.dto.MenorResponse;
import cl.triskeledu.aduanas.menores.model.Menor;
import java.util.List;

public interface MenorMapper {
    MenorResponse toResponse(Menor menor);
    List<MenorResponse> toResponseList(List<Menor> menores);
    Menor toEntity(MenorRequest request);
    void updateEntity(MenorRequest request, Menor menor);
}
