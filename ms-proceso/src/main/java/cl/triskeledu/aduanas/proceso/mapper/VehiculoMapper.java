package cl.triskeledu.aduanas.proceso.mapper;

import cl.triskeledu.aduanas.proceso.dto.VehiculoRequest;
import cl.triskeledu.aduanas.proceso.dto.VehiculoResponse;
import cl.triskeledu.aduanas.proceso.model.Vehiculo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface VehiculoMapper {

    VehiculoResponse toResponse(Vehiculo vehiculo);

    List<VehiculoResponse> toResponseList(List<Vehiculo> vehiculos);

    @Mapping(target = "id", ignore = true)
    Vehiculo toEntity(VehiculoRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(VehiculoRequest request, @MappingTarget Vehiculo vehiculo);

    // NOTA: el repositorio VehiculoRepository real vive en
    // cl.triskeledu.aduanas.proceso.repository.VehiculoRepository.
    // Aquí había una interfaz anidada duplicada que registraba un
    // segundo bean JpaRepository para la misma entidad Vehiculo — eliminada.
}