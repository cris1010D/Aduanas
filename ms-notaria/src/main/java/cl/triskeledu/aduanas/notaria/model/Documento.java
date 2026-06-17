package cl.triskeledu.aduanas.notaria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "documento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_poder", nullable = false)
    private Integer idPoder;

    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false, unique = true, length = 20)
    private String folio;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;
}
