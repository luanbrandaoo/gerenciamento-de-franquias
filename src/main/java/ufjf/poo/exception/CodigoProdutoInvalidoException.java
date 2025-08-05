package ufjf.poo.exception;

/**
 * quando o código de um produto é inválido.
 */
public class CodigoProdutoInvalidoException extends ProdutoException {
    
    public CodigoProdutoInvalidoException() {
        super("O código do produto não deve estar vazio!");
    }
    
    public CodigoProdutoInvalidoException(String message) {
        super(message);
    }
}
