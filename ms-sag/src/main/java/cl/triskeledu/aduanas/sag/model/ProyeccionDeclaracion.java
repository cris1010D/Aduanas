package cl.triskeledu.aduanas.sag.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "proyeccion_declaracion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyeccionDeclaracion {

    @Id
    @Column(name = "rut_viajero", length = 12)
    private String rutViajero;

    @Column(name = "ultima_fecha", nullable = false)
    private LocalDate ultimaFecha;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "paso_fronterizo", nullable = false, length = 40)
    private String pasoFronterizo;

    @Column(nullable = false)
    private LocalDate sincronizado;
}
