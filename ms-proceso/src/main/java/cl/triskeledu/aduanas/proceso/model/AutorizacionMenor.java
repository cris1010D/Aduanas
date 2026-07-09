package cl.triskeledu.aduanas.proceso.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "autorizacion_menor")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AutorizacionMenor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rut_menor", nullable = false, length = 12)
    private String rutMenor;

    @Column(name = "rut_tutor", nullable = false, length = 12)
    private String rutTutor;

    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;
}
