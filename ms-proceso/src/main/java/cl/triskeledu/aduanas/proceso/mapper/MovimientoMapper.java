package cl.triskeledu.aduanas.proceso.mapper;

import cl.triskeledu.aduanas.proceso.dto.MovimientoRequest;
import cl.triskeledu.aduanas.proceso.dto.MovimientoResponse;
import cl.triskeledu.aduanas.proceso.model.Movimiento;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface MovimientoMapper {

    MovimientoResponse toResponse(Movimiento movimiento);

    List<MovimientoResponse> toResponseList(List<Movimiento> movimientos);

    @Mapping(target = "id", ignore = true)
    Movimiento toEntity(MovimientoRequest request);
}
