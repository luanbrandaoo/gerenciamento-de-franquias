package ufjf.poo.model.estoque;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto("Notebook", "NB001", 2500*100);
    }

    @Test
    void testCriacaoProdutoValido() {
        assertEquals("Notebook", produto.getNome());
        assertEquals("NB001", produto.getCodigo());
        assertEquals(2500.0f, produto.getPreco());
    }

    @Test
    void testCriacaoProdutoComPrecoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Produto("Produto", "P001", -10*100);
        });
    }

    @Test
    void testCriacaoProdutoComNomeNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Produto(null, "P001", 100*100);
        });
    }

    @Test
    void testCriacaoProdutoComNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Produto("", "P001", 100*100);
        });
    }

    @Test
    void testCriacaoProdutoComCodigoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Produto("Produto", null, 100*100);
        });
    }

    @Test
    void testSetNomeValido() {
        produto.setNome("Laptop");
        assertEquals("Laptop", produto.getNome());
    }

    @Test
    void testSetNomeNulo() {
        String nomeOriginal = produto.getNome();
        produto.setNome(null);
        assertEquals(nomeOriginal, produto.getNome());
    }

    @Test
    void testSetNomeVazio() {
        String nomeOriginal = produto.getNome();
        produto.setNome("");
        assertEquals(nomeOriginal, produto.getNome());
    }

    @Test
    void testSetPrecoValido() {
        produto.setPreco(3000*100);
        assertEquals(300000, produto.getPreco());
    }

    @Test
    void testSetPrecoNegativo() {
        float precoOriginal = produto.getPreco();
        produto.setPreco(-100*100);
        assertEquals(precoOriginal, produto.getPreco());
    }

    @Test
    void testSetPrecoZero() {
        float precoOriginal = produto.getPreco();
        produto.setPreco(0*100);
        assertEquals(precoOriginal, produto.getPreco());
    }
}
