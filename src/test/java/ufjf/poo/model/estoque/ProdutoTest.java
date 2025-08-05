package ufjf.poo.model.estoque;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import ufjf.poo.exception.PrecoInvalidoException;
import ufjf.poo.exception.NomeProdutoInvalidoException;
import ufjf.poo.exception.CodigoProdutoInvalidoException;

class ProdutoTest {

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto("Notebook", "NB001", new BigDecimal("2500.00"));
    }

    @Test
    void testCriacaoProdutoValido() {
        assertEquals("Notebook", produto.getNome());
        assertEquals("NB001", produto.getCodigo());
        assertEquals(new BigDecimal("2500.00"), produto.getPreco());
    }

    @Test
    void testCriacaoProdutoComPrecoNegativo() {
        assertThrows(PrecoInvalidoException.class, () -> {
            new Produto("Produto", "P001", new BigDecimal("-10.00"));
        });
    }

    @Test
    void testCriacaoProdutoComNomeNulo() {
        assertThrows(NomeProdutoInvalidoException.class, () -> {
            new Produto(null, "P001", new BigDecimal("100.00"));
        });
    }

    @Test
    void testCriacaoProdutoComNomeVazio() {
        assertThrows(NomeProdutoInvalidoException.class, () -> {
            new Produto("", "P001", new BigDecimal("100.00"));
        });
    }

    @Test
    void testCriacaoProdutoComCodigoNulo() {
        assertThrows(CodigoProdutoInvalidoException.class, () -> {
            new Produto("Produto", null, new BigDecimal("100.00"));
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
        produto.setPreco(new BigDecimal("3000.00"));
        assertEquals(new BigDecimal("3000.00"), produto.getPreco());
    }

    @Test
    void testSetPrecoNegativo() {
        BigDecimal precoOriginal = produto.getPreco();
        produto.setPreco(new BigDecimal("-100.00"));
        assertEquals(precoOriginal, produto.getPreco());
    }

    @Test
    void testSetPrecoZero() {
        BigDecimal precoOriginal = produto.getPreco();
        produto.setPreco(BigDecimal.ZERO);
        assertEquals(precoOriginal, produto.getPreco());
    }
}
