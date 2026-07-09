package cl.triskeledu.aduanas.proceso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
    "cl.triskeledu.aduanas.proceso", // Escanea el propio microservicio
    "cl.triskeledu.common"           // Escanea el filtro, interceptor y componentes de seguridad compartidos
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cl.triskeledu.aduanas.proceso") // Asegura el escaneo de clientes Feign locales si existen
public class AduanasProcesoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AduanasProcesoApplication.class, args);
    }
}