package ufjf.poo.model.pedido;

import ufjf.poo.model.estoque.Produto;

public record ItemPedido(Produto produto, int quantidade, double subtotal) {
}
