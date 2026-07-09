package cl.triskeledu.aduanas.auditoria.repository;

import cl.triskeledu.aduanas.auditoria.model.DetalleLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleLogRepository extends JpaRepository<DetalleLog, Integer> {
    List<DetalleLog> findByIdLog(Integer idLog);
}
