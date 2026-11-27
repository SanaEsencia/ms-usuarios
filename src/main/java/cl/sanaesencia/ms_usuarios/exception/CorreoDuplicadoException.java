package cl.sanaesencia.ms_usuarios.exception;

// Se lanza cuando se intenta registrar un correo que ya existe (responde 409)
public class CorreoDuplicadoException extends RuntimeException {
    public CorreoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
