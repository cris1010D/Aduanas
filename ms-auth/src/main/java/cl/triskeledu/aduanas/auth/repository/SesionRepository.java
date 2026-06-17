package cl.triskeledu.aduanas.auth.repository;

import cl.triskeledu.aduanas.auth.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {
    List<Sesion> findByRutOficial(String rutOficial);
    List<Sesion> findAllByOrderByIdAsc();
}
