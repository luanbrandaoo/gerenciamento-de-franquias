package ufjf.poo.exception;

/**
 * quando um produto é nulo onde não deveria ser.
 */
public class ProdutoNuloException extends ProdutoException {
    
    public ProdutoNuloException() {
        super("O produto não deve ser nulo!");
    }
    
    public ProdutoNuloException(String message) {
        super(message);
    }
}
