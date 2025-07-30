package ufjf.poo.model;

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
