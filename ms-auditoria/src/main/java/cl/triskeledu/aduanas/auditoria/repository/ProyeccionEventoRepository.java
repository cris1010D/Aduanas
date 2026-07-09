package cl.triskeledu.aduanas.auditoria.repository;

import cl.triskeledu.aduanas.auditoria.model.ProyeccionEvento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyeccionEventoRepository extends JpaRepository<ProyeccionEvento, String> {
}
