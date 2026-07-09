package cl.triskeledu.common.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 1. Obtener los atributos de la petición HTTP actual (la que originó el cliente)
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            
            // 2. Extraer el token de la cabecera de la petición original
            String authorizationToken = request.getHeader(AUTHORIZATION_HEADER);
            
            // 3. Si existe un token, inyectarlo en la nueva petición Feign que va hacia el otro microservicio
            if (authorizationToken != null) {
                requestTemplate.header(AUTHORIZATION_HEADER, authorizationToken);
            }
        }
    }
}