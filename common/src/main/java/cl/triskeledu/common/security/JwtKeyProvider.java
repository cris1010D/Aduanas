package cl.triskeledu.common.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

@Component
public class JwtKeyProvider {

    private final JwtProperties jwtProperties;

    public JwtKeyProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * Recupera el secreto en Base64 desde las propiedades del sistema,
     * lo decodifica y genera una clave criptográfica robusta para HMAC-SHA256.
     */
    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}