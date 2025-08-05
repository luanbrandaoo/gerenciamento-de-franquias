package ufjf.poo.model;

import java.math.BigDecimal;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ufjf.poo.model.pedido.Pedido;

public class Unidade {
    
    private int id;
    private String nome;
    private String endereco;
    private Gerente gerente;
    private List<Vendedor> vendedores;
    private Estoque estoque;

    public Unidade(int id, String nome, String endereco, Estoque estoque) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.gerente = null;
        this.vendedores = new ArrayList<>();
        this.estoque = estoque;
    }
    
    public void adicionarVendedor(Vendedor novoVendedor) {
        if (novoVendedor != null)
            vendedores.add(novoVendedor);
    }
    
    public void removerVendedor(Vendedor novoVendedor) {
        vendedores.remove(novoVendedor);
    }
    
    public void gerarRelatorioVendas() {
        if(vendedores.isEmpty()){
            System.out.println("Nao ha Vendedores! ");
            return;
        }
        
        BigDecimal totalVendasUnidade = BigDecimal.ZERO;
        int    totalPedidosUnidade = 0;
        
        System.out.println("Relatorio de vendas por vendedor: ");
        
        for(Vendedor vendedorAnalisado : vendedores){
            BigDecimal totalVendasVendedor = BigDecimal.ZERO;
            
            for(Pedido pedidoAnalisado : vendedorAnalisado.getPedidosRealizados()){
                totalVendasVendedor = totalVendasVendedor.add(pedidoAnalisado.getValorTotal());
            }
            
            System.out.println("- Vendedor: " + vendedorAnalisado.getNome() + 
                               vendedorAnalisado.getPedidosRealizados().size() + 
                               " pedidos, Total R$" + totalVendasVendedor);
            
            totalVendasUnidade = totalVendasUnidade.add(totalVendasVendedor);
            totalPedidosUnidade += vendedorAnalisado.getPedidosRealizados().size();
        }
        
        System.out.println("Resumo da Unidade:");
        System.out.println("Total de Pedidos: " + totalPedidosUnidade);
        System.out.println("Total de Vendas: R$" + totalVendasUnidade);
        
    }
    
    public void trocarGerente(Gerente novoGerente) {
        gerente = novoGerente;
    }
    
    public List<Vendedor> getVendedores(){
        return vendedores;
    }
    
    public Estoque getEstoque(){
        return estoque;
    }
    
    public String getNome(){
        return nome;
    }
    
    public int getId(){
        return id;
    }
    
    public String getEndereco(){
        return endereco;
    }
    
    public Gerente getGerente(){
        return gerente;
    }
}
