package cl.triskeledu.aduanas.datos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configuracion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 60)
    private String clave;

    @Column(nullable = false, length = 120)
    private String valor;

    @Column(name = "ms_duenio", nullable = false, length = 30)
    private String msDuenio;

    @Column(nullable = false)
    private Boolean activo;
}
