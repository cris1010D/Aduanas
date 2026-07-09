package cl.triskeledu.aduanas.auth;
/**
 * ADVERTENCIA: Este componente es exclusivamente para entorno de DESARROLLO Y PRUEBAS.
 *
 * En cada arranque de ms-auth, resetea automáticamente el password de los 9 oficiales
 * de prueba (RUTS_PRUEBA) a "123456", calculando el hash BCrypt en tiempo real con el
 * PasswordEncoder real de Spring. Esto evita el problema de hashes hardcodeados
 * incorrectos en los scripts SQL de inicialización (init-multi-db/02-create_auth.sql),
 * haciendo que el sistema se "autorrepare" sin intervención manual.
 *
 * NO debe usarse en un ambiente de producción real, porque resetea contraseñas
 * conocidas en cada arranque del servicio. Antes de un despliegue productivo,
 * este componente debería eliminarse o restringirse con @Profile("dev").
 */
import cl.triskeledu.aduanas.auth.repository.OficialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private static final String PASSWORD_PRUEBA = "123456";

    private static final List<String> RUTS_PRUEBA = List.of(
            "12345678-9", "98765432-1", "11111111-1", "22222222-2",
            "33333333-3", "44444444-4", "55555555-5", "66666666-6", "77777777-7"
    );

    private final OficialRepository oficialRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner sincronizarPasswordsDePrueba() {
        return args -> {
            String hashCorrecto = passwordEncoder.encode(PASSWORD_PRUEBA);
            RUTS_PRUEBA.forEach(rut ->
                oficialRepository.findByRut(rut).ifPresent(oficial -> {
                    oficial.setPassword(hashCorrecto);
                    oficialRepository.save(oficial);
                })
            );
            log.info("Passwords de prueba sincronizados al arrancar ms-auth ({} oficiales).", RUTS_PRUEBA.size());
        };
    }
}
