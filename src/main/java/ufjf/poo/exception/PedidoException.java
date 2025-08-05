package ufjf.poo.exception;

/**
 * quando há problemas relacionados a pedidos.
 */
public class PedidoException extends SistemaFranquiaException {
    
    public PedidoException(String message) {
        super(message);
    }
    
    public PedidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
