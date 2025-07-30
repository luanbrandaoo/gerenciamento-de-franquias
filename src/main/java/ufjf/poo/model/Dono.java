package ufjf.poo.model;

import java.util.List;

public class Dono extends Usuario {
    
    private List<Unidade> unidadesGerenciadas;
    private List<Gerente> gerentesSubordinados;

    public Dono(String nome, int id, String key, String email) {
        super(nome, id, key, email);
    }
    
    public void gerenciaUnidades() {
        
    }
    
    public void gerenciaGerentes() {
        
    }
    
    public void relatorioUnidades() {
        
    }
    
    public void relatorioVendedores() {
        
    }
    
    public void editaUsuarios() {
        
    }
}