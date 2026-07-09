package cl.triskeledu.aduanas.auditoria.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_log")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DetalleLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_log", nullable = false)
    private Integer idLog;

    @Column(nullable = false, length = 40)
    private String entidad;

    @Column(nullable = false, length = 40)
    private String campo;

    @Column(name = "valor_nuevo", nullable = false, length = 80)
    private String valorNuevo;
}
