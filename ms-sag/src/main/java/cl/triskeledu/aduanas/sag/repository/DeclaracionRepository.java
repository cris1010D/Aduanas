package cl.triskeledu.aduanas.sag.repository;

import cl.triskeledu.aduanas.sag.model.Declaracion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeclaracionRepository extends JpaRepository<Declaracion, Integer> {
    List<Declaracion> findByRutViajero(String rutViajero);
    List<Declaracion> findAllByOrderByIdAsc();
}
