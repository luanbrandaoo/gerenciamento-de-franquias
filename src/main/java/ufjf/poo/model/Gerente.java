package ufjf.poo.model;

import java.util.List;

public class Gerente extends Usuario {
    
    private Unidade unidadeFranquia;
    private List<Vendedor> equipeDeVendedores;
    
    public Gerente(String nome, int id, String key, String email) {
        super(nome, id, key, email);
    }

    public void gerenciaEquipes() {

    }
    
    public void controlaPedidos() {

    }
    
    public void administrarEstoque() {

    }
    
    public void relatorioDeDesempenho() {

    }
}
