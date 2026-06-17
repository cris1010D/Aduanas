package cl.triskeledu.aduanas.reporte.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "detalle_reporte")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class DetalleReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_reporte", nullable = false)
    private Integer idReporte;

    @Column(nullable = false, length = 120)
    private String descripcion;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "fecha_dato", nullable = false)
    private LocalDate fechaDato;
}
