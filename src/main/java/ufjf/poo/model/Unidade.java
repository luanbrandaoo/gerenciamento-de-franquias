package ufjf.poo.model;

import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;

import java.util.List;
import java.util.Map;

public class Unidade {
    
    private int id;
    private String nome;
    private String endereco;
    private Gerente gerente;
    private List<Vendedor> vendedores;
    private Estoque estoque;

    public Unidade(int id, String nome, String endereco) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.gerente = null;
        this.vendedores = null;
        this.estoque = null;
    }
    
    public void adicionarVendedor(Vendedor novoVendedor) {
        if (novoVendedor != null)
            vendedores.add(novoVendedor);
    }
    
    public void removerVendedor(Vendedor novoVendedor) {
        vendedores.remove(novoVendedor);
    }
    
    public void gerarRelatorioVendas() {
    }
    
    public void trocarGerente(Gerente novoGerente) {
        gerente = novoGerente;
    }
}
