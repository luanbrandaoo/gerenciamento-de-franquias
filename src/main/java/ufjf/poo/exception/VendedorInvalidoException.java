package ufjf.poo.exception;

/**
 * quando o vendedor é nulo
 */
public class VendedorInvalidoException extends UsuarioException {

    public VendedorInvalidoException() {
        super("Vendedor não existe.");
    }

    public VendedorInvalidoException(String message) {
        super(message);
    }
}
