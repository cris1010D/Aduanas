package cl.triskeledu.aduanas.sag.repository;

import cl.triskeledu.aduanas.sag.model.ItemDeclaracion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDeclaracionRepository extends JpaRepository<ItemDeclaracion, Integer> {
    List<ItemDeclaracion> findByIdDeclaracion(Integer idDeclaracion);
}
