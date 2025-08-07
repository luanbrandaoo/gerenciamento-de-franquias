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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class DonoTest {

    private Dono dono;
    private List<Unidade> unidades;
    private Unidade unidade1;
    private Unidade unidade2;
    private Gerente gerente1;
    private Gerente gerente2;
    private Vendedor vendedor1;
    private Vendedor vendedor2;
    private Estoque estoque1;
    private Estoque estoque2;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setUp() {
        produto1 = new Produto("Notebook", "NB001", new BigDecimal("2500.00"));
        produto2 = new Produto("Mouse", "M001", new BigDecimal("50.00"));
        
        HashMap<Produto, Integer> produtos1 = new HashMap<>();
        produtos1.put(produto1, 10);
        produtos1.put(produto2, 25);
        
        HashMap<Produto, Integer> produtos2 = new HashMap<>();
        produtos2.put(produto1, 5);
        produtos2.put(produto2, 15);
        
        estoque1 = new Estoque(produtos1);
        estoque2 = new Estoque(produtos2);
        
        unidade1 = new Unidade(1, "Franquia Centro", "Rua Central, 123", estoque1);
        unidade2 = new Unidade(2, "Franquia Sul", "Rua Sul, 456", estoque2);
        
        gerente1 = new Gerente("Ana Gerente", 2, "senha123", "ana@email.com", unidade1);
        gerente2 = new Gerente("Carlos Gerente", 3, "senha456", "carlos@email.com", unidade2);
        
        unidade1.trocarGerente(gerente1);
        unidade2.trocarGerente(gerente2);
        
        vendedor1 = new Vendedor("João Vendedor", 4, "senha789", "joao@email.com", estoque1);
        vendedor2 = new Vendedor("Maria Vendedora", 5, "senha000", "maria@email.com", estoque2);
        
        unidade1.adicionarVendedor(vendedor1);
        unidade2.adicionarVendedor(vendedor2);
        
        unidades = new ArrayList<>();
        unidades.add(unidade1);
        unidades.add(unidade2);
        
        dono = new Dono("Roberto Dono", 1, "senhamaster", "roberto@email.com", unidades);
    }

    @Test
    void testCriacaoDono() {
        assertEquals("Roberto Dono", dono.getNome());
        assertEquals(1, dono.getId());
        assertEquals("senhamaster", dono.getPrivateKey());
        assertEquals("roberto@email.com", dono.getEmail());
    }

    @Test
    void testCriacaoDonoComListaVazia() {
        List<Unidade> listaVazia = new ArrayList<>();
        Dono donoSemUnidades = new Dono("Pedro", 10, "senha", "pedro@email.com", listaVazia);
        
        assertNotNull(donoSemUnidades);
        assertEquals("Pedro", donoSemUnidades.getNome());
    }

    @Test
    void testGerenciaUnidadesComUnidades() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        dono.gerenciaUnidades();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Gerenciamento de Unidades"));
        assertTrue(output.contains("Franquia Centro"));
        assertTrue(output.contains("Franquia Sul"));
        assertTrue(output.contains("Ana Gerente"));
        assertTrue(output.contains("Carlos Gerente"));
        
        System.setOut(System.out);
    }

    @Test
    void testGerenciaUnidadesSemUnidades() {
        List<Unidade> listaVazia = new ArrayList<>();
        Dono donoSemUnidades = new Dono("Pedro", 10, "senha", "pedro@email.com", listaVazia);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        donoSemUnidades.gerenciaUnidades();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Nenuma unidade cadastrada!"));
        
        System.setOut(System.out);
    }

    @Test
    void testGerenciaUnidadeSemGerente() {
        Unidade unidadeSemGerente = new Unidade(3, "Franquia Norte", "Rua Norte, 789", estoque1);
        
        List<Unidade> unidadesComSemGerente = new ArrayList<>();
        unidadesComSemGerente.add(unidade1);
        unidadesComSemGerente.add(unidade2);
        unidadesComSemGerente.add(unidadeSemGerente);
        
        Dono donoComUnidadeSemGerente = new Dono("Roberto Dono", 1, "senhamaster", "roberto@email.com", unidadesComSemGerente);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        donoComUnidadeSemGerente.gerenciaUnidades();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Nao ha gerentes subordinados para a unidade: Franquia Norte"));
        
        System.setOut(System.out);
    }

    @Test
    void testRelatorioUnidadesComUnidades() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        dono.relatorioUnidades();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Relatório Consolidado de Unidades"));
        assertTrue(output.contains("Relatorio da Uniade: Franquia Centro"));
        assertTrue(output.contains("Relatorio da Uniade: Franquia Sul"));
        
        System.setOut(System.out);
    }

    @Test
    void testRelatorioUnidadesSemUnidades() {
        List<Unidade> listaVazia = new ArrayList<>();
        Dono donoSemUnidades = new Dono("Pedro", 10, "senha", "pedro@email.com", listaVazia);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        donoSemUnidades.relatorioUnidades();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Nenhuma unidade para gerar relatório"));
        
        System.setOut(System.out);
    }

    @Test
    void testRelatorioVendedoresComVendas() {
        List<ItemPedido> itens = new ArrayList<>();
        itens.add(new ItemPedido(produto2, 2, produto2.getPreco().multiply(new BigDecimal("2"))));
        
        vendedor1.cadastrarPedidos("Cliente Teste", "Cartão", "Entrega", itens);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        dono.relatorioVendedores();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Relatório de Desempenho por Vendedor"));
        assertTrue(output.contains("João Vendedor"));
        assertTrue(output.contains("Franquia Centro"));
        
        System.setOut(System.out);
    }

    @Test
    void testRelatorioVendedoresSemUnidades() {
        List<Unidade> listaVazia = new ArrayList<>();
        Dono donoSemUnidades = new Dono("Pedro", 10, "senha", "pedro@email.com", listaVazia);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        donoSemUnidades.relatorioVendedores();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Nenhuma unidade para gerar relatório"));
        
        System.setOut(System.out);
    }

    @Test
    void testListarTodosUsuarios() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        dono.listarTodosUsuarios();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Lista de Gerentes e Vendedores"));
        assertTrue(output.contains("Franquia Centro"));
        assertTrue(output.contains("Franquia Sul"));
        assertTrue(output.contains("Ana Gerente"));
        assertTrue(output.contains("Carlos Gerente"));
        assertTrue(output.contains("João Vendedor"));
        assertTrue(output.contains("Maria Vendedora"));
        
        System.setOut(System.out);
    }

    @Test
    void testListarTodosUsuariosSemUnidades() {
        List<Unidade> listaVazia = new ArrayList<>();
        Dono donoSemUnidades = new Dono("Pedro", 10, "senha", "pedro@email.com", listaVazia);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        donoSemUnidades.listarTodosUsuarios();
        
        String output = outputStream.toString();
        assertTrue(output.contains("Nenhuma unidade cadastrada para listar usuários"));
        
        System.setOut(System.out);
    }

    @Test
    void testEditaUsuarioGerente() {
        String input = "2\n" + "Ana Silva\n" + "ana.silva@email.com\n";

        Scanner scanner = new Scanner(input);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        dono.editaUsuarios(scanner);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Usuario Encontrado: ID 2 Nome: Ana Gerente"));
        assertTrue(output.contains("Informações do usuário atualizadas com sucesso"));
        
        assertEquals("Ana Silva", gerente1.getNome());
        assertEquals("ana.silva@email.com", gerente1.getEmail());
        
        System.setOut(System.out);
    }

    @Test
    void testEditaUsuarioVendedor() {
        String input = "4\n" +"João Santos\n" +"\n";
        
        Scanner scanner = new Scanner(input);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        dono.editaUsuarios(scanner);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Usuario Encontrado: ID 4 Nome: João Vendedor"));
        assertTrue(output.contains("Informações do usuário atualizadas com sucesso"));
        
        assertEquals("João Santos", vendedor1.getNome());
        assertEquals("joao@email.com", vendedor1.getEmail());
        
        System.setOut(System.out);
    }

    @Test
    void testEditaUsuarioIdInexistente() {
        String input = "999\n";
        
        Scanner scanner = new Scanner(input);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        dono.editaUsuarios(scanner);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Usuário com ID 999 não encontrado"));
        
        System.setOut(System.out);
    }

    @Test
    void testEditaUsuarioIdNegativo() {
        String input = "-1\n";
        
        Scanner scanner = new Scanner(input);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        dono.editaUsuarios(scanner);
        
        String output = outputStream.toString();
        assertTrue(output.contains("ID invalido!"));
        
        System.setOut(System.out);
    }

    @Test
    void testEditaUsuariosSemUnidades() {
        List<Unidade> listaVazia = new ArrayList<>();
        Dono donoSemUnidades = new Dono("Pedro", 10, "senha", "pedro@email.com", listaVazia);
        
        Scanner scanner = new Scanner("1\n");
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        donoSemUnidades.editaUsuarios(scanner);
        
        String output = outputStream.toString();
        assertTrue(output.contains("Nenhuma unidade cadastrada para listar usuários"));
        
        System.setOut(System.out);
    }

    @Test
    void testFuncionalidadesDonoIntegradas() {
        
        //verificar se pode listar unidades
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        dono.gerenciaUnidades();
        String output1 = outputStream.toString();
        assertTrue(output1.contains("Franquia Centro"));
        assertTrue(output1.contains("Franquia Sul"));

        //criar pedidos para gerar dados de vendas
        List<ItemPedido> itens1 = new ArrayList<>();
        itens1.add(new ItemPedido(produto1, 1, produto1.getPreco()));
        
        List<ItemPedido> itens2 = new ArrayList<>();
        itens2.add(new ItemPedido(produto2, 5, produto2.getPreco().multiply(new BigDecimal("5"))));
        
        vendedor1.cadastrarPedidos("Cliente A", "Cartão", "Entrega", itens1);
        vendedor2.cadastrarPedidos("Cliente B", "PIX", "Retirada", itens2);
        
        //verificar relatório de vendedores
        outputStream.reset();
        dono.relatorioVendedores();
        String output2 = outputStream.toString();
        assertTrue(output2.contains("João Vendedor"));
        assertTrue(output2.contains("Maria Vendedora"));

        //verificar relatório de unidades
        outputStream.reset();
        dono.relatorioUnidades();
        String output3 = outputStream.toString();
        assertTrue(output3.contains("Relatório Consolidado"));
        
        System.setOut(System.out);
    }
}
