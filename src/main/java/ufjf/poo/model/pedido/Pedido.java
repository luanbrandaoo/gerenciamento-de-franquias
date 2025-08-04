package ufjf.poo.model.pedido;

import java.util.ArrayList;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Vendedor;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Pedido {
    
    private List<ItemPedido> itens;
    private long valorTotal;
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
            throw new IllegalArgumentException("O id nao deve ser negativo! ");
        
        if(nomeCliente == null || nomeCliente.trim().isEmpty())
            throw new IllegalArgumentException("O nome do cliente nao deve estar vazia! ");
        
        if(formaDePagamento == null || formaDePagamento.trim().isEmpty())
            throw new IllegalArgumentException("A forma de pagamento nao deve estar vazia! ");
        
        if(formaDeEntrega == null || formaDeEntrega.trim().isEmpty())
            throw new IllegalArgumentException("A forma de entrega nao deve estar vazia! ");
        
         // TODO validar vendedor
        
        this.idPedido = idPedido;
        this.nomeCliente = nomeCliente;
        this.vendedor = vendedor;
        this.data = data;
        this.formaDePagamento = formaDePagamento;
        this.formaDeEntrega = formaDeEntrega;
        this.itens = new ArrayList<>();
        this.valorTotal = 0;
        this.status = "Pendente";
        
    }
    
    public void calcularTotal() {
        valorTotal = 0;
        for(ItemPedido itemAtual : itens){
            valorTotal += itemAtual.subtotal();
        }
    }
    
    public void adicionarItem(Produto produto, int quantidade) {
        
        if(produto == null){
            throw new IllegalArgumentException("O produto nao deve ser nulo! ");
        }
        
        ItemPedido novoItem = new ItemPedido(produto, quantidade, produto.getPreco()*quantidade);
        
        this.itens.add(novoItem);
        calcularTotal();
    }
    
    public void adicionarItem(List<ItemPedido> novosItens) {
    if (novosItens != null && !novosItens.isEmpty()) {
        this.itens.addAll(novosItens);
        calcularTotal();
    }
}
    
    //Esta versao esta removendo todas as ocorrencias de um mesmo produto
    public void removerItem(Produto produto) {
        
        if(produto == null)
            throw new IllegalArgumentException("O produto nao deve ser nulo! ");

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
    
    public long getValorTotal(){
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
            throw new IllegalArgumentException("O status do pedido nao deve ser vazio! ");
        
        this.status = status;
    }
    
    public void setFormaDePagamento(String formaDePagamento){
        
        if(formaDePagamento == null || formaDePagamento.trim().isEmpty())
            throw new IllegalArgumentException("Forma de pagamento nao inserida! ");
        
        this.formaDePagamento = formaDePagamento;
    }
    
    public void setFormaDeEntrega(String formaDeEntrega){
        
        if(formaDeEntrega == null || formaDeEntrega.trim().isEmpty())
            throw new IllegalArgumentException("Forma de entrega nao inserida! ");
        
        this.formaDeEntrega = formaDeEntrega;
    }
}
