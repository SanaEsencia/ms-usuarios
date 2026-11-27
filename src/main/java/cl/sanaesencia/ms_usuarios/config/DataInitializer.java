package cl.sanaesencia.ms_usuarios.config;

import cl.sanaesencia.ms_usuarios.Repository.UsuarioRepository;
import cl.sanaesencia.ms_usuarios.model.Rol;
import cl.sanaesencia.ms_usuarios.model.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Carga las cuentas de prueba (datos ficticios) solo si la tabla está vacía
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("DataInitializer: la BD ya tiene usuarios, se omite la carga inicial");
            return;
        }

        log.info("DataInitializer: base de datos vacía, cargando usuarios de prueba");

        usuarioRepository.save(new Usuario(null, "Paciente Demo", "paciente@sanaesencia.cl",
                passwordEncoder.encode("Paciente123"), "+56911111111", Rol.PACIENTE, 0));
        usuarioRepository.save(new Usuario(null, "Tomás Fuentes", "terapeuta@sanaesencia.cl",
                passwordEncoder.encode("Terapeuta123"), "+56922222222", Rol.TERAPEUTA, 2));
        usuarioRepository.save(new Usuario(null, "Administración", "admin@sanaesencia.cl",
                passwordEncoder.encode("Admin1234"), "+56933333333", Rol.ADMIN, 0));

        log.info(">>> DataInitializer: {} usuarios insertados correctamente", usuarioRepository.count());
        log.info("  PACIENTE  -> paciente@sanaesencia.cl  | Paciente123");
        log.info("  TERAPEUTA -> terapeuta@sanaesencia.cl | Terapeuta123");
        log.info("  ADMIN     -> admin@sanaesencia.cl     | Admin1234");
    }
}
