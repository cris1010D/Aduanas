package cl.triskeledu.aduanas.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
    "cl.triskeledu.aduanas.auth",   // Escanea los controladores, servicios y DTOs de auth
    "cl.triskeledu.common"          // Escanea las propiedades, llaves y el provider de JWT
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cl.triskeledu.aduanas.auth")
public class AduanasAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AduanasAuthApplication.class, args);
    }
}