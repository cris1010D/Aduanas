package cl.triskeledu.aduanas.auth.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "proyeccion_oficial")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyeccionOficial {

    @Id
    @Column(length = 12)
    private String rut;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 30)
    private String rol;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private LocalDate sincronizado;
}
