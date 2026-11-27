package cl.sanaesencia.ms_usuarios.Controller;

import cl.sanaesencia.ms_usuarios.Service.UsuarioService;
import cl.sanaesencia.ms_usuarios.dto.ActualizarUsuarioDTO;
import cl.sanaesencia.ms_usuarios.dto.LoginRequestDTO;
import cl.sanaesencia.ms_usuarios.dto.RegistroRequestDTO;
import cl.sanaesencia.ms_usuarios.dto.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "USUARIOS", description = "REGISTRO, LOGIN Y PERFIL DE USUARIOS")
@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "OBTENER TODOS LOS USUARIOS", description = "Retorna la lista de usuarios (administración)")
    @ApiResponse(responseCode = "200", description = "LISTA OBTENIDA CON ÉXITO")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerUsuarios() {
        log.info("GET /api/usuarios - LISTAR TODOS");
        return ResponseEntity.ok(usuarioService.obtenerUsuarios());
    }

    @Operation(summary = "OBTENER USUARIO POR ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "USUARIO ENCONTRADO"),
            @ApiResponse(responseCode = "404", description = "USUARIO NO ENCONTRADO")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuario(@PathVariable Long id) {
        log.info("GET /api/usuarios/{} - BUSCAR POR ID", id);
        return ResponseEntity.ok(usuarioService.obtenerUsuario(id));
    }

    @Operation(summary = "REGISTRAR PACIENTE", description = "Crea una cuenta nueva con rol PACIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "USUARIO REGISTRADO"),
            @ApiResponse(responseCode = "400", description = "DATOS INVÁLIDOS"),
            @ApiResponse(responseCode = "409", description = "EL CORREO YA ESTÁ REGISTRADO")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO dto) {
        log.info("POST /api/usuarios - REGISTRAR correo={}", dto.getCorreo());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(dto));
    }

    @Operation(summary = "INICIAR SESIÓN", description = "Valida correo y contraseña y retorna los datos del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "LOGIN EXITOSO"),
            @ApiResponse(responseCode = "400", description = "DATOS INVÁLIDOS"),
            @ApiResponse(responseCode = "401", description = "CORREO O CONTRASEÑA INCORRECTOS")
    })
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        log.info("POST /api/usuarios/login - LOGIN correo={}", dto.getCorreo());
        return ResponseEntity.ok(usuarioService.login(dto));
    }

    @Operation(summary = "ACTUALIZAR PERFIL", description = "Modifica el nombre y el teléfono de un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "USUARIO ACTUALIZADO"),
            @ApiResponse(responseCode = "400", description = "DATOS INVÁLIDOS"),
            @ApiResponse(responseCode = "404", description = "USUARIO NO ENCONTRADO")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarUsuarioDTO dto) {
        log.info("PUT /api/usuarios/{} - ACTUALIZAR", id);
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    @Operation(summary = "ELIMINAR USUARIO")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "USUARIO ELIMINADO"),
            @ApiResponse(responseCode = "404", description = "USUARIO NO ENCONTRADO")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/usuarios/{} - ELIMINAR", id);
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
