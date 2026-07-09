package cl.triskeledu.aduanas.auth.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegistroTransportistaResponse {

    private Integer id;
    private String rut;
    private String nombre;
    private String empresa;
    private String rol;
    private String token;
    private String mensaje;
}