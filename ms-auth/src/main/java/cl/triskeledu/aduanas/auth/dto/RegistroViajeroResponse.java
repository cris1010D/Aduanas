package cl.triskeledu.aduanas.auth.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegistroViajeroResponse {

    private Integer id;
    private String rut;
    private String nombre;
    private String rol;
    private String token;   // JWT listo para usar — el viajero queda logueado al registrarse
    private String mensaje;
}
