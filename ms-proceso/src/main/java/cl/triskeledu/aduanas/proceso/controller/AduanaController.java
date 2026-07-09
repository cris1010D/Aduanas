package cl.triskeledu.aduanas.proceso.controller;

import cl.triskeledu.aduanas.proceso.dto.DeclaracionAduanaRequest;
import cl.triskeledu.aduanas.proceso.dto.DeclaracionAduanaResponse;
import cl.triskeledu.aduanas.proceso.service.DeclaracionAduanaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Aduana", description = "Declaracion de equipaje, valores, moneda y franquicias")
@RestController
@RequestMapping("/api/v1/aduana")
@RequiredArgsConstructor
public class AduanaController {

    private final DeclaracionAduanaService declaracionAduanaService;

    @Operation(summary = "Listar todas las declaraciones de aduana")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/declaracion")
    public ResponseEntity<List<DeclaracionAduanaResponse>> listarTodas() {
        return ResponseEntity.ok(declaracionAduanaService.listarTodas());
    }

    @Operation(summary = "Listar declaraciones de aduana de un viajero")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping("/declaracion/viajero/{rut}")
    public ResponseEntity<List<DeclaracionAduanaResponse>> listarPorViajero(@PathVariable String rut) {
        return ResponseEntity.ok(declaracionAduanaService.listarPorViajero(rut));
    }

    @Operation(summary = "Buscar declaracion de aduana por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Declaracion encontrada"),
            @ApiResponse(responseCode = "404", description = "Declaracion no encontrada")
    })
    @GetMapping("/declaracion/{id}")
    public ResponseEntity<DeclaracionAduanaResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(declaracionAduanaService.buscarPorId(id));
    }

    @Operation(summary = "Tramitar declaracion aduanera (R.6 - evalua franquicia de dinero, alcohol, tabaco y mercancias)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Declaracion creada - estado APROBADA u OBSERVADA segun franquicia"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping("/declaracion")
    public ResponseEntity<DeclaracionAduanaResponse> tramitar(@Valid @RequestBody DeclaracionAduanaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(declaracionAduanaService.tramitar(request));
    }
}