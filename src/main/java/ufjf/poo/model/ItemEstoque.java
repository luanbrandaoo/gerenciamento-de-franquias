package ufjf.poo.model;

import java.util.Date;

public record ItemEstoque(Produto produto, int quantidade, Date validade) {
}
