package cl.triskeledu.aduanas.proceso.controller;

import cl.triskeledu.aduanas.proceso.dto.AutorizacionMenorResponse;
import cl.triskeledu.aduanas.proceso.dto.MenorValidacionRequest;
import cl.triskeledu.aduanas.proceso.service.MenorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Validacion Menores", description = "Validacion de salida de menores al exterior (R.2)")
@RestController
@RequestMapping("/api/v1/viajeros/menores")
@RequiredArgsConstructor
public class MenorController {

    private final MenorService menorService;

    @Operation(summary = "Validar autorizacion de salida de menor al exterior (R.2 - consulta ms-menores y ms-notaria)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Autorizacion validada y registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "403", description = "Menor no autorizado para salir del pais")
    })
    @PostMapping("/validar")
    public ResponseEntity<AutorizacionMenorResponse> validarSalidaMenor(
            @Valid @RequestBody MenorValidacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menorService.validarSalidaMenor(request));
    }
}
