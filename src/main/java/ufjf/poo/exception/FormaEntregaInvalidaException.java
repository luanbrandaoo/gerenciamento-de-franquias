package ufjf.poo.exception;

/**
 * quando a forma de entrega é inválida.
 */
public class FormaEntregaInvalidaException extends PedidoException {
    
    public FormaEntregaInvalidaException() {
        super("A forma de entrega não deve estar vazia!");
    }
    
    public FormaEntregaInvalidaException(String message) {
        super(message);
    }
}
