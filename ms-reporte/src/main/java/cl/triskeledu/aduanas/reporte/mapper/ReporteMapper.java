package cl.triskeledu.aduanas.reporte.mapper;

import cl.triskeledu.aduanas.reporte.dto.ReporteRequest;
import cl.triskeledu.aduanas.reporte.dto.ReporteResponse;
import cl.triskeledu.aduanas.reporte.model.Reporte;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ReporteMapper {
    ReporteResponse toResponse(Reporte reporte);
    List<ReporteResponse> toResponseList(List<Reporte> reportes);
    @Mapping(target = "id", ignore = true)
    Reporte toEntity(ReporteRequest request);
    @Mapping(target = "id", ignore = true)
    void updateEntity(ReporteRequest request, @MappingTarget Reporte reporte);
}
