package ufjf.poo.model.usuario;

import java.util.ArrayList;
import ufjf.poo.model.Unidade;

import java.util.List;
import java.util.Scanner;
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
    
    public void editaUsuarios(Scanner teclado) {
        
        if (unidadesGerenciadas.isEmpty()) {
            System.out.println("Nenhuma unidade cadastrada para listar usuários.");
            return;
        }
        System.out.println("Edicao de Usuarios. ");
        listarTodosUsuarios();

        System.out.println("Digite o ID do usuario que deseja editar: ");
        
        int idBusca;
        Usuario usuarioEncontrado = null;
        
        idBusca = Integer.parseInt(teclado.nextLine());
        
        if(idBusca < 0){
            System.out.println("ID invalido! ");
            return;
        }
        
        
        for(Unidade u : unidadesGerenciadas){
            if(u.getGerente() != null && u.getGerente().getId() == idBusca){
                usuarioEncontrado = u.getGerente();
                break;
            }
            
            for(Vendedor v : u.getVendedores()){
                if(v != null && v.getId() == idBusca){
                    usuarioEncontrado = v;
                    break;
                }
            }
            
            if (usuarioEncontrado != null) {
                break;
            }
        }
        
         if (usuarioEncontrado == null) {
            System.out.println("Usuário com ID " + idBusca + " não encontrado.");
            return;
        }
         
         System.out.println(" Usuario Encontrado: ID " + usuarioEncontrado.getId() + " Nome: " + usuarioEncontrado.getNome());
         
         System.out.println("Digite um novo nome (ou deixe em branco caso nao queira mudar! ) ");
         String novoNome =  teclado.nextLine();
         if(!novoNome.trim().isEmpty()){
             usuarioEncontrado.setNome(novoNome);
         }
         
         System.out.println("Digite o novo Email (ou deixe em branco caso nao queira mudar! ) ");
         String novoEmail =  teclado.nextLine();
         if(!novoEmail.trim().isEmpty()){
             usuarioEncontrado.setEmail(novoEmail);
         }
         
         System.out.println("Informações do usuário atualizadas com sucesso!");
    }
    
    public void listarTodosUsuarios() {
    System.out.println("\n Lista de Gerentes e Vendedores ");
        if (unidadesGerenciadas.isEmpty()) {
            System.out.println("Nenhuma unidade cadastrada para listar usuários.");
            return;
        }

        for (Unidade unidade : unidadesGerenciadas) {
            System.out.println("\nUnidade: " + unidade.getNome() + " ID: " + unidade.getId());

            // Lista o Gerente da Unidade
            Gerente gerente = unidade.getGerente();
            if (gerente != null) {
                System.out.println("- Gerente -> ID: " + gerente.getId() + ", Nome: " + gerente.getNome());
            }

            // Lista os Vendedores da Unidade
            for (Vendedor vendedor : unidade.getVendedores()) {
                System.out.println("- Vendedor -> ID: " + vendedor.getId() + ", Nome: " + vendedor.getNome());
            }
        }
    }
}