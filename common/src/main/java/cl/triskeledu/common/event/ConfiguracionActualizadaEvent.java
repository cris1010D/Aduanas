package cl.triskeledu.common.event;

import lombok.*;

/**
 * Evento publicado en Kafka cuando una configuracion global es modificada.
 *
 * Topico: datos.configuracion.updated
 *
 * Los microservicios suscritos (ms-auth, ms-sag, ms-proceso, etc.) pueden
 * reaccionar para refrescar sus parametros en caliente sin reiniciar.
 *
 * Campos:
 *   clave          : identificador unico de la configuracion (ej: sag.riesgo.niveles)
 *   valorAnterior  : valor que tenia ANTES de la actualizacion
 *   valorNuevo     : valor que tiene AHORA (el actualizado)
 *   msDuenio       : MS propietario logico del parametro
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionActualizadaEvent implements DomainEvent {

    private String clave;
    private String valorAnterior;
    private String valorNuevo;
    private String msDuenio;

    @Override
    public String getAggregateId() {
        return clave;
    }
}
