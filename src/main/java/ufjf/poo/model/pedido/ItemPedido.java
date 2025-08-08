/*
Rafael Müller dos Santos Moreira 202465557C
Luan Brandão de Oliveira 202465055A
Marcos José de Oliveira Júnior 202135011
 */

package ufjf.poo.model.pedido;

import java.math.BigDecimal;
import ufjf.poo.model.estoque.Produto;

public record ItemPedido(Produto produto, int quantidade, BigDecimal subtotal) {
}
