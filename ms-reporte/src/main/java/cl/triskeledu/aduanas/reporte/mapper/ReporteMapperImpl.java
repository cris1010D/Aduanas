package cl.triskeledu.aduanas.reporte.mapper;

import cl.triskeledu.aduanas.reporte.dto.ReporteRequest;
import cl.triskeledu.aduanas.reporte.dto.ReporteResponse;
import cl.triskeledu.aduanas.reporte.model.Reporte;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReporteMapperImpl implements ReporteMapper {

    @Override
    public ReporteResponse toResponse(Reporte reporte) {
        if (reporte == null) return null;
        return ReporteResponse.builder()
                .id(reporte.getId())
                .tipo(reporte.getTipo())
                .fecha(reporte.getFecha())
                .rutOficial(reporte.getRutOficial())
                .formato(reporte.getFormato())
                .build();
    }

    @Override
    public List<ReporteResponse> toResponseList(List<Reporte> reportes) {
        if (reportes == null) return null;
        return reportes.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public Reporte toEntity(ReporteRequest request) {
        if (request == null) return null;
        return Reporte.builder()
                .tipo(request.getTipo())
                .fecha(request.getFecha())
                .rutOficial(request.getRutOficial())
                .formato(request.getFormato())
                .build();
    }

    @Override
    public void updateEntity(ReporteRequest request, Reporte reporte) {
        if (request == null || reporte == null) return;
        reporte.setTipo(request.getTipo());
        reporte.setFecha(request.getFecha());
        reporte.setRutOficial(request.getRutOficial());
        reporte.setFormato(request.getFormato());
    }
}
