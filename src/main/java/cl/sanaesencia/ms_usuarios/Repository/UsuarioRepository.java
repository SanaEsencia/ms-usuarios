package cl.sanaesencia.ms_usuarios.Repository;

import cl.sanaesencia.ms_usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Acceso a la tabla usuarios. JpaRepository ya trae save, findAll, findById y deleteById
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su correo (login)
    Optional<Usuario> findByCorreo(String correo);

    // Indica si ya existe un usuario con ese correo (registro)
    boolean existsByCorreo(String correo);
}
