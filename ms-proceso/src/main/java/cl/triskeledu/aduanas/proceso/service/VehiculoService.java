package cl.triskeledu.aduanas.proceso.service;

import cl.triskeledu.aduanas.proceso.config.KafkaTopicConfig;
import cl.triskeledu.aduanas.proceso.dto.VehiculoRequest;
import cl.triskeledu.aduanas.proceso.dto.VehiculoResponse;
import cl.triskeledu.aduanas.proceso.mapper.VehiculoMapper;
import cl.triskeledu.aduanas.proceso.model.Vehiculo;
import cl.triskeledu.aduanas.proceso.repository.VehiculoRepository;
import cl.triskeledu.common.event.VehiculoAdmitidoEvent;
import cl.triskeledu.common.exception.DuplicateResourceException;
import cl.triskeledu.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehiculoService {

    private static final int DIAS_VIGENCIA = 180;

    private final VehiculoRepository vehiculoRepository;
    private final VehiculoMapper vehiculoMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<VehiculoResponse> listarTodos() {
        log.info("Listando todos los vehiculos");
        return vehiculoMapper.toResponseList(vehiculoRepository.findAllByOrderByIdAsc());
    }

    public VehiculoResponse buscarPorId(Integer id) {
        log.info("Buscando vehiculo por id: {}", id);
        return vehiculoMapper.toResponse(getVehiculoById(id));
    }

    public VehiculoResponse buscarPorPlaca(String placa) {
        log.info("Buscando vehiculo por placa: {}", placa);
        return vehiculoMapper.toResponse(
            vehiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new EntityNotFoundException("Vehiculo", "placa", placa))
        );
    }

    /**
     * R.3 — Admision de vehiculo fronterizo.
     * Asigna fechaIngreso = hoy y calcula fechaVencimiento = hoy + 180 dias.
     * Publica VehiculoAdmitidoEvent en Kafka para notificar a ms-auditoria.
     */
    @Transactional
    @SuppressWarnings("null")
    public VehiculoResponse admitirVehiculo(VehiculoRequest request) {
        log.info("Admitiendo vehiculo con placa: {}", request.getPlaca());
        validarPlacaUnica(request.getPlaca());

        // Calculo de fechas — el cliente no decide, las impone el sistema
        LocalDate fechaIngreso     = LocalDate.now();
        LocalDate fechaVencimiento = fechaIngreso.plusDays(DIAS_VIGENCIA);

        Vehiculo vehiculo = Vehiculo.builder()
<<<<<<< HEAD
                .placa(request.getPlaca())
                .propietario(request.getPropietario())
                .fechaIngreso(fechaIngreso)
                .fechaVencimiento(fechaVencimiento)
                .rutPropietario(request.getRutPropietario())
                .esComercial(request.getEsComercial())
                .empresaTransportista(request.getEmpresaTransportista())
                .patenteRemolque(request.getPatenteRemolque())
                .manifiestoCarga(request.getManifiestoCarga())
                .marca(request.getMarca())
                .modelo(request.getModelo())
                .anio(request.getAnio())
                .build();
=======
            .placa(request.getPlaca())
            .propietario(request.getPropietario())
            .fechaIngreso(fechaIngreso)
            .fechaVencimiento(fechaVencimiento)
            .build();
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7

        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        log.info("Vehiculo {} admitido. Ingreso: {} | Vencimiento: {}",
            guardado.getPlaca(), guardado.getFechaIngreso(), guardado.getFechaVencimiento());

        // Publicar evento Kafka → ms-auditoria
        VehiculoAdmitidoEvent evento = VehiculoAdmitidoEvent.builder()
            .id(guardado.getId())
            .placa(guardado.getPlaca())
            .propietario(guardado.getPropietario())
            .fechaIngreso(guardado.getFechaIngreso().toString())
            .fechaVencimiento(guardado.getFechaVencimiento().toString())
            .build();

        log.info("Publicando VehiculoAdmitidoEvent para placa: {}", guardado.getPlaca());
        kafkaTemplate.send(KafkaTopicConfig.VEHICULO_ADMITIDO_TOPIC, guardado.getPlaca(), evento);
        log.info("VehiculoAdmitidoEvent publicado correctamente");

        return vehiculoMapper.toResponse(guardado);
    }

    @Transactional
    //@SuppressWarnings("null")
    public VehiculoResponse actualizar(Integer id, VehiculoRequest request) {
        log.info("Actualizando vehiculo id: {}", id);
        Vehiculo vehiculo = getVehiculoById(id);
        if (!vehiculo.getPlaca().equals(request.getPlaca())) {
            validarPlacaUnica(request.getPlaca());
        }
        vehiculoMapper.updateEntity(request, vehiculo);
        return vehiculoMapper.toResponse(vehiculoRepository.save(vehiculo));
    }

    @Transactional
    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        log.info("Eliminando vehiculo id: {}", id);
        vehiculoRepository.delete(getVehiculoById(id));
    }
    @SuppressWarnings("null")
    private Vehiculo getVehiculoById(Integer id) {
        return vehiculoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehiculo", "id", id));
    }

    private void validarPlacaUnica(String placa) {
        if (vehiculoRepository.existsByPlaca(placa)) {
            throw new DuplicateResourceException("Vehiculo", "placa", placa, placa);
        }
    }
<<<<<<< HEAD

    public List<VehiculoResponse> listarPorRutPropietario(String rut) {
        log.info("Listando vehiculos del propietario/transportista: {}", rut);
        return vehiculoMapper.toResponseList(vehiculoRepository.findByRutPropietario(rut));
    }
=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
}
