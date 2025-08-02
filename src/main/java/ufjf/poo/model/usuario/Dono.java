package ufjf.poo.model.usuario;

import java.util.ArrayList;
import ufjf.poo.model.Unidade;

import java.util.List;
import ufjf.poo.model.pedido.Pedido;

public class Dono extends Usuario {
    
    private List<Unidade> unidadesGerenciadas;

    public Dono(String nome, int id, String key, String email, List<Unidade> unidadesGerenciadas) {
        super(nome, id, key, email);
        this.unidadesGerenciadas = new ArrayList<>(unidadesGerenciadas);
    }
    
    public void gerenciaUnidades() {
        System.out.println("\n Gerenciamento de Unidades ");
        if(unidadesGerenciadas.isEmpty()){
            System.out.println("Nenuma unidade cadastrada! ");
            return;
        }
        
        System.out.println("Unidades gerenciadas: ");
        for(Unidade u :  unidadesGerenciadas){
            System.out.println("ID: " + u.getId() + "Nome: " + u.getNome() + "Endereco" + u.getEndereco());
            
            System.out.println("Gerentes Subordinados:");
            if(u.getGerente() != null){
                System.out.println("ID: " + u.getGerente().getId() +
                                   "Nome: " + u.getGerente().getNome() +
                                    "Unidade: " + u.getNome());
            }
            else{
                System.out.println("Nao ha gerentes subordinados para a unidade: " + u.getNome());
            }
        }
    }
    
    public void gerenciaGerentes() {
        
    }
    
    public void relatorioUnidades() {
        System.out.println("\n Relatório Consolidado de Unidades ");
        if(unidadesGerenciadas.isEmpty()){
            System.out.println("Nenhuma unidade para gerar relatório.");
            return;
        }
        
        for(Unidade u : unidadesGerenciadas){
            System.out.println("Relatorio da Uniade: " + u.getNome());
            u.gerarRelatorioVendas();
        }
    }
    
    public void relatorioVendedores() {
        if(unidadesGerenciadas.isEmpty()){
           System.out.println("Nenhuma unidade para gerar relatório.");
           return;
        }
        
        System.out.println("\n-- Relatório de Desempenho por Vendedor --");
                
        for(Unidade u : unidadesGerenciadas){
            for(Vendedor v : u.getVendedores()){
                double totalVendas = 0.0;
                for(Pedido p : v.getPedidosRealizados()){
                    totalVendas += p.getValorTotal();
                }
                
                System.out.println(" Vendedor: " + v.getNome() +
                                   " Unidade: "  + u.getNome() +
                                    "Total de vendas: R$" + String.format("%.2f", totalVendas));
            }
        }
    }
    
    public void editaUsuarios() {
        // TODO
    }
}