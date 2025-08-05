package ufjf.poo.exception;

/**
 * quando o nome de um produto é inválido.
 */
public class NomeProdutoInvalidoException extends ProdutoException {
    
    public NomeProdutoInvalidoException() {
        super("O nome do produto não deve estar vazio!");
    }
    
    public NomeProdutoInvalidoException(String message) {
        super(message);
    }
}
