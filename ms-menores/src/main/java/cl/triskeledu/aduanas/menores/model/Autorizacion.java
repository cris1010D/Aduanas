package cl.triskeledu.aduanas.menores.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "autorizacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Autorizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rut_menor", nullable = false, length = 12)
    private String rutMenor;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false)
    private LocalDate vigencia;

    @Column(name = "notaria_origen", nullable = false, length = 60)
    private String notariaOrigen;
}
