package cl.triskeledu.aduanas.pdi.repository;

import cl.triskeledu.aduanas.pdi.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {
    List<Consulta> findByRut(String rut);
    List<Consulta> findAllByOrderByIdAsc();
}
