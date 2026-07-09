package cl.triskeledu.aduanas.auth.config;

import cl.triskeledu.common.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    /**
     * Evita que Spring Boot registre JwtAuthenticationFilter como servlet filter
     * standalone. Sin esto, el filtro se ejecuta ANTES de que SecurityContextHolderFilter
     * inicialice el contexto de Spring Security, que luego lo sobreescribe con uno vacio
     * (politica STATELESS), dejando al usuario como anonimo y devolviendo 401 en vez de 403.
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> disableJwtFilterAutoRegistration(
            JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration =
            new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    @SuppressWarnings("null")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            // RBAC: SUPERVISOR=total | OFICIAL=operativo | INSPECTOR=fiscalización | VIAJERO=autogestión
            .authorizeHttpRequests(auth -> auth
                // Swagger, actuator: sin token
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/**", "/error").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()

                // Login (POST /sesiones): sin token — endpoint de autenticación para TODOS los roles
                .requestMatchers(HttpMethod.POST, "/api/v1/sesiones/**").permitAll()

                // Gestión de oficiales: SUPERVISOR crea/edita/elimina; OFICIAL solo lee
                .requestMatchers(HttpMethod.GET,    "/api/v1/oficiales/**").hasAnyRole("SUPERVISOR", "OFICIAL")
                .requestMatchers(HttpMethod.POST,   "/api/v1/oficiales/**").hasRole("SUPERVISOR")
                .requestMatchers(HttpMethod.PUT,    "/api/v1/oficiales/**").hasRole("SUPERVISOR")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/oficiales/**").hasRole("SUPERVISOR")

                // Sesiones (GET, DELETE): funcionarios solamente
                .requestMatchers("/api/v1/sesiones/**").hasAnyRole("SUPERVISOR", "OFICIAL", "INSPECTOR")

                // VIAJERO: solo puede autenticarse (POST /sesiones ya es permitAll)
                // El resto de endpoints del sistema no son accesibles para VIAJERO desde ms-auth
                .anyRequest().hasAnyRole("SUPERVISOR", "OFICIAL", "INSPECTOR")
            )

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) ->
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token requerido"))
                .accessDeniedHandler((req, res, e) ->
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: rol insuficiente"))
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}