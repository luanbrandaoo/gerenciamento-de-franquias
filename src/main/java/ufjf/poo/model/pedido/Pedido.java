package ufjf.poo.model.pedido;

import java.math.BigDecimal;
import java.util.ArrayList;

import ufjf.poo.exception.*;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Vendedor;

import java.util.Date;
import java.util.List;

public class Pedido {
    
    private final List<ItemPedido> itens;
    private BigDecimal valorTotal;
    private final Vendedor vendedor;
    private final Date data;
    
    private final int idPedido;
    private final String nomeCliente;
    private String formaDePagamento;
    private String formaDeEntrega;
    private String status;
    
    public Pedido(int idPedido, String nomeCliente, Vendedor vendedor, Date data,
                  String formaDePagamento, String formaDeEntrega ){
        
        if(idPedido < 0)
            throw new IdPedidoInvalidoException();
        
        if(nomeCliente == null || nomeCliente.trim().isEmpty())
            throw new NomeClienteInvalidoException();
        
        if(formaDePagamento == null || formaDePagamento.trim().isEmpty())
            throw new FormaPagamentoInvalidaException();
        
        if(formaDeEntrega == null || formaDeEntrega.trim().isEmpty())
            throw new FormaEntregaInvalidaException();
        
        if(vendedor == null)
            throw new VendedorInvalidoException();
        
        this.idPedido = idPedido;
        this.nomeCliente = nomeCliente;
        this.vendedor = vendedor;
        this.data = data;
        this.formaDePagamento = formaDePagamento;
        this.formaDeEntrega = formaDeEntrega;
        this.itens = new ArrayList<>();
        this.valorTotal = BigDecimal.ZERO;
        this.status = "Pendente";
        
    }
    
    public void calcularTotal() {
        valorTotal = BigDecimal.ZERO;
        for(ItemPedido itemAtual : itens){
            valorTotal = valorTotal.add(itemAtual.subtotal());
        }
    }
    
    public void adicionarItem(Produto produto, int quantidade) {
        
        if(produto == null){
            throw new ProdutoNuloException();
        }
        
        ItemPedido novoItem = new ItemPedido(produto, quantidade, produto.getPreco().multiply(BigDecimal.valueOf(quantidade)));
        
        this.itens.add(novoItem);
        calcularTotal();
    }
    
    public void adicionarItem(List<ItemPedido> novosItens) {
    if (novosItens != null && !novosItens.isEmpty()) {
        this.itens.addAll(novosItens);
        calcularTotal();
    }
}
    

    public void removerItem(Produto produto) {
        
        if(produto == null)
            throw new ProdutoNuloException();

        boolean removido = itens.removeIf(itemPedido -> itemPedido.produto().equals(produto));
        
        if(removido){
            System.out.println("Produto(s) removido(s) do pedido.");
            calcularTotal();
        }
        else{
            System.out.println("Produto nao removido ");
        }
    }
    
    public int getIdPedido(){
        return idPedido;
    }
    
    public String getNomeCliente(){
        return nomeCliente;
    }
    
    public Vendedor getVendedor(){
        return vendedor;
    }
    
    public Date getDataPedido(){
        return data;
    }
    
    public List<ItemPedido> getItens(){
        return new ArrayList<>(itens);
    }
    
    public BigDecimal getValorTotal(){
        return valorTotal;
    }
    
    public String getFormaDePagamento(){
        return formaDePagamento;
    }
    
    public String getFormaDeEntrega(){
        return formaDeEntrega;
    }
    
    public String getStatus(){
        return status;
    }
    
    public void setStatus(String status){
        
        if(status == null || status.trim().isEmpty())
            throw new StatusPedidoInvalidoException();
        
        this.status = status;
    }
    
    public void setFormaDePagamento(String formaDePagamento){
        
        if(formaDePagamento == null || formaDePagamento.trim().isEmpty())
            throw new FormaPagamentoInvalidaException();
        
        this.formaDePagamento = formaDePagamento;
    }
    
    public void setFormaDeEntrega(String formaDeEntrega){
        
        if(formaDeEntrega == null || formaDeEntrega.trim().isEmpty())
            throw new FormaEntregaInvalidaException();
        
        this.formaDeEntrega = formaDeEntrega;
    }
}
