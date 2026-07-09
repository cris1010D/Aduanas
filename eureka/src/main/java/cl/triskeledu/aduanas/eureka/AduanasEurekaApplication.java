package cl.triskeledu.aduanas.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class AduanasEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AduanasEurekaApplication.class, args);
    }
}
