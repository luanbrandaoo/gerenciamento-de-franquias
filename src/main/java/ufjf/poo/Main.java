package ufjf.poo;

import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.pedido.ItemPedido;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.model.usuario.Vendedor;
import ufjf.poo.model.Unidade;
import ufjf.poo.model.Session;
import ufjf.poo.view.LoginFrame;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        
        // --- 1. Inicializando as Unidades, Estoque e Produtos ---
        Map<Produto, Integer> itensEstoque1 = new HashMap<>();
        Estoque estoqueUnidade1 = new Estoque(itensEstoque1);
        Produto p1 = new Produto("Smartphone", "A01", new BigDecimal("1500.00"));
        Produto p2 = new Produto("Notebook", "B02", new BigDecimal("3500.00"));
        estoqueUnidade1.adicionarProduto(p1, 50);
        estoqueUnidade1.adicionarProduto(p2, 20);

        Map<Produto, Integer> itensEstoque2 = new HashMap<>();
        Estoque estoqueUnidade2 = new Estoque(itensEstoque2);
        Produto p3 = new Produto("Tablet", "C03", new BigDecimal("800.00"));
        estoqueUnidade2.adicionarProduto(p3, 30);
        
        Unidade unidade1 = new Unidade(1, "Unidade Centro", "Rua A, 123", estoqueUnidade1);
        Unidade unidade2 = new Unidade(2, "Unidade Bairro", "Av B, 456", estoqueUnidade2);
        
        // --- 2. Criando os usuários (Dono, Gerente, Vendedores) ---
        List<Usuario> todosUsuarios = new ArrayList<>();
        
        Dono dono = new Dono("Dono", 1, "dono123", "dono@empresa.com", new ArrayList<>(List.of(unidade1, unidade2)));
        
        Gerente gerente1 = new Gerente("Gerente_1", 2, "gerente123", "gerente1@empresa.com", unidade1);
        Gerente gerente2 = new Gerente("Gerente_2", 3, "gerente456", "gerente2@empresa.com", unidade2);
        
        Vendedor vendedor1_1 = new Vendedor("Vendedor_1", 4, "vendedor123", "vendedor1@empresa.com", estoqueUnidade1);
        Vendedor vendedor1_2 = new Vendedor("Vendedor_2", 5, "vendedor456", "vendedor2@empresa.com", estoqueUnidade1);
        
        Vendedor vendedor2_1 = new Vendedor("Vendedor_3", 6, "vendedor789", "vendedor3@empresa.com", estoqueUnidade2);

        unidade1.adicionarVendedor(vendedor1_1);
        unidade1.adicionarVendedor(vendedor1_2);
        unidade2.adicionarVendedor(vendedor2_1);

        List<ItemPedido> itensPedido1 = new ArrayList<>();
        itensPedido1.add(new ItemPedido(p1, 2, p1.getPreco().multiply(BigDecimal.valueOf(2))));
        vendedor1_1.cadastrarPedidos("Cliente Teste", "PIX", "Retirada", itensPedido1);

        todosUsuarios.add(dono);
        todosUsuarios.add(gerente1);
        todosUsuarios.add(gerente2);
        todosUsuarios.add(vendedor1_1);
        todosUsuarios.add(vendedor1_2);
        todosUsuarios.add(vendedor2_1);
        
        // --- 3. Inicializando a Session e a Tela de Login ---
        Session session = new Session();
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame(session, todosUsuarios);
        });
    }
}