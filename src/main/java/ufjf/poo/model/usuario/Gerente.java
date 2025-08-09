/*
Rafael Müller dos Santos Moreira 202465557C
Luan Brandão de Oliveira 202465055A
Marcos José de Oliveira Júnior 202135011
 */

package ufjf.poo.model.usuario;

import ufjf.poo.model.Unidade;

import java.util.List;
import ufjf.poo.model.estoque.Produto;

public class Gerente extends Usuario {

    private final Unidade unidadeFranquia;
    //private List<Vendedor> equipeDeVendedores;

    public Gerente(String nome, int id, String key, String email, Unidade unidadeFranquia) {
        super(nome, id, key, email, "Gerente" );
        this.unidadeFranquia = unidadeFranquia;
    }
    
    public Unidade getUnidadeFranquia() {
        return unidadeFranquia;
    }

    @Override
    public void relatorioAtividades() {

        System.out.println("\n--- Gerenciamento de Equipe ---");
        List<Vendedor> vendedores = unidadeFranquia.getVendedores();

        if (vendedores.isEmpty()) {
            System.out.println("A unidade não possui vendedores.");
            return;
        }

        System.out.println("Vendedores da unidade " + unidadeFranquia.getNome());

        for (Vendedor v : vendedores) {
            System.out.println("- ID: " + v.getId() + " Nome: " + v.getNome() + " E-mail" + v.getEmail());
            System.out.println(" Pedidos realizados: " + v.getPedidosRealizados().size());
        }

    }

    public void controlaPedidos() {

        System.out.println("\n--- Controle de Pedidos da Unidade ---");
        List<Vendedor> vendedores = unidadeFranquia.getVendedores();

        if (vendedores.isEmpty()) {
            System.out.println("Nenhum vendedor Cadastrado! ");
            return;
        }

        for (Vendedor v : vendedores) {
            System.out.println("\nPedidos do Vendedor: " + v.getNome());
            v.relatorioAtividades();
        }

    }

    public void administrarEstoque(Produto produto, int quantidade, String acao) {

        if (unidadeFranquia.getEstoque() == null) {
            System.out.println("Estoque não configurado para a unidade.");
            return;
        }

        System.out.println("\n--- Administração de Estoque ---");

        if (acao.equalsIgnoreCase("adicionar")) {
            unidadeFranquia.getEstoque().adicionarProduto(produto, quantidade);
            System.out.println("Produto " + produto.getNome() + " adicionado com sucesso.");
        } else if (acao.equalsIgnoreCase("remover")) {
            unidadeFranquia.getEstoque().removerProduto(produto);
            System.out.println("Produto " + produto.getNome() + " removido com sucesso.");
        } else if (acao.equalsIgnoreCase("listar")) {
            unidadeFranquia.getEstoque().listarProdutos();
        } else {
            System.out.println("Ação '" + acao + "' não reconhecida.");
        }

    }

    public void relatorioDeDesempenho() {
        System.out.println("Relatorio da Unidade: ");
        unidadeFranquia.gerarRelatorioVendas();
    }
}
