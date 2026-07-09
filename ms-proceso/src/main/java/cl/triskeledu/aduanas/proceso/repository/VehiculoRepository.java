package cl.triskeledu.aduanas.proceso.repository;

import cl.triskeledu.aduanas.proceso.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    Optional<Vehiculo> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
    List<Vehiculo> findAllByOrderByIdAsc();
<<<<<<< HEAD

    List<Vehiculo> findByRutPropietario(String rutPropietario);
=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
}
