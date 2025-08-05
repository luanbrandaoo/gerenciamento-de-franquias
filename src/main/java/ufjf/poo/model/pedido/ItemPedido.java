package ufjf.poo.model.pedido;

import java.math.BigDecimal;
import ufjf.poo.model.estoque.Produto;

public record ItemPedido(Produto produto, int quantidade, BigDecimal subtotal) {
}
