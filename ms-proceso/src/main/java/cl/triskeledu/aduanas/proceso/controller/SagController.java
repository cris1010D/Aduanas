package cl.triskeledu.aduanas.proceso.controller;

import cl.triskeledu.aduanas.proceso.dto.DeclaracionSagResponse;
import cl.triskeledu.aduanas.proceso.dto.TramiteSagRequest;
import cl.triskeledu.aduanas.proceso.service.SagOrquestadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SAG - Proceso", description = "Declaraciones fitosanitarias SAG en el flujo fronterizo")
@RestController
@RequestMapping("/api/v1/sag")
@RequiredArgsConstructor
public class SagController {

    private final SagOrquestadorService sagOrquestadorService;

    @Operation(summary = "Tramitar declaracion SAG fitosanitaria (R.5 - evalua riesgo de items)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Declaracion creada - estado APROBADA o CUARENTENA segun riesgo"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping("/tramite")
    public ResponseEntity<DeclaracionSagResponse> tramitar(
            @Valid @RequestBody TramiteSagRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sagOrquestadorService.tramitarDeclaracionSag(request));
    }
}
