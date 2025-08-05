package ufjf.poo.exception;

/**
 * quando o ID de um pedido é inválido.
 */
public class IdPedidoInvalidoException extends PedidoException {
    
    public IdPedidoInvalidoException() {
        super("O ID não deve ser negativo!");
    }
    
    public IdPedidoInvalidoException(String message) {
        super(message);
    }
}
