package cl.triskeledu.aduanas.auth.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "sesion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rut_oficial", nullable = false, length = 12)
    private String rutOficial;

    @Column(nullable = false, length = 120)
    private String token;

    @Column(nullable = false)
    private LocalDate inicio;

    @Column(nullable = false)
    private LocalDate expira;
}
