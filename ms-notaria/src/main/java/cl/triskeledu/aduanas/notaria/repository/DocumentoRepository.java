package cl.triskeledu.aduanas.notaria.repository;

import cl.triskeledu.aduanas.notaria.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
    List<Documento> findByIdPoder(Integer idPoder);
    Optional<Documento> findByFolio(String folio);
    List<Documento> findAllByOrderByIdAsc();
}
