package cl.triskeledu.aduanas.auth.config;

import cl.triskeledu.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilitar CSRF ya que no usamos cookies/sesiones de navegador
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Configurar la política de sesión como estricta STATELESS
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 3. Configuración de reglas de autorización de rutas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/oficiales/auth/login", "/api/v1/auth/**").permitAll() // Endpoints de Login abiertos
                .requestMatchers("/actuator/**").permitAll() // Monitoreo libre para Spring Boot Actuator
                .anyRequest().authenticated() // Cualquier otra ruta CRUD requiere token válido
            )
            
            // 4. Registrar nuestro filtro personalizado de JWT antes del filtro nativo de Spring
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean requerido por OficialService para matches() e inscripciones futuras
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}