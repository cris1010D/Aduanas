package cl.triskeledu.aduanas.datos.config;

import cl.triskeledu.common.security.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Configuracion de seguridad para ms-datos (defensa en profundidad).
 *
 * Valida JWT en cada request. Los tokens llegan propagados desde el gateway
 * via FeignClientInterceptor (modulo common). Swagger y actuator permanecen
 * abiertos para herramientas de observabilidad.
 *
 * FilterRegistrationBean.setEnabled(false): evita que Spring Boot registre
 * JwtAuthenticationFilter como servlet filter standalone (doble ejecucion).
 * El filtro se agrega manualmente a la cadena de Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> disableJwtFilterAutoRegistration(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration =
                new FilterRegistrationBean<>(jwtAuthenticationFilter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    @SuppressWarnings("null")
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/error").permitAll()
                // Configuracion del sistema: solo SUPERVISOR
                .requestMatchers("/api/v1/configuraciones/**").hasRole("SUPERVISOR")
                // Cache Redis: SUPERVISOR y OFICIAL pueden consultar y limpiar
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/cache/**")
                    .hasAnyRole("SUPERVISOR", "OFICIAL")
                .requestMatchers("/api/v1/cache/**").hasRole("SUPERVISOR")
                .anyRequest().authenticated());
        http.exceptionHandling(ex -> ex
            .authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
            .accessDeniedHandler((req, res, e) -> res.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden")));
        return http.build();
    }
}
