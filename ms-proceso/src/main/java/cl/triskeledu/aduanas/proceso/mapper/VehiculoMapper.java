package cl.triskeledu.aduanas.proceso.mapper;

import cl.triskeledu.aduanas.proceso.dto.VehiculoRequest;
import cl.triskeledu.aduanas.proceso.dto.VehiculoResponse;
import cl.triskeledu.aduanas.proceso.model.Vehiculo;
import org.mapstruct.*;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
=======

import java.util.List;
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface VehiculoMapper {

    VehiculoResponse toResponse(Vehiculo vehiculo);

    List<VehiculoResponse> toResponseList(List<Vehiculo> vehiculos);

    @Mapping(target = "id", ignore = true)
    Vehiculo toEntity(VehiculoRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(VehiculoRequest request, @MappingTarget Vehiculo vehiculo);
<<<<<<< HEAD

    public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
        Optional<Vehiculo> findByPlaca(String placa);
        boolean existsByPlaca(String placa);
        List<Vehiculo> findAllByOrderByIdAsc();
        List<Vehiculo> findByRutPropietario(String rutPropietario); // nuevo
    }
=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
}
