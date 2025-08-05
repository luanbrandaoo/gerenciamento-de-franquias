package ufjf.poo.exception;

/**
 * quando o ID de um usuário é inválido.
 */
public class IdUsuarioInvalidoException extends UsuarioException {
    
    public IdUsuarioInvalidoException() {
        super("ID do usuário deve ser positivo.");
    }
    
    public IdUsuarioInvalidoException(String message) {
        super(message);
    }
}
