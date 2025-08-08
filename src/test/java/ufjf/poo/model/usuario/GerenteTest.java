package ufjf.poo.model.usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import ufjf.poo.model.Unidade;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.pedido.ItemPedido;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

class GerenteTest {

    private Gerente gerente;
    private Unidade unidade;
    private Estoque estoque;
    private Vendedor vendedor1;
    private Vendedor vendedor2;
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
        unidade = new Unidade(1, "Franquia Centro", "Rua Central, 123", estoque);
        
        gerente = new Gerente("Ana Gerente", 1, "senha123", "ana@email.com", unidade);
        
        vendedor1 = new Vendedor("João Vendedor", 2, "senha456", "joao@email.com", estoque);
        vendedor2 = new Vendedor("Maria Vendedora", 3, "senha789", "maria@email.com", estoque);
        
        unidade.adicionarVendedor(vendedor1);
        unidade.adicionarVendedor(vendedor2);
    }

    @Test
    void testCriacaoGerente() {
        assertEquals("Ana Gerente", gerente.getNome());
        assertEquals(1, gerente.getId());
        assertEquals("senha123", gerente.getPrivateKey());
        assertEquals("ana@email.com", gerente.getEmail());
    }

    @Test
    void testRelatorioAtividadesComVendedores() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerente.relatorioAtividades();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Gerenciamento de Equipe"));
        assertTrue(output.contains("João Vendedor"));
        assertTrue(output.contains("Maria Vendedora"));
        assertTrue(output.contains("Franquia Centro"));
        
        System.setOut(System.out);
    }

    @Test
    void testRelatorioAtividadesSemVendedores() {
        Unidade unidadeVazia = new Unidade(2, "Franquia Vazia", "Rua Vazia, 456", estoque);
        Gerente gerenteVazio = new Gerente("Carlos", 4, "senha000", "carlos@email.com", unidadeVazia);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerenteVazio.relatorioAtividades();
        
        String output = outputStream.toString();
        assertTrue(output.contains("A unidade não possui vendedores"));
        
        System.setOut(System.out);
    }

    @Test
    void testControlaPedidosComVendedores() {
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto2, 1, produto2.getPreco()));
        
        vendedor1.cadastrarPedidos("Cliente Teste", "Cartão", "Entrega", itens);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerente.controlaPedidos();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Controle de Pedidos da Unidade"));
        assertTrue(output.contains("João Vendedor"));
        
        System.setOut(System.out);
    }

    @Test
    void testControlaPedidosSemVendedores() {
        Unidade unidadeVazia = new Unidade(2, "Franquia Vazia", "Rua Vazia, 456", estoque);
        Gerente gerenteVazio = new Gerente("Carlos", 4, "senha000", "carlos@email.com", unidadeVazia);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerenteVazio.controlaPedidos();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Nenhum vendedor Cadastrado!"));
        
        System.setOut(System.out);
    }

    @Test
    void testAdministrarEstoqueAdicionar() {
        Produto produtoNovo = new Produto("Teclado", "T001", new BigDecimal("150.00"));
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerente.administrarEstoque(produtoNovo, 20, "adicionar");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Produto Teclado adicionado com sucesso"));
        assertEquals(20, estoque.quantidadeProduto(produtoNovo));
        

        System.setOut(System.out);
    }

    @Test
    void testAdministrarEstoqueRemover() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerente.administrarEstoque(produto1, 0, "remover");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Produto Notebook removido com sucesso"));
        assertFalse(estoque.produtoEmEstoque(produto1));
        
        System.setOut(System.out);
    }

    @Test
    void testAdministrarEstoqueListar() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerente.administrarEstoque(produto1, 0, "listar");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Produtos no Estoque"));
        
        System.setOut(System.out);
    }

    @Test
    void testAdministrarEstoqueAcaoInvalida() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerente.administrarEstoque(produto1, 5, "acao_invalida");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Ação 'acao_invalida' não reconhecida"));
        
        System.setOut(System.out);
    }

    @Test
    void testAdministrarEstoqueSemEstoque() {
        Unidade unidadeSemEstoque = new Unidade(2, "Franquia Sem Estoque", "Rua Vazia, 456", null);
        Gerente gerenteSemEstoque = new Gerente("Carlos", 4, "senha000", "carlos@email.com", unidadeSemEstoque);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerenteSemEstoque.administrarEstoque(produto1, 10, "adicionar");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Estoque não configurado para a unidade"));
        
        System.setOut(System.out);
    }

    @Test
    void testRelatorioDeDesempenho() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerente.relatorioDeDesempenho();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Relatorio da Unidade"));
        
        System.setOut(System.out);
    }

    @Test
    void testGerenteComUnidadeValida() {
        assertNotNull(gerente);
        assertEquals("Ana Gerente", gerente.getNome());
    }

    @Test
    void testFuncionalidadesGerenteIntegradas() {
        // gerente adiciona produto, vendedor faz pedido, gerente controla
        Produto produtoNovo = new Produto("Headset", "H001", new BigDecimal("200.00"));
        gerente.administrarEstoque(produtoNovo, 15, "adicionar");
        
        // verificar se produto foi adicionado
        assertTrue(estoque.produtoEmEstoque(produtoNovo));
        assertEquals(15, estoque.quantidadeProduto(produtoNovo));
        
        // vendedor faz pedido
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produtoNovo, 2, produtoNovo.getPreco().multiply(new BigDecimal("2"))));
        
        vendedor1.cadastrarPedidos("Cliente TESTE", "PIX", "Retirada", itens);
        
        // verificar se pedido foi criado
        assertEquals(1, vendedor1.getPedidosRealizados().size());
        
        // verificar se estoque foi atualizado
        assertEquals(13, estoque.quantidadeProduto(produtoNovo)); // 15 - 2 = 13
        
        // gerente pode ver os pedidos
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        gerente.controlaPedidos();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Cliente TESTE"));
        
        System.setOut(System.out);
    }
}
