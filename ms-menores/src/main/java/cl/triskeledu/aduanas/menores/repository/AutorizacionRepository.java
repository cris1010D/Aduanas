package cl.triskeledu.aduanas.menores.repository;

import cl.triskeledu.aduanas.menores.model.Autorizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorizacionRepository extends JpaRepository<Autorizacion, Integer> {
    List<Autorizacion> findByRutMenor(String rutMenor);
    List<Autorizacion> findAllByOrderByIdAsc();
}
