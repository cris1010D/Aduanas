package cl.triskeledu.aduanas.auditoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "proyeccion_evento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyeccionEvento {

    @Id
    @Column(name = "rut_oficial", length = 12)
    private String rutOficial;

    @Column(name = "ultima_accion", nullable = false, length = 60)
    private String ultimaAccion;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "ms_origen", nullable = false, length = 30)
    private String msOrigen;

    @Column(nullable = false)
    private LocalDate sincronizado;
}
