package ufjf.poo.model;

import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;

import java.util.List;

public class Unidade {
    
    private int id;
    private String nome;
    //private Endereco local;
    private Gerente gerente;
    private List<Vendedor> vendedores;
    private Estoque estoque;
    
    public void adicionarVendedor() {
    }
    
    public void removerVendedor() {
    }
    
    public void gerarRelatorioVendas() {
    }
    
    public void trocarGerente() {
    }
    
    public List<Produto> listarEstoque() {
        return null;
    }
}
