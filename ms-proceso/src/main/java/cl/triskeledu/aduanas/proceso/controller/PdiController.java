package cl.triskeledu.aduanas.proceso.controller;

import cl.triskeledu.aduanas.proceso.dto.AntecedenteConsultadoResponse;
import cl.triskeledu.aduanas.proceso.dto.ConsultaPdiRequest;
import cl.triskeledu.aduanas.proceso.service.PdiConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PDI - Proceso", description = "Consulta de antecedentes PDI en el flujo fronterizo")
@RestController
@RequestMapping("/api/v1/pdi")
@RequiredArgsConstructor
public class PdiController {

    private final PdiConsultaService pdiConsultaService;

    @Operation(summary = "Consultar antecedentes PDI de un viajero (SLA <= 2000ms)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consulta exitosa - retorna resultado SIN_REGISTROS o CON_REGISTROS"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "500", description = "ms-pdi supero el SLA de 2000ms")
    })
    @PostMapping("/consultar")
    public ResponseEntity<AntecedenteConsultadoResponse> consultar(
            @Valid @RequestBody ConsultaPdiRequest request) {
        return ResponseEntity.ok(pdiConsultaService.consultarAntecedentes(request));
    }
}
