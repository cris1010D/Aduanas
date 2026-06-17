package cl.triskeledu.aduanas.sag.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_declaracion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemDeclaracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_declaracion", nullable = false)
    private Integer idDeclaracion;

    @Column(nullable = false, length = 80)
    private String descripcion;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, length = 15)
    private String riesgo;
}
