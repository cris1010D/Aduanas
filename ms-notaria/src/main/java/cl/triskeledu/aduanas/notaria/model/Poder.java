package cl.triskeledu.aduanas.notaria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "poder")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Poder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rut_titular", nullable = false, length = 12)
    private String rutTitular;

    @Column(name = "rut_apoderado", nullable = false, length = 12)
    private String rutApoderado;

    @Column(name = "notaria_origen", nullable = false, length = 60)
    private String notariaOrigen;

    @Column(nullable = false)
    private LocalDate vigencia;
}
