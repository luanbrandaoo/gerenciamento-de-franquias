package ufjf.poo.model.pedido;

import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Vendedor;

import java.util.Date;
import java.util.List;

public class Pedido {
    
    private List<ItemPedido> itens;
    private double valorTotal;
    private Vendedor vendedor;
    private Date data;
    
    public void calcularTotal() {

    }
    
    public void adicionarItem(Produto produto, int quantidade) {

    }
    
    public void removerItem(Produto produto) {

    }
}
