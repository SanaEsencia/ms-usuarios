package cl.sanaesencia.ms_usuarios.Service;

import cl.sanaesencia.ms_usuarios.Repository.UsuarioRepository;
import cl.sanaesencia.ms_usuarios.dto.ActualizarUsuarioDTO;
import cl.sanaesencia.ms_usuarios.dto.LoginRequestDTO;
import cl.sanaesencia.ms_usuarios.dto.RegistroRequestDTO;
import cl.sanaesencia.ms_usuarios.dto.UsuarioResponseDTO;
import cl.sanaesencia.ms_usuarios.exception.CorreoDuplicadoException;
import cl.sanaesencia.ms_usuarios.exception.CredencialesInvalidasException;
import cl.sanaesencia.ms_usuarios.model.Rol;
import cl.sanaesencia.ms_usuarios.model.Usuario;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// Lógica de negocio de los usuarios
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Convierte la entidad en el DTO que se envía a la app (sin la contraseña)
    private UsuarioResponseDTO mapToDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getTelefono(),
                usuario.getRol().name(),
                usuario.getTerapeutaId()
        );
    }

    // Los correos se guardan y comparan siempre en minúsculas y sin espacios
    private String normalizarCorreo(String correo) {
        return correo.trim().toLowerCase();
    }

    public List<UsuarioResponseDTO> obtenerUsuarios() {
        log.info("LISTANDO TODOS LOS USUARIOS");
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO obtenerUsuario(Long id) {
        log.info("BUSCANDO USUARIO CON ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario con id " + id + " no encontrado"));
        return mapToDTO(usuario);
    }

    // Registro desde la app: siempre crea un PACIENTE
    public UsuarioResponseDTO registrar(RegistroRequestDTO dto) {
        String correo = normalizarCorreo(dto.getCorreo());
        log.info("REGISTRANDO NUEVO USUARIO: {}", correo);

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new CorreoDuplicadoException("Ya existe una cuenta con ese correo");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre().trim());
        usuario.setCorreo(correo);
        usuario.setClave(passwordEncoder.encode(dto.getClave()));
        usuario.setTelefono(dto.getTelefono().trim().replace(" ", ""));
        usuario.setRol(Rol.PACIENTE);
        usuario.setTerapeutaId(0);

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("USUARIO REGISTRADO EXITOSAMENTE CON ID: {}", guardado.getId());
        return mapToDTO(guardado);
    }

    // Revisa el correo y la contraseña. El mensaje es el mismo en ambos casos por seguridad
    public UsuarioResponseDTO login(LoginRequestDTO dto) {
        String correo = normalizarCorreo(dto.getCorreo());
        log.info("INTENTO DE LOGIN PARA: {}", correo);

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(dto.getClave(), usuario.getClave())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
        }

        log.info("LOGIN EXITOSO PARA: {}", correo);
        return mapToDTO(usuario);
    }

    // Edición del perfil: solo nombre y teléfono
    public UsuarioResponseDTO actualizar(Long id, ActualizarUsuarioDTO dto) {
        log.info("ACTUALIZANDO USUARIO CON ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario con id " + id + " no encontrado"));

        usuario.setNombre(dto.getNombre().trim());
        usuario.setTelefono(dto.getTelefono().trim().replace(" ", ""));

        log.info("USUARIO CON ID {} ACTUALIZADO", id);
        return mapToDTO(usuarioRepository.save(usuario));
    }

    public void eliminar(Long id) {
        log.info("ELIMINANDO USUARIO CON ID: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Usuario con id " + id + " no encontrado");
        }
        usuarioRepository.deleteById(id);
        log.info("USUARIO CON ID {} ELIMINADO", id);
    }
}
