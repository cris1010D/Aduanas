package cl.triskeledu.aduanas.notaria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "proyeccion_poder")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyeccionPoder {

    @Id
    @Column(name = "rut_titular", length = 12)
    private String rutTitular;

    @Column(name = "rut_apoderado", nullable = false, length = 12)
    private String rutApoderado;

    @Column(nullable = false)
    private LocalDate vigencia;

    @Column(name = "notaria_origen", nullable = false, length = 60)
    private String notariaOrigen;

    @Column(nullable = false)
    private LocalDate sincronizado;
}
