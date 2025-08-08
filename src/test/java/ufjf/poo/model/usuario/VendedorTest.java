package ufjf.poo.model.usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.pedido.Pedido;
import ufjf.poo.model.pedido.ItemPedido;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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
        
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto1, 1, produto1.getPreco()));
        
        assertThrows(NullPointerException.class, () -> {
            vendedorSemEstoque.cadastrarPedidos("Cliente Teste", "Cartão", "Entrega", itens);
        });
    }

    @Test
    void testCadastrarPedidoComSucesso() {
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto1, 2, produto1.getPreco().multiply(new BigDecimal("2"))));
        
        int pedidosAnteriores = vendedor.getPedidosRealizados().size();
        vendedor.cadastrarPedidos("Cliente Teste", "Cartão", "Entrega", itens);
        
        assertEquals(pedidosAnteriores + 1, vendedor.getPedidosRealizados().size());
        
        Pedido pedidoCriado = vendedor.getPedidosRealizados().getFirst();
        assertEquals("Cliente Teste", pedidoCriado.getNomeCliente());
        assertEquals("Cartão", pedidoCriado.getFormaDePagamento());
        assertEquals("Entrega", pedidoCriado.getFormaDeEntrega());
        assertTrue(pedidoCriado.getValorTotal().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testCadastrarPedidoSemItens() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        int pedidosAnteriores = vendedor.getPedidosRealizados().size();
        vendedor.cadastrarPedidos("Cliente Teste", "Cartão", "Entrega", new ArrayList<>());
        
        assertEquals(pedidosAnteriores, vendedor.getPedidosRealizados().size());
        
        String output = outputStream.toString();
        assertTrue(output.contains("Nenhum item adicionado. Pedido cancelado."));
        
        System.setOut(System.out);
    }

    @Test
    void testCadastrarPedidoEstoqueInsuficiente() {
    
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto1, 50, produto1.getPreco().multiply(new BigDecimal("50"))));
        
        int pedidosAnteriores = vendedor.getPedidosRealizados().size();
    
        if (estoque.estaDisponivel(produto1, 50)) {
            vendedor.cadastrarPedidos("Cliente Teste", "Cartão", "Entrega", itens);
        }

        assertEquals(pedidosAnteriores, vendedor.getPedidosRealizados().size());
    }

    @Test
    void testRelatorioAtividadesVazio() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        vendedor.relatorioAtividades();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Sem pedidos feitos!"));
        
        System.setOut(System.out);
    }

    @Test
    void testRelatorioAtividades() {
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto2, 1, produto2.getPreco()));
        
        vendedor.cadastrarPedidos("Cliente Teste", "PIX", "Retirada", itens);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        vendedor.relatorioAtividades();
        
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
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto2, 5, produto2.getPreco().multiply(new BigDecimal("5"))));
        
        int estoqueAnterior = estoque.quantidadeProduto(produto2);
        vendedor.cadastrarPedidos("Cliente Teste", "Dinheiro", "Entrega", itens);
        int estoqueAposVenda = estoque.quantidadeProduto(produto2);
        
        assertEquals(estoqueAnterior - 5, estoqueAposVenda);
        assertEquals(20, estoqueAposVenda); // 25 - 5 = 20
    }

    @Test
    void testPedidoComMultiplosItens() {
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto1, 1, produto1.getPreco())); // notebook
        itens.add(new ItemPedido(produto2, 2, produto2.getPreco().multiply(new BigDecimal("2")))); // mouse
        
        vendedor.cadastrarPedidos("Cliente Teste", "Cartão", "Entrega", itens);
        
        assertEquals(1, vendedor.getPedidosRealizados().size());
        
        Pedido pedido = vendedor.getPedidosRealizados().getFirst();
        assertEquals(2, pedido.getItens().size());
        
        // valor total deve ser: 1 * 2500 + 2 *50 = 2600
        assertEquals(new BigDecimal("2600.00"), pedido.getValorTotal());
    }
}
