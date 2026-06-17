package cl.triskeledu.aduanas.reporte.repository;

import cl.triskeledu.aduanas.reporte.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Integer> {
    List<Reporte> findByTipo(String tipo);
    List<Reporte> findByRutOficial(String rutOficial);
    List<Reporte> findAllByOrderByIdAsc();
}
