package ufjf.poo.exception;

/**
 * quando o status de um pedido é inválido.
 */
public class StatusPedidoInvalidoException extends PedidoException {
    
    public StatusPedidoInvalidoException() {
        super("O status do pedido não deve ser vazio!");
    }
    
    public StatusPedidoInvalidoException(String message) {
        super(message);
    }
}
