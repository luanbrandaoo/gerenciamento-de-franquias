package ufjf.poo.exception;

/**
 * quando o nome de um usuário é inválido.
 */
public class NomeUsuarioInvalidoException extends UsuarioException {
    
    public NomeUsuarioInvalidoException() {
        super("Nome do usuário não deve ser vazio.");
    }
    
    public NomeUsuarioInvalidoException(String message) {
        super(message);
    }
}
