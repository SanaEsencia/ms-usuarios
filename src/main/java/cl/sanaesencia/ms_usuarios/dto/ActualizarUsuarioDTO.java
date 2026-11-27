package cl.sanaesencia.ms_usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Datos que se pueden editar desde el perfil (el correo y el rol no cambian)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarUsuarioDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+$", message = "El nombre solo puede tener letras y espacios")
    private String nombre;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^(\\+?56)?9[0-9]{8}$", message = "El teléfono debe tener el formato +56912345678")
    private String telefono;
}
