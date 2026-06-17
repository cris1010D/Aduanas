package cl.triskeledu.aduanas.sag.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "declaracion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Declaracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rut_viajero", nullable = false, length = 12)
    private String rutViajero;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "paso_fronterizo", nullable = false, length = 40)
    private String pasoFronterizo;
}
