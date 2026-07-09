package cl.triskeledu.aduanas.reporte.repository;

import cl.triskeledu.aduanas.reporte.model.DetalleReporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DetalleReporteRepository extends JpaRepository<DetalleReporte, Integer> {
    List<DetalleReporte> findByIdReporte(Integer idReporte);
}
