package cl.triskeledu.aduanas.pdi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "antecedente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Antecedente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(nullable = false, length = 20)
    private String resultado;

    @Column(name = "fecha_consulta", nullable = false)
    private LocalDate fechaConsulta;

    @Column(nullable = false, length = 40)
    private String fuente;
}
