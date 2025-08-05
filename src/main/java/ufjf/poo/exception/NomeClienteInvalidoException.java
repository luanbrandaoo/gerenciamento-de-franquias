package ufjf.poo.exception;

/**
 * quando o nome do cliente é inválido.
 */
public class NomeClienteInvalidoException extends PedidoException {
    
    public NomeClienteInvalidoException() {
        super("O nome do cliente não deve estar vazio!");
    }
    
    public NomeClienteInvalidoException(String message) {
        super(message);
    }
}
