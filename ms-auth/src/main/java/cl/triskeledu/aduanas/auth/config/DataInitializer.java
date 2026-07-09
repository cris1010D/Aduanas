package cl.triskeledu.aduanas.auth.config;

import cl.triskeledu.aduanas.auth.model.Oficial;
import cl.triskeledu.aduanas.auth.repository.OficialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Inicializa y sincroniza usuarios de prueba al arrancar.
 * - Si la tabla está vacía: crea los 9 usuarios base.
 * - Siempre: resetea la contraseña de todos a "123456" y activa los usuarios base.
 * Password de todos: 123456
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final OficialRepository oficialRepository;
    private final PasswordEncoder passwordEncoder;

    // RUTs de funcionarios base
    private static final List<String> RUTS_BASE = List.of(
        "12345678-9", "98765432-1", "11111111-1",
        "22222222-2", "33333333-3", "44444444-4",
        "55555555-5", "66666666-6", "77777777-7"
    );


    @Override
    @SuppressWarnings("null")
    public void run(ApplicationArguments args) {
        String hash = passwordEncoder.encode("123456");

        // ── Funcionarios base ──────────────────────────────────────────────
        List<String> rutsExistentes = oficialRepository.findAll().stream()
            .map(Oficial::getRut).toList();

        List<Oficial> funcionarios = List.of(
            build("12345678-9", "Carlos Rojas",    "SUPERVISOR", true, hash),
            build("98765432-1", "Ana Pizarro",     "OFICIAL",    true, hash),
            build("11111111-1", "Luis Mendez",     "SUPERVISOR", true, hash),
            build("22222222-2", "Maria Soto",      "INSPECTOR",  true, hash),
            build("33333333-3", "Pedro Fuentes",   "OFICIAL",    true, hash),
            build("44444444-4", "Rosa Herrera",    "SUPERVISOR", true, hash),
            build("55555555-5", "Juan Carrasco",   "INSPECTOR",  true, hash),
            build("66666666-6", "Claudia Vera",    "OFICIAL",    true, hash),
            build("77777777-7", "Sergio Espinoza", "OFICIAL",    true, hash)
        );
        funcionarios.forEach(f -> {
            if (!rutsExistentes.contains(f.getRut())) {
                oficialRepository.save(f);
            } else {
                oficialRepository.findAll().stream()
                    .filter(o -> o.getRut().equals(f.getRut()))
                    .findFirst().ifPresent(o -> {
                        o.setPassword(hash); o.setActivo(true);
                        oficialRepository.save(o);
                    });
            }
        });
        log.info("[DataInitializer] 9 funcionarios base sincronizados (password: 123456)");

        // ── Viajeros semilla (mismos que ms-proceso) ───────────────────────
        List<String[]> viajerosSemilla = List.of(
            new String[]{"10111213-4", "Jorge Alvarez"},
            new String[]{"20212223-5", "Sofia Reyes"},
            new String[]{"30313233-6", "Diego Castro"},
            new String[]{"40414243-7", "Valentina Mora"},
            new String[]{"50515253-8", "Andres Lagos"},
            new String[]{"60616263-9", "Camila Torres"},
            new String[]{"70717273-K", "Ricardo Nunez"},
            new String[]{"80818283-1", "Patricia Gomez"},
            new String[]{"90919293-2", "Martin Vargas"}
        );
        viajerosSemilla.forEach(v -> {
            if (!rutsExistentes.contains(v[0])) {
                oficialRepository.save(build(v[0], v[1], "VIAJERO", true, hash));
            }
        });
        log.info("[DataInitializer] 9 viajeros semilla sincronizados (password: 123456)");

        log.info("[DataInitializer] Usuarios disponibles (password: 123456):");
        oficialRepository.findAll().stream()
            .filter(o -> RUTS_BASE.contains(o.getRut()))
            .forEach(o -> log.info("  RUT={} | nombre={} | rol={}", o.getRut(), o.getNombre(), o.getRol()));
    }

    private Oficial build(String rut, String nombre, String rol, boolean activo, String hash) {
        Oficial o = new Oficial();
        o.setRut(rut);
        o.setNombre(nombre);
        o.setRol(rol);
        o.setActivo(activo);
        o.setPassword(hash);
        return o;
    }
}