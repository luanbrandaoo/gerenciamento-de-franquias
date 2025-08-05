package ufjf.poo.model.usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.pedido.Pedido;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class VendedorTest {

    private Vendedor vendedor;
    private Estoque estoque;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setUp() {
        produto1 = new Produto("Notebook", "NB001", new BigDecimal("2500.00"));
        produto2 = new Produto("Mouse", "M001", new BigDecimal("50.00"));
        
        HashMap<Produto, Integer> produtos = new HashMap<>();
        produtos.put(produto1, 10);
        produtos.put(produto2, 25);
        
        estoque = new Estoque(produtos);
        vendedor = new Vendedor("João Vendedor", 1, "senha123", "joao@email.com", estoque);
    }

    @Test
    void testCriacaoVendedor() {
        assertEquals("João Vendedor", vendedor.getNome());
        assertEquals(1, vendedor.getId());
        assertEquals("senha123", vendedor.getPrivateKey());
        assertEquals("joao@email.com", vendedor.getEmail());
        assertTrue(vendedor.getPedidosRealizados().isEmpty());
    }

    @Test
    void testCriacaoVendedorSemEstoque() {
        Vendedor vendedorSemEstoque = new Vendedor("Maria", 2, "senha456", "maria@email.com", null);
        assertNotNull(vendedorSemEstoque);
        assertTrue(vendedorSemEstoque.getPedidosRealizados().isEmpty());
    }

    @Test
    void testGetPedidosRealizadosRetornaCopia() {
        List<Pedido> pedidos1 = vendedor.getPedidosRealizados();
        List<Pedido> pedidos2 = vendedor.getPedidosRealizados();
        
        assertNotSame(pedidos1, pedidos2);
        assertEquals(pedidos1.size(), pedidos2.size());
    }

    @Test
    void testCadastrarPedidosSemEstoque() {
        Vendedor vendedorSemEstoque = new Vendedor("Maria", 2, "senha456", "maria@email.com", null);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        Scanner scanner = new Scanner("Cliente Teste\n");
        vendedorSemEstoque.cadastrarPedidos(scanner);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Erro: Estoque não configurado"));
        
        System.setOut(System.out);
    }

    @Test
    void testCadastrarPedidoComSucesso() {
        String input = "Cliente Teste\n" +  // nome do cliente
                      "NB001\n" +           // código do produto
                      "2\n" +               // quantidade
                      "n\n" +               // não adicionar mais itens
                      "Cartão\n" +          // forma de pagamento
                      "Entrega\n";          // forma de entrega
        
        Scanner scanner = new Scanner(input);
        
        int pedidosAnteriores = vendedor.getPedidosRealizados().size();
        vendedor.cadastrarPedidos(scanner);
        
        assertEquals(pedidosAnteriores + 1, vendedor.getPedidosRealizados().size());
        
        Pedido pedidoCriado = vendedor.getPedidosRealizados().get(0);
        assertEquals("Cliente Teste", pedidoCriado.getNomeCliente());
        assertEquals("Cartão", pedidoCriado.getFormaDePagamento());
        assertEquals("Entrega", pedidoCriado.getFormaDeEntrega());
        assertTrue(pedidoCriado.getValorTotal().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testCadastrarPedidoSemItens() {
        String input = "Cliente Teste\n" +  // nome do cliente
                      "sair\n";             // sair sem adicionar produtos

        Scanner scanner = new Scanner(input);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        int pedidosAnteriores = vendedor.getPedidosRealizados().size();
        vendedor.cadastrarPedidos(scanner);
        
        assertEquals(pedidosAnteriores, vendedor.getPedidosRealizados().size());
        
        String output = outputStream.toString();
        assertTrue(output.contains("Nenhum item adicionado. Pedido cancelado."));
        
        System.setOut(System.out);
    }

    @Test
    void testCadastrarPedidoProdutoInexistente() {
        String input = "Cliente Teste\n" +  // nome do cliente
                      "INEXISTENTE\n" +     // código inexistente
                      "sair\n";             // sair

        Scanner scanner = new Scanner(input);
        
        int pedidosAnteriores = vendedor.getPedidosRealizados().size();
        vendedor.cadastrarPedidos(scanner);
        
        assertEquals(pedidosAnteriores, vendedor.getPedidosRealizados().size());
    }

    @Test
    void testCadastrarPedidoEstoqueInsuficiente() {
        String input = "Cliente Teste\n" +  // nome do cliente
                      "NB001\n" +           // código do produto
                      "50\n" +              // quantidade maior que estoque (10)
                      "sair\n";             // sair
        
        Scanner scanner = new Scanner(input);
        
        int pedidosAnteriores = vendedor.getPedidosRealizados().size();
        vendedor.cadastrarPedidos(scanner);

        assertEquals(pedidosAnteriores, vendedor.getPedidosRealizados().size());
    }

    @Test
    void testVisualizarPedidosSemPedidos() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        vendedor.visualizarPedidos();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Sem pedidos feitos!"));
        
        System.setOut(System.out);
    }

    @Test
    void testVisualizarPedidosComPedidos() {
        String input = "Cliente Teste\n" +
                      "M001\n" +
                      "1\n" +
                      "n\n" +
                      "PIX\n" +
                      "Retirada\n";
        
        Scanner scanner = new Scanner(input);
        vendedor.cadastrarPedidos(scanner);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        vendedor.visualizarPedidos();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Histórico de Pedidos Realizados"));
        assertTrue(output.contains("Cliente Teste"));
        assertTrue(output.contains("PIX"));
        assertTrue(output.contains("Retirada"));
        assertTrue(output.contains("Total de pedidos no histórico: 1"));
        
        System.setOut(System.out);
    }

    @Test
    void testVerificacaoEstoqueAposVenda() {
        String input = "Cliente Teste\n" +
                      "M001\n" +           // mouse (25 em estoque)
                      "5\n" +              // quantidade 5
                      "n\n" +
                      "Dinheiro\n" +
                      "Entrega\n";
        
        Scanner scanner = new Scanner(input);
        
        int estoqueAnterior = estoque.quantidadeProduto(produto2);
        vendedor.cadastrarPedidos(scanner);
        int estoqueAposVenda = estoque.quantidadeProduto(produto2);
        
        assertEquals(estoqueAnterior - 5, estoqueAposVenda);
        assertEquals(20, estoqueAposVenda); // 25 - 5 = 20
    }

    @Test
    void testPedidoComMultiplosItens() {
        String input = "Cliente Teste\n" +
                      "NB001\n" +           // notebook
                      "1\n" +               // quantidade 1
                      "s\n" +               // adicionar mais itens
                      "M001\n" +            // mouse
                      "2\n" +               // quantidade 2
                      "n\n" +               // não adicionar mais
                      "Cartão\n" +
                      "Entrega\n";
        
        Scanner scanner = new Scanner(input);
        vendedor.cadastrarPedidos(scanner);
        
        assertEquals(1, vendedor.getPedidosRealizados().size());
        
        Pedido pedido = vendedor.getPedidosRealizados().get(0);
        assertEquals(2, pedido.getItens().size());
        
        // valor total deve ser: 1 * 2500 + 2 *50 = 2600
        assertEquals(new BigDecimal("2600.00"), pedido.getValorTotal());
    }
}
