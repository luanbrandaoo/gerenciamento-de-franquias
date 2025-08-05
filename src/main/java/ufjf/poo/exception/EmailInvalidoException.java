package ufjf.poo.exception;

/**
 * quando um email é inválido.
 */
public class EmailInvalidoException extends UsuarioException {
    
    public EmailInvalidoException() {
        super("Email inválido.");
    }
    
    public EmailInvalidoException(String message) {
        super(message);
    }
}
