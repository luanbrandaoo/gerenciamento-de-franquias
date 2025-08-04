package ufjf.poo.model.estoque;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class EstoqueTest {

    private Estoque estoque;
    private Produto produto1;
    private Produto produto2;
    private Map<Produto, Integer> produtos;

    @BeforeEach
    void setUp() {
        produto1 = new Produto("Notebook", "NB001", 2500.0f);
        produto2 = new Produto("Mouse", "M001", 50.0f);
        
        produtos = new HashMap<>();
        produtos.put(produto1, 10);
        produtos.put(produto2, 25);
        
        estoque = new Estoque(produtos);
    }

    @Test
    void testCriacaoEstoque() {
        assertEquals(10, estoque.quantidadeProduto(produto1));
        assertEquals(25, estoque.quantidadeProduto(produto2));
    }

    @Test
    void testAdicionarProdutoNovo() {
        Produto produtoNovo = new Produto("Teclado", "T001", 150.0f);
        estoque.adicionarProduto(produtoNovo, 15);
        
        assertEquals(15, estoque.quantidadeProduto(produtoNovo));
        assertTrue(estoque.produtoEmEstoque(produtoNovo));
    }

    @Test
    void testAdicionarProdutoExistente() {
        estoque.adicionarProduto(produto1, 5);
        assertEquals(15, estoque.quantidadeProduto(produto1)); // 10 + 5
    }

    @Test
    void testRemoverProduto() {
        estoque.removerProduto(produto1);
        assertFalse(estoque.produtoEmEstoque(produto1));
        assertEquals(0, estoque.quantidadeProduto(produto1));
    }

    @Test
    void testSubtrairProduto() {
        estoque.subtraiProduto(produto1, 3);
        assertEquals(7, estoque.quantidadeProduto(produto1)); // 10 - 3
    }

    @Test
    void testSubtrairProdutoQuantidadeMaiorQueEstoque() {
        estoque.subtraiProduto(produto1, 15);
        assertEquals(0, estoque.quantidadeProduto(produto1)); // não pode ficar negativo
    }

    @Test
    void testAtualizarProduto() {
        estoque.atualizarProduto(produto1, 30);
        assertEquals(30, estoque.quantidadeProduto(produto1));
    }

    @Test
    void testQuantidadeProdutoInexistente() {
        Produto produtoInexistente = new Produto("Inexistente", "I001", 1.0f);
        assertEquals(0, estoque.quantidadeProduto(produtoInexistente));
    }

    @Test
    void testProdutosEmEstoque() {
        Set<Produto> produtosEmEstoque = estoque.produtosEmEstoque();
        assertTrue(produtosEmEstoque.contains(produto1));
        assertTrue(produtosEmEstoque.contains(produto2));
        assertEquals(2, produtosEmEstoque.size());
    }

    @Test
    void testMapeiaEstoque() {
        Map<Produto, Integer> mapeamento = estoque.mapeiaEstoque();
        assertEquals(10, mapeamento.get(produto1));
        assertEquals(25, mapeamento.get(produto2));
    }

    @Test
    void testLimparEstoque() {
        estoque.limparEstoque();
        assertTrue(estoque.produtosEmEstoque().isEmpty());
        assertEquals(0, estoque.quantidadeProduto(produto1));
        assertEquals(0, estoque.quantidadeProduto(produto2));
    }

    @Test
    void testProdutoEmEstoque() {
        assertTrue(estoque.produtoEmEstoque(produto1));
        assertTrue(estoque.produtoEmEstoque(produto2));
        
        Produto produtoInexistente = new Produto("Inexistente", "I001", 1.0f);
        assertFalse(estoque.produtoEmEstoque(produtoInexistente));
    }

    @Test
    void testProdutoEmEstoqueComQuantidadeZero() {
        estoque.atualizarProduto(produto1, 0);
        assertFalse(estoque.produtoEmEstoque(produto1));
    }

    @Test
    void testBuscarProdutoPorCodigo() {
        Produto produtoEncontrado = estoque.buscarProdutoPorCodigo("NB001");
        assertEquals(produto1, produtoEncontrado);
        
        Produto produtoNaoEncontrado = estoque.buscarProdutoPorCodigo("INEXISTENTE");
        assertNull(produtoNaoEncontrado);
    }

    @Test
    void testEstaDisponivel() {
        assertTrue(estoque.estaDisponivel(produto1, 5)); // tem 10, quer 5
        assertTrue(estoque.estaDisponivel(produto1, 10)); // tem 10, quer 10
        assertFalse(estoque.estaDisponivel(produto1, 15)); // tem 10, quer 15
    }

    @Test
    void testEstaDisponivelProdutoInexistente() {
        Produto produtoInexistente = new Produto("Inexistente", "I001", 1.0f);
        assertFalse(estoque.estaDisponivel(produtoInexistente, 1));
    }
}
