package ufjf.poo.exception;

/**
 * exceção base para o sistema de gerenciamento de franquias.
 */
public class SistemaFranquiaException extends RuntimeException {
    
    public SistemaFranquiaException(String message) {
        super(message);
    }
    
    public SistemaFranquiaException(String message, Throwable cause) {
        super(message, cause);
    }
}
