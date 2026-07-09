package cl.triskeledu.aduanas.auth.controller;

<<<<<<< HEAD
import cl.triskeledu.aduanas.auth.dto.*;
=======
import cl.triskeledu.aduanas.auth.dto.LoginRequest;
import cl.triskeledu.aduanas.auth.dto.LoginResponse;
import cl.triskeledu.aduanas.auth.dto.RegistroViajeroRequest;
import cl.triskeledu.aduanas.auth.dto.RegistroViajeroResponse;
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
import cl.triskeledu.aduanas.auth.service.OficialService;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Autenticacion", description = "Login y generacion de tokens JWT para oficiales de aduana")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OficialService oficialService;

    @Operation(summary = "Autenticar oficial y obtener token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autenticacion exitosa - retorna JWT"),
        @ApiResponse(responseCode = "400", description = "Credenciales invalidas o campos faltantes"),
        @ApiResponse(responseCode = "401", description = "RUT o password incorrecto")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Solicitud de login recibida para usuario: {}", request.getUsername());
        LoginResponse response = oficialService.login(request);
        log.info("Login exitoso para usuario: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registro de viajero (publico, sin token)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Viajero registrado correctamente - retorna JWT"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos o contraseñas no coinciden"),
        @ApiResponse(responseCode = "409", description = "Ya existe un viajero con ese RUT")
    })
    @PostMapping("/registro")
    public ResponseEntity<RegistroViajeroResponse> registro(@Valid @RequestBody RegistroViajeroRequest request) {
        log.info("Solicitud de registro de viajero: {}", request.getRut());
        RegistroViajeroResponse response = oficialService.registrarViajero(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
<<<<<<< HEAD

    @Operation(summary = "Registro de transportista/chofer comercial (publico, sin token)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transportista registrado correctamente - retorna JWT"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o contraseñas no coinciden"),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese RUT")
    })
    @PostMapping("/registro-transportista")
    public ResponseEntity<RegistroTransportistaResponse> registroTransportista(
            @Valid @RequestBody RegistroTransportistaRequest request) {
        log.info("Solicitud de registro de transportista: {}", request.getRut());
        RegistroTransportistaResponse response = oficialService.registrarTransportista(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
=======
>>>>>>> ea01fb5f3b7f052c39b23f480a9f45e8e152cad7
}
