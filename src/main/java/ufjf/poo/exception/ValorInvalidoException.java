package ufjf.poo.exception;

/**
 * quando um valor inválido é fornecido para um campo.
 */
public class ValorInvalidoException extends SistemaFranquiaException {
    
    public ValorInvalidoException(String message) {
        super(message);
    }
    
    public ValorInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
