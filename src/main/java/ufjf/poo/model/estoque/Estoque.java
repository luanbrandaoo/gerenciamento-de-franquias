package ufjf.poo.model.estoque;

import java.util.Map;
import java.util.Set;

import static java.lang.Integer.max;

public class Estoque {
    
    private Map<Produto, Integer> produtos;

    public Estoque(Map<Produto, Integer> produtos) {
        this.produtos = produtos;
    }

    public void adicionarProduto(Produto produto, Integer quantidade) {
//        produtos.put(produto, produtos.getOrDefault(produto, 0) + quantidade);
        produtos.compute(produto, (k, v) -> v == null ? quantidade : v + quantidade);
    }
    
    public void removerProduto(Produto produto) {
        produtos.remove(produto);
    }

    public void subtraiProduto(Produto produto, Integer quantidade) {
        produtos.compute(produto, (k, v) -> v == null ? 0 : max(v - quantidade, 0));
    }

    public void atualizarProduto(Produto produto, Integer quantidade) {
        produtos.put(produto, quantidade);
    }

    public int quantidadeProduto(Produto produto) {
        return produtos.getOrDefault(produto, 0);
    }
    
    public Set<Produto> produtosEmEstoque() {
        return produtos.keySet();
    }

    public Map<Produto, Integer> mapeiaEstoque() {
        return produtos;
    }

    public void limparEstoque() {
        produtos.clear();
    }

    public boolean produtoEmEstoque(Produto produto) {
        return produtos.containsKey(produto) && produtos.get(produto) != 0;
    }
    
    public void listarProdutos(){
        
        if(produtos.isEmpty()){
            System.out.println("Estoque vazio! ");
            return;
        }
        
        System.out.println("--- Produtos no Estoque ---");
        for(Map.Entry<Produto, Integer> produtoAtual : mapeiaEstoque().entrySet()){
            
            Produto produto = produtoAtual.getKey();
            Integer quantidade = produtoAtual.getValue();
            System.out.println("Codigo: " + produto.getCodigo() +
                               " | Nome" + produto.getNome() + 
                               " | Preço: R$ " + String.format("%.2f", produto.getPreco()) + 
                               " | Quantidade: " + quantidade);
        }
        System.out.println("______________________");
    }
    
    public Produto buscarProdutoPorCodigo(String codigo){
        
        for(Produto produtoBuscado : produtos.keySet()){
            if(produtoBuscado.getCodigo().equals(codigo))
                return produtoBuscado;
        }
        
        return null;
    }
    
    public boolean estaDisponivel(Produto produto, int quantidade){
        
        int quantidadeDisponivel = quantidadeProduto(produto);
        
        return quantidadeDisponivel >= quantidade;
    }
    
}
