package ufjf.poo.exception;

/**
 * quando a forma de pagamento é inválida.
 */
public class FormaPagamentoInvalidaException extends PedidoException {
    
    public FormaPagamentoInvalidaException() {
        super("A forma de pagamento não deve estar vazia!");
    }
    
    public FormaPagamentoInvalidaException(String message) {
        super(message);
    }
}
