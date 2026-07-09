package cl.triskeledu.aduanas.notaria.repository;

import cl.triskeledu.aduanas.notaria.model.Poder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoderRepository extends JpaRepository<Poder, Integer> {
    List<Poder> findByRutTitular(String rutTitular);
    List<Poder> findByRutApoderado(String rutApoderado);
    List<Poder> findAllByOrderByIdAsc();
}
