package cl.triskeledu.aduanas.proceso.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "movimiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rut_viajero", nullable = false, length = 12)
    private String rutViajero;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "paso_fronterizo", nullable = false, length = 40)
    private String pasoFronterizo;
}
