package ufjf.poo.model.usuario;

import java.math.BigDecimal;
import java.util.ArrayList;
import ufjf.poo.model.Unidade;

import java.util.List;
import java.util.Scanner;
import ufjf.poo.model.pedido.Pedido;

public class Dono extends Usuario {
    
    private final List<Unidade> unidadesGerenciadas;

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
                BigDecimal totalVendas = BigDecimal.ZERO;
                for(Pedido p : v.getPedidosRealizados()){
                    totalVendas = totalVendas.add(p.getValorTotal());
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

    public Usuario cadastrarUsuario(String nome, String email, String senha, String tipoUsuario, 
                                   Unidade unidade, java.util.List<Usuario> todosUsuarios) {
        try {
            // verificar se email já existe
            for (Usuario u : todosUsuarios) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    System.out.println("Erro: Email já cadastrado no sistema.");
                    return null;
                }
            }
            
            // gerar ID único
            int novoId = todosUsuarios.stream()
                    .mapToInt(Usuario::getId)
                    .max()
                    .orElse(0) + 1;
            
            Usuario novoUsuario;
            
            switch (tipoUsuario.toLowerCase()) {
                case "vendedor":
                    if (unidade == null) {
                        System.out.println("Erro: Unidade é obrigatória para Vendedor.");
                        return null;
                    }
                    novoUsuario = new Vendedor(nome, novoId, senha, email, unidade.getEstoque());
                    unidade.adicionarVendedor((Vendedor) novoUsuario);
                    break;
                    
                case "gerente":
                    if (unidade == null) {
                        System.out.println("Erro: Unidade é obrigatória para Gerente.");
                        return null;
                    }
                    novoUsuario = new Gerente(nome, novoId, senha, email, unidade);
                    unidade.trocarGerente((Gerente) novoUsuario);
                    break;
                    
                case "dono":
                    novoUsuario = new Dono(nome, novoId, senha, email, unidadesGerenciadas);
                    break;
                    
                default:
                    System.out.println("Erro: Tipo de usuário inválido: " + tipoUsuario);
                    return null;
            }
            
            todosUsuarios.add(novoUsuario);
            System.out.println("Usuário " + nome + " cadastrado com sucesso como " + tipoUsuario);
            return novoUsuario;
            
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
            return null;
        }
    }
}