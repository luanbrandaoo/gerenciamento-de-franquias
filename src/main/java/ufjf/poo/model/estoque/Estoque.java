package ufjf.poo.model.estoque;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.crypto.Data;

public class Estoque {
    
    private List<ItemEstoque> produtos;
    
    public Estoque(){
        this.produtos = new ArrayList<>();
    }
    
    public void adicionarProduto(Produto produto, int quantidade, Date validade) {
        
        ItemEstoque novoItem = new ItemEstoque(produto, quantidade, validade);
        produtos.add(novoItem);
    }
    
    public void removerProduto() {
    }
    
    public void listarProdutos() {
    }
}
