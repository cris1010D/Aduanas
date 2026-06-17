package cl.triskeledu.aduanas.reporte.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "reporte")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "rut_oficial", nullable = false, length = 12)
    private String rutOficial;

    @Column(nullable = false, length = 10)
    private String formato;
}
