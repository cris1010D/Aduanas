package cl.triskeledu.aduanas.proceso.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "declaracion_aduana")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeclaracionAduana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rut_viajero", nullable = false, length = 12)
    private String rutViajero;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "paso_fronterizo", nullable = false, length = 40)
    private String pasoFronterizo;

    // ── Dinero en efectivo / instrumentos negociables ───────────────────
    @Column(name = "porta_dinero_efectivo", nullable = false)
    private Boolean portaDineroEfectivo;

    @Column(name = "monto_dinero", precision = 12, scale = 2)
    private BigDecimal montoDinero;

    @Column(name = "moneda_dinero", length = 10)
    private String monedaDinero; // USD | CLP | EUR ...

    // ── Mercancías afectas a impuestos ──────────────────────────────────
    @Column(name = "mercancias_afectas", nullable = false)
    private Boolean mercanciasAfectas;

    @Column(name = "descripcion_mercancias", length = 200)
    private String descripcionMercancias;

    // ── Franquicia de pasajeros ──────────────────────────────────────────
    @Column(name = "litros_alcohol")
    private Double litrosAlcohol;

    @Column(name = "cantidad_cigarrillos")
    private Integer cantidadCigarrillos;

    // ── Resultado calculado por el sistema ──────────────────────────────
    @Column(name = "excede_franquicia", nullable = false)
    private Boolean excedeFranquicia;

    @Column(nullable = false, length = 15)
    private String estado; // APROBADA | OBSERVADA
}