package ufjf.poo.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class UnidadeTest {

    private Unidade unidade;
    private Estoque estoque;
    private Gerente gerente;
    private Vendedor vendedor1;
    private Vendedor vendedor2;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setUp() {
        produto1 = new Produto("Notebook", "NB001", 2500.0f);
        produto2 = new Produto("Mouse", "M001", 50.0f);
        
        HashMap<Produto, Integer> produtos = new HashMap<>();
        produtos.put(produto1, 10);
        produtos.put(produto2, 25);
        
        estoque = new Estoque(produtos);
        unidade = new Unidade(1, "Franquia Centro", "Rua Central, 123", estoque);
        
        gerente = new Gerente("Ana Gerente", 2, "senha123", "ana@email.com", unidade);
        vendedor1 = new Vendedor("João Vendedor", 3, "senha456", "joao@email.com", estoque);
        vendedor2 = new Vendedor("Maria Vendedora", 4, "senha789", "maria@email.com", estoque);
    }

    @Test
    void testCriacaoUnidade() {
        assertEquals(1, unidade.getId());
        assertEquals("Franquia Centro", unidade.getNome());
        assertEquals("Rua Central, 123", unidade.getEndereco());
        assertEquals(estoque, unidade.getEstoque());
        assertNull(unidade.getGerente());
        assertTrue(unidade.getVendedores().isEmpty());
    }

    @Test
    void testTrocarGerente() {
        unidade.trocarGerente(gerente);
        assertEquals(gerente, unidade.getGerente());
        
        Gerente novoGerente = new Gerente("Carlos", 5, "senha000", "carlos@email.com", unidade);
        unidade.trocarGerente(novoGerente);
        assertEquals(novoGerente, unidade.getGerente());
    }

    @Test
    void testTrocarGerenteParaNull() {
        unidade.trocarGerente(gerente);
        assertEquals(gerente, unidade.getGerente());
        
        unidade.trocarGerente(null);
        assertNull(unidade.getGerente());
    }

    @Test
    void testAdicionarVendedor() {
        unidade.adicionarVendedor(vendedor1);
        assertEquals(1, unidade.getVendedores().size());
        assertTrue(unidade.getVendedores().contains(vendedor1));
        
        unidade.adicionarVendedor(vendedor2);
        assertEquals(2, unidade.getVendedores().size());
        assertTrue(unidade.getVendedores().contains(vendedor2));
    }

    @Test
    void testAdicionarVendedorNulo() {
        unidade.adicionarVendedor(null);
        assertTrue(unidade.getVendedores().isEmpty());
    }

    @Test
    void testRemoverVendedor() {
        unidade.adicionarVendedor(vendedor1);
        unidade.adicionarVendedor(vendedor2);
        assertEquals(2, unidade.getVendedores().size());
        
        unidade.removerVendedor(vendedor1);
        assertEquals(1, unidade.getVendedores().size());
        assertFalse(unidade.getVendedores().contains(vendedor1));
        assertTrue(unidade.getVendedores().contains(vendedor2));
    }

    @Test
    void testRemoverVendedorInexistente() {
        unidade.adicionarVendedor(vendedor1);
        assertEquals(1, unidade.getVendedores().size());
        
        unidade.removerVendedor(vendedor2);
        assertEquals(1, unidade.getVendedores().size());
    }

    @Test
    void testGerarRelatorioVendasSemVendedores() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        unidade.gerarRelatorioVendas();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Nao ha Vendedores!"));
        
        System.setOut(System.out);
    }

    @Test
    void testGerarRelatorioVendasComVendedoresSemPedidos() {
        unidade.adicionarVendedor(vendedor1);
        unidade.adicionarVendedor(vendedor2);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        unidade.gerarRelatorioVendas();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Relatorio de vendas por vendedor"));
        assertTrue(output.contains("João Vendedor"));
        assertTrue(output.contains("Maria Vendedora"));
        assertTrue(output.contains("Resumo da Unidade"));
        
        System.setOut(System.out);
    }

    @Test
    void testGerarRelatorioVendasComPedidos() {
        unidade.adicionarVendedor(vendedor1);
        unidade.adicionarVendedor(vendedor2);
        
        String inputPedido1 = "Cliente A\nNB001\n1\nn\nCartão\nEntrega\n";
        String inputPedido2 = "Cliente B\nM001\n2\nn\nPIX\nRetirada\n";
        
        Scanner scanner1 = new Scanner(inputPedido1);
        Scanner scanner2 = new Scanner(inputPedido2);
        
        vendedor1.cadastrarPedidos(scanner1);
        vendedor2.cadastrarPedidos(scanner2);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        unidade.gerarRelatorioVendas();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Relatorio de vendas por vendedor"));
        assertTrue(output.contains("João Vendedor"));
        assertTrue(output.contains("Maria Vendedora"));
        assertTrue(output.contains("1 pedidos"));
        assertTrue(output.contains("Resumo da Unidade"));
        
        System.setOut(System.out);
    }

    @Test
    void testGetVendedoresRetornaListaOriginal() {
        unidade.adicionarVendedor(vendedor1);
        List<Vendedor> vendedores = unidade.getVendedores();
        
        assertEquals(1, vendedores.size());
        assertEquals(vendedor1, vendedores.get(0));
    }

    @Test
    void testUnidadeComEstoqueNulo() {
        Unidade unidadeSemEstoque = new Unidade(2, "Franquia Sem Estoque", "Rua Teste, 456", null);
        
        assertEquals(2, unidadeSemEstoque.getId());
        assertEquals("Franquia Sem Estoque", unidadeSemEstoque.getNome());
        assertEquals("Rua Teste, 456", unidadeSemEstoque.getEndereco());
        assertNull(unidadeSemEstoque.getEstoque());
    }

}
