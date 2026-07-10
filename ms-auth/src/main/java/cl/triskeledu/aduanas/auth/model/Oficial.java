package cl.triskeledu.aduanas.auth.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "oficial")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Oficial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 30)
    private String rol;

    @Column(nullable = false)
    private Boolean activo;

    // Campo agregado para almacenar la contraseña cifrada (hash BCrypt)
    @Column(nullable = false, length = 255)
    private String password;

    // Empresa transportista. Solo aplica a usuarios con rol TRANSPORTISTA;
    // queda en null para SUPERVISOR/OFICIAL/INSPECTOR/VIAJERO.
    @Column(length = 100)
    private String empresa;
}