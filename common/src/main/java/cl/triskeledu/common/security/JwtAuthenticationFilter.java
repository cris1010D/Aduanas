package cl.triskeledu.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // 1. Obtener el token de la cabecera
        String token = obtenerTokenDeSolicitud(request);

        // 2. Validar el token y establecer la seguridad si es correcto
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // 3. Extraer el rol del claim JWT y convertirlo en GrantedAuthority.
            // El claim "rol" es configurado por ms-auth: claims.put("rol", oficial.getRol())
            // Ejemplos de valor: "SUPERVISOR", "OFICIAL", "INSPECTOR"
            // Spring Security requiere prefijo "ROLE_" para que hasRole("SUPERVISOR") funcione.
            String rol = jwtTokenProvider.getRolFromToken(token);
            List<GrantedAuthority> authorities = StringUtils.hasText(rol)
                ? List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                : Collections.emptyList();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null, authorities
            );
            
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 4. Guardar la identidad en el Contexto de Seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para limpiar la palabra "Bearer " del token
    private String obtenerTokenDeSolicitud(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}