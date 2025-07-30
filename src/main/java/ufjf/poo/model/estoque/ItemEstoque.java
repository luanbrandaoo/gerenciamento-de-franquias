package ufjf.poo.model.estoque;

import java.util.Date;

public record ItemEstoque(Produto produto, int quantidade, Date validade) {
}
