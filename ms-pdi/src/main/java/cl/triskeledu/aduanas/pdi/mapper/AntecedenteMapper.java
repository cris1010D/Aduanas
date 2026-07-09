package cl.triskeledu.aduanas.pdi.mapper;

import cl.triskeledu.aduanas.pdi.dto.AntecedenteRequest;
import cl.triskeledu.aduanas.pdi.dto.AntecedenteResponse;
import cl.triskeledu.aduanas.pdi.model.Antecedente;
import java.util.List;

public interface AntecedenteMapper {
    AntecedenteResponse toResponse(Antecedente antecedente);
    List<AntecedenteResponse> toResponseList(List<Antecedente> antecedentes);
    Antecedente toEntity(AntecedenteRequest request);
    void updateEntity(AntecedenteRequest request, Antecedente antecedente);
}
