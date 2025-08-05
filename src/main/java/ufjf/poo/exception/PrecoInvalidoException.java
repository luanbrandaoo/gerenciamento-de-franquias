package ufjf.poo.exception;

/**
 * quando um preço de produto é inválido.
 */
public class PrecoInvalidoException extends ProdutoException {
    
    public PrecoInvalidoException() {
        super("O valor do produto não deve ser negativo!");
    }
    
    public PrecoInvalidoException(String message) {
        super(message);
    }
}
