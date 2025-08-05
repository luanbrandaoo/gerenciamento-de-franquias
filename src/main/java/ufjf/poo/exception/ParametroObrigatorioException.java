package ufjf.poo.exception;

/**
 * quando um parâmetro obrigatório é nulo ou vazio.
 */
public class ParametroObrigatorioException extends ValorInvalidoException {
    
    public ParametroObrigatorioException(String nomeParametro) {
        super("O parâmetro '" + nomeParametro + "' é obrigatório e não pode ser nulo ou vazio.");
    }
    
    public ParametroObrigatorioException(String message, Throwable cause) {
        super(message, cause);
    }
}
