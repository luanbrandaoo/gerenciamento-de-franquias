package ufjf.poo.exception;

/**
 * quando há problemas relacionados a produtos.
 */
public class ProdutoException extends SistemaFranquiaException {
    
    public ProdutoException(String message) {
        super(message);
    }
    
    public ProdutoException(String message, Throwable cause) {
        super(message, cause);
    }
}
