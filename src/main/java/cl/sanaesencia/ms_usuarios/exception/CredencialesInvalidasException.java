package cl.sanaesencia.ms_usuarios.exception;

// Se lanza cuando el correo o la contraseña del login no son correctos (responde 401)
public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
