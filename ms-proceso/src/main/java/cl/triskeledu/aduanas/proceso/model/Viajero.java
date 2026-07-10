package cl.triskeledu.aduanas.proceso.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    // ── Documento de viaje (migración avanzada) ─────────────────────────
    @Column(name = "tipo_documento", length = 20)
    private String tipoDocumento; // PASAPORTE | CEDULA | DNI | SALVOCONDUCTO

    @Column(name = "pais_emisor_documento", length = 40)
    private String paisEmisorDocumento;

    @Column(name = "fecha_emision_documento")
    private LocalDate fechaEmisionDocumento;

    @Column(name = "fecha_vencimiento_documento")
    private LocalDate fechaVencimientoDocumento;

    // ── Contacto ─────────────────────────────────────────────────────────
    @Column(length = 100)
    private String email;

    @Column(length = 25)
    private String telefono;

    // ── Logística del viaje ─────────────────────────────────────────────
    @Column(name = "direccion_estadia", length = 160)
    private String direccionEstadia;

    @Column(name = "proposito_viaje", length = 20)
    private String propositoViaje; // TURISMO | NEGOCIOS | TRABAJO | RESIDENCIA | TRANSITO

    // ── Contacto de emergencia ──────────────────────────────────────────
    @Column(name = "emergencia_nombre", length = 80)
    private String emergenciaNombre;

    @Column(name = "emergencia_parentesco", length = 40)
    private String emergenciaParentesco;

    @Column(name = "emergencia_telefono", length = 25)
    private String emergenciaTelefono;

    // ── Salud y seguro ───────────────────────────────────────────────────
    @Column(name = "seguro_vigente")
    private Boolean seguroVigente;

    @Column(name = "seguro_proveedor", length = 80)
    private String seguroProveedor;
}