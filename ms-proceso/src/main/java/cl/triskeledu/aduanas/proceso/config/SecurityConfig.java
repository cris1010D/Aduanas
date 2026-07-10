package cl.triskeledu.aduanas.proceso.config;

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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Desactiva el registro automatico de JwtAuthenticationFilter como servlet filter.
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
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    "{\"status\":401,\"error\":\"Unauthorized\"," +
                            "\"message\":\"Token de autenticacion requerido\"," +
                            "\"path\":\"" + request.getRequestURI() + "\"}"
            );
        };
    }

    @Bean
    @SuppressWarnings("null")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/actuator/**", "/error").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // ── Viajeros ─────────────────────────────────────────────────
                        .requestMatchers(HttpMethod.GET,    "/api/v1/viajeros/**").hasAnyRole("SUPERVISOR", "OFICIAL", "INSPECTOR", "VIAJERO")
                        .requestMatchers(HttpMethod.POST,   "/api/v1/viajeros/**").hasAnyRole("SUPERVISOR", "OFICIAL", "VIAJERO")
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/viajeros/**").hasAnyRole("SUPERVISOR", "OFICIAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/viajeros/**").hasAnyRole("SUPERVISOR", "OFICIAL")

                        // ── Vehiculos (incluye TRANSPORTISTA — regla unica, sin duplicados) ──
                        .requestMatchers(HttpMethod.GET,    "/api/v1/vehiculos/**").hasAnyRole("SUPERVISOR", "OFICIAL", "INSPECTOR", "VIAJERO", "TRANSPORTISTA")
                        .requestMatchers(HttpMethod.POST,   "/api/v1/vehiculos/**").hasAnyRole("SUPERVISOR", "OFICIAL", "VIAJERO", "TRANSPORTISTA")
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/vehiculos/**").hasAnyRole("SUPERVISOR", "OFICIAL", "TRANSPORTISTA")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/vehiculos/**").hasAnyRole("SUPERVISOR", "OFICIAL")

                        // ── Movimientos (incluye TRANSPORTISTA en GET — regla unica) ────
                        .requestMatchers(HttpMethod.GET,    "/api/v1/movimientos/**").hasAnyRole("SUPERVISOR", "OFICIAL", "INSPECTOR", "VIAJERO", "TRANSPORTISTA")
                        .requestMatchers(HttpMethod.POST,   "/api/v1/movimientos/**").hasAnyRole("SUPERVISOR", "OFICIAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/movimientos/**").hasAnyRole("SUPERVISOR", "OFICIAL")

                        .requestMatchers("/api/v1/sag/**").hasAnyRole("SUPERVISOR", "OFICIAL")
                        .requestMatchers("/api/v1/pdi/**").hasAnyRole("SUPERVISOR", "OFICIAL")
                        .requestMatchers("/api/v1/aduana/**").hasAnyRole("SUPERVISOR", "OFICIAL", "INSPECTOR", "VIAJERO")
                        .requestMatchers("/api/v1/viajeros/menores/**").hasAnyRole("SUPERVISOR", "OFICIAL")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedEntryPoint())
                        .accessDeniedHandler((req, res, e) -> res.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}