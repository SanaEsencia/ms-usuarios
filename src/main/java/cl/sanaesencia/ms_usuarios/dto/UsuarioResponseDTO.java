package cl.sanaesencia.ms_usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lo que el microservicio devuelve a la app. Nunca incluye la contraseña
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String rol;
    private Integer terapeutaId;
}
