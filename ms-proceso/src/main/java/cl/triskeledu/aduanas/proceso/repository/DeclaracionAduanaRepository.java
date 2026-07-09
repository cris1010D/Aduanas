package cl.triskeledu.aduanas.proceso.repository;

import cl.triskeledu.aduanas.proceso.model.DeclaracionAduana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeclaracionAduanaRepository extends JpaRepository<DeclaracionAduana, Integer> {
    List<DeclaracionAduana> findByRutViajero(String rutViajero);
    List<DeclaracionAduana> findAllByOrderByIdAsc();
}