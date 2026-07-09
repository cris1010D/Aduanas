package cl.triskeledu.aduanas.reporte.mapper;

import cl.triskeledu.aduanas.reporte.dto.ReporteRequest;
import cl.triskeledu.aduanas.reporte.dto.ReporteResponse;
import cl.triskeledu.aduanas.reporte.model.Reporte;
import java.util.List;

public interface ReporteMapper {
    ReporteResponse toResponse(Reporte reporte);
    List<ReporteResponse> toResponseList(List<Reporte> reportes);
    Reporte toEntity(ReporteRequest request);
    void updateEntity(ReporteRequest request, Reporte reporte);
}
