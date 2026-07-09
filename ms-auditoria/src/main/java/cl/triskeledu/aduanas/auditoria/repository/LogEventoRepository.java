package cl.triskeledu.aduanas.auditoria.repository;

import cl.triskeledu.aduanas.auditoria.model.LogEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogEventoRepository extends JpaRepository<LogEvento, Integer> {
    List<LogEvento> findByRutOficial(String rutOficial);
    List<LogEvento> findByMsOrigen(String msOrigen);
    List<LogEvento> findAllByOrderByIdAsc();
}
