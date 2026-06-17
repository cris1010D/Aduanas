package cl.triskeledu.aduanas.pdi.repository;

import cl.triskeledu.aduanas.pdi.model.Antecedente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AntecedenteRepository extends JpaRepository<Antecedente, Integer> {
    Optional<Antecedente> findByRut(String rut);
    boolean existsByRut(String rut);
    List<Antecedente> findAllByOrderByIdAsc();
}
