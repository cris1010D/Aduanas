package cl.triskeledu.aduanas.pdi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "consulta")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 12)
    private String rut;

    @Column(name = "rut_oficial", nullable = false, length = 12)
    private String rutOficial;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 60)
    private String motivo;
}
