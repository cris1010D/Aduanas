package cl.triskeledu.aduanas.proceso.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "viajero")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Viajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(unique = true, length = 20)
    private String pasaporte;

    @Column(nullable = false, length = 40)
    private String nacionalidad;
}
