package cl.triskeledu.aduanas.datos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "cache_entry")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CacheEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 60)
    private String clave;

    @Column(name = "valor_cache", nullable = false, length = 120)
    private String valorCache;

    @Column(nullable = false)
    private LocalDate expira;

    @Column(nullable = false)
    private Integer hits;
}
