package cl.triskeledu.aduanas.auditoria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "log_evento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LogEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rut_oficial", nullable = false, length = 12)
    private String rutOficial;

    @Column(nullable = false, length = 60)
    private String accion;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "ms_origen", nullable = false, length = 30)
    private String msOrigen;
}
