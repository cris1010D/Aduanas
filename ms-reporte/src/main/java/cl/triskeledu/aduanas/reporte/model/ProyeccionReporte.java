package cl.triskeledu.aduanas.reporte.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "proyeccion_reporte")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProyeccionReporte {

    @Id
    @Column(length = 30)
    private String tipo;

    @Column(name = "ultimo_generado", nullable = false)
    private LocalDate ultimoGenerado;

    @Column(name = "rut_oficial", nullable = false, length = 12)
    private String rutOficial;

    @Column(nullable = false, length = 10)
    private String formato;

    @Column(nullable = false)
    private LocalDate sincronizado;
}
