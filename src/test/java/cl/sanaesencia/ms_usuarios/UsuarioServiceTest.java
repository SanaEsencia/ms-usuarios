package cl.sanaesencia.ms_usuarios;

import cl.sanaesencia.ms_usuarios.Repository.UsuarioRepository;
import cl.sanaesencia.ms_usuarios.Service.UsuarioService;
import cl.sanaesencia.ms_usuarios.dto.LoginRequestDTO;
import cl.sanaesencia.ms_usuarios.dto.RegistroRequestDTO;
import cl.sanaesencia.ms_usuarios.dto.UsuarioResponseDTO;
import cl.sanaesencia.ms_usuarios.exception.CorreoDuplicadoException;
import cl.sanaesencia.ms_usuarios.exception.CredencialesInvalidasException;
import cl.sanaesencia.ms_usuarios.model.Rol;
import cl.sanaesencia.ms_usuarios.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Pruebas de la lógica del servicio. El repositorio se simula con Mockito (no usa la base de datos)
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);
    }

    @Test
    void registrarGuardaPacienteConClaveCifrada() {
        RegistroRequestDTO dto = new RegistroRequestDTO("Lucia Perez", " Lucia@Correo.cl ", "Clave2026", "+56987654321");
        when(usuarioRepository.existsByCorreo("lucia@correo.cl")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(10L);
            // La contraseña guardada no es la original
            assertNotEquals("Clave2026", u.getClave());
            return u;
        });

        UsuarioResponseDTO resultado = usuarioService.registrar(dto);

        assertEquals(10L, resultado.getId());
        assertEquals("lucia@correo.cl", resultado.getCorreo());
        assertEquals("PACIENTE", resultado.getRol());
    }

    @Test
    void registrarConCorreoRepetidoLanzaExcepcion() {
        RegistroRequestDTO dto = new RegistroRequestDTO("Lucia Perez", "paciente@sanaesencia.cl", "Clave2026", "+56987654321");
        when(usuarioRepository.existsByCorreo("paciente@sanaesencia.cl")).thenReturn(true);

        assertThrows(CorreoDuplicadoException.class, () -> usuarioService.registrar(dto));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void loginCorrectoRetornaUsuario() {
        Usuario guardado = new Usuario(1L, "Paciente Demo", "paciente@sanaesencia.cl",
                passwordEncoder.encode("Paciente123"), "+56911111111", Rol.PACIENTE, 0);
        when(usuarioRepository.findByCorreo("paciente@sanaesencia.cl")).thenReturn(Optional.of(guardado));

        UsuarioResponseDTO resultado = usuarioService.login(new LoginRequestDTO("paciente@sanaesencia.cl", "Paciente123"));

        assertEquals("Paciente Demo", resultado.getNombre());
    }

    @Test
    void loginConClaveIncorrectaLanzaExcepcion() {
        Usuario guardado = new Usuario(1L, "Paciente Demo", "paciente@sanaesencia.cl",
                passwordEncoder.encode("Paciente123"), "+56911111111", Rol.PACIENTE, 0);
        when(usuarioRepository.findByCorreo("paciente@sanaesencia.cl")).thenReturn(Optional.of(guardado));

        assertThrows(CredencialesInvalidasException.class,
                () -> usuarioService.login(new LoginRequestDTO("paciente@sanaesencia.cl", "otraClave1")));
    }

    @Test
    void loginConCorreoInexistenteLanzaExcepcion() {
        when(usuarioRepository.findByCorreo("nadie@correo.cl")).thenReturn(Optional.empty());

        assertThrows(CredencialesInvalidasException.class,
                () -> usuarioService.login(new LoginRequestDTO("nadie@correo.cl", "Clave2026")));
    }
}
