package ufjf.poo.model.estoque;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Integer.max;
import javax.xml.crypto.Data;

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

    /*Necessário revisar implementação de ItemEstoque aqui*/
//    public void adicionarItemEstoque(Produto produto, int quantidade, Date validade) {
//        ItemEstoque novoItem = new ItemEstoque(produto, quantidade, validade);
//        produtos.add(novoItem);
//    }
    
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
}
