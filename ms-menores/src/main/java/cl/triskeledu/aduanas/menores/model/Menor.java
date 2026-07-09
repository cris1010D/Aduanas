package cl.triskeledu.aduanas.menores.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "menor")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Menor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(name = "fecha_nac", nullable = false)
    private LocalDate fechaNac;

    @Column(name = "rut_tutor", nullable = false, length = 12)
    private String rutTutor;
}
