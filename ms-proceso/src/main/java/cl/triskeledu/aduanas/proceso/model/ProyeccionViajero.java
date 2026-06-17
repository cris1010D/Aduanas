package cl.triskeledu.aduanas.proceso.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "proyeccion_viajero")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyeccionViajero {

    @Id
    @Column(length = 12)
    private String rut;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 40)
    private String nacionalidad;

    @Column(name = "ultimo_movimiento", nullable = false)
    private LocalDate ultimoMovimiento;

    @Column(nullable = false)
    private LocalDate sincronizado;
}
