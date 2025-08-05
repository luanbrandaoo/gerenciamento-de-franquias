package ufjf.poo.exception;

/**
 * quando a chave privada de um usuário é inválida.
 */
public class ChavePrivadaInvalidaException extends UsuarioException {
    
    public ChavePrivadaInvalidaException() {
        super("Private Key não deve ser vazia.");
    }
    
    public ChavePrivadaInvalidaException(String message) {
        super(message);
    }
}
