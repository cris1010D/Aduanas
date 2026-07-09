package cl.triskeledu.aduanas.reporte.config;

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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Seguridad de ms-reporte con RBAC completo.
 *
 * Politica de acceso:
 *   exportar/**          → solo SUPERVISOR (R.7/R.8)
 *   PUT/DELETE reportes  → solo SUPERVISOR
 *   GET/POST reportes    → SUPERVISOR y OFICIAL
 *   INSPECTOR            → sin acceso a reportes
 *   Swagger / actuator   → publico
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Desactiva el registro automatico de JwtAuthenticationFilter como servlet filter.
     * Sin esto el filtro ejecuta DOS veces: una como servlet filter y otra en la cadena
     * de Spring Security. El segundo registro es el unico que cuenta para RBAC.
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
            .sessionManagement(s ->
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Swagger y actuator: sin token
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/**", "/error").permitAll()

                // R.7/R.8: exportacion PDF/Excel solo SUPERVISOR
                .requestMatchers("/api/v1/reportes/exportar/**").hasRole("SUPERVISOR")

                // Edicion y eliminacion de reportes: solo SUPERVISOR
                .requestMatchers(HttpMethod.PUT,    "/api/v1/reportes/**").hasRole("SUPERVISOR")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/reportes/**").hasRole("SUPERVISOR")

                // Ver y crear reportes: SUPERVISOR y OFICIAL
                .requestMatchers(HttpMethod.GET,  "/api/v1/reportes/**").hasAnyRole("SUPERVISOR", "OFICIAL")
                .requestMatchers(HttpMethod.POST, "/api/v1/reportes/**").hasAnyRole("SUPERVISOR", "OFICIAL")

                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) ->
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado"))
                .accessDeniedHandler((req, res, e) ->
                    res.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Acceso denegado: rol insuficiente"))
            )
            // JWT filter en la posicion correcta de la cadena Spring Security
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
