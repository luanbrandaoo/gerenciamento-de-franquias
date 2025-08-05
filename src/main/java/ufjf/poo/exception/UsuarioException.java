package ufjf.poo.exception;

/**
 * quando há problemas relacionados a usuários.
 */
public class UsuarioException extends SistemaFranquiaException {
    
    public UsuarioException(String message) {
        super(message);
    }
    
    public UsuarioException(String message, Throwable cause) {
        super(message, cause);
    }
}
