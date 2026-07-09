package cl.triskeledu.aduanas.datos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "proyeccion_cache")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyeccionCache {

    @Id
    @Column(length = 60)
    private String clave;

    @Column(name = "valor_cache", nullable = false, length = 120)
    private String valorCache;

    @Column(nullable = false)
    private LocalDate expira;

    @Column(name = "ms_duenio", nullable = false, length = 30)
    private String msDuenio;

    @Column(nullable = false)
    private LocalDate sincronizado;
}
