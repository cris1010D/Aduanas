package cl.triskeledu.aduanas.menores.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "proyeccion_permiso")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyeccionPermiso {

    @Id
    @Column(name = "rut_menor", length = 12)
    private String rutMenor;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false)
    private LocalDate vigencia;

    @Column(name = "notaria_origen", nullable = false, length = 60)
    private String notariaOrigen;

    @Column(nullable = false)
    private LocalDate sincronizado;
}
