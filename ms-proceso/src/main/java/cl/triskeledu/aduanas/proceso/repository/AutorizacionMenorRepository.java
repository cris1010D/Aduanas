package cl.triskeledu.aduanas.proceso.repository;

import cl.triskeledu.aduanas.proceso.model.AutorizacionMenor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorizacionMenorRepository extends JpaRepository<AutorizacionMenor, Integer> {

    Optional<AutorizacionMenor> findTopByRutMenorOrderByIdDesc(String rutMenor);

    boolean existsByRutMenorAndTipo(String rutMenor, String tipo);

    List<AutorizacionMenor> findAllByOrderByIdAsc();
}
