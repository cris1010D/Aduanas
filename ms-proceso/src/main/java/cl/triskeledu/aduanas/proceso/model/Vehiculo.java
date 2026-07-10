package cl.triskeledu.aduanas.proceso.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "vehiculo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 20)
    private String placa;

    @Column(nullable = false, length = 80)
    private String propietario;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    // ── Campos nuevos para vehículos comerciales / transportistas ──────────
    @Column(name = "rut_propietario", length = 12)
    private String rutPropietario;

    @Column(name = "es_comercial")
    @Builder.Default
    private Boolean esComercial = false;

    @Column(name = "empresa_transportista", length = 100)
    private String empresaTransportista;

    @Column(name = "patente_remolque", length = 20)
    private String patenteRemolque;

    @Column(name = "manifiesto_carga", length = 500)
    private String manifiestoCarga;

    @Column(length = 60)
    private String marca;

    @Column(length = 60)
    private String modelo;

    @Column(name = "anio")
    private Integer anio;
}