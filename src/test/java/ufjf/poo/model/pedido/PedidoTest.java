package ufjf.poo.model.pedido;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Vendedor;
import ufjf.poo.exception.IdPedidoInvalidoException;
import ufjf.poo.exception.NomeClienteInvalidoException;
import ufjf.poo.exception.FormaPagamentoInvalidaException;
import ufjf.poo.exception.FormaEntregaInvalidaException;
import ufjf.poo.exception.ProdutoNuloException;
import ufjf.poo.exception.StatusPedidoInvalidoException;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

class PedidoTest {

    private Pedido pedido;
    private Vendedor vendedor;
    private Produto produto1;
    private Produto produto2;
    private Date data;

    @BeforeEach
    void setUp() {
        produto1 = new Produto("Notebook", "NB001", new BigDecimal("2500.00"));
        produto2 = new Produto("Mouse", "M001", new BigDecimal("50.00"));
        
        Estoque estoque = new Estoque(new HashMap<>());
        vendedor = new Vendedor("João Vendedor", 1, "senha123", "joao@email.com", estoque);
        
        data = new Date();
        pedido = new Pedido(1, "Cliente Teste", vendedor, data, "Cartão", "Entrega");
    }

    @Test
    void testCriacaoPedidoValido() {
        assertEquals(1, pedido.getIdPedido());
        assertEquals("Cliente Teste", pedido.getNomeCliente());
        assertEquals(vendedor, pedido.getVendedor());
        assertEquals(data, pedido.getDataPedido());
        assertEquals("Cartão", pedido.getFormaDePagamento());
        assertEquals("Entrega", pedido.getFormaDeEntrega());
        assertEquals("Pendente", pedido.getStatus());
        assertEquals(BigDecimal.ZERO, pedido.getValorTotal());
        assertTrue(pedido.getItens().isEmpty());
    }

    @Test
    void testCriacaoPedidoComIdNegativo() {
        assertThrows(IdPedidoInvalidoException.class, () -> {
            new Pedido(-1, "Cliente", vendedor, data, "Cartão", "Entrega");
        });
    }

    @Test
    void testCriacaoPedidoComNomeClienteNulo() {
        assertThrows(NomeClienteInvalidoException.class, () -> {
            new Pedido(1, null, vendedor, data, "Cartão", "Entrega");
        });
    }

    @Test
    void testCriacaoPedidoComNomeClienteVazio() {
        assertThrows(NomeClienteInvalidoException.class, () -> {
            new Pedido(1, "", vendedor, data, "Cartão", "Entrega");
        });
    }

    @Test
    void testCriacaoPedidoComFormaPagamentoNula() {
        assertThrows(FormaPagamentoInvalidaException.class, () -> {
            new Pedido(1, "Cliente", vendedor, data, null, "Entrega");
        });
    }

    @Test
    void testCriacaoPedidoComFormaPagamentoVazia() {
        assertThrows(FormaPagamentoInvalidaException.class, () -> {
            new Pedido(1, "Cliente", vendedor, data, "", "Entrega");
        });
    }

    @Test
    void testCriacaoPedidoComFormaEntregaNula() {
        assertThrows(FormaEntregaInvalidaException.class, () -> {
            new Pedido(1, "Cliente", vendedor, data, "Cartão", null);
        });
    }

    @Test
    void testCriacaoPedidoComFormaEntregaVazia() {
        assertThrows(FormaEntregaInvalidaException.class, () -> {
            new Pedido(1, "Cliente", vendedor, data, "Cartão", "");
        });
    }

    @Test
    void testAdicionarItemUnico() {
        pedido.adicionarItem(produto1, 2);
        
        List<ItemPedido> itens = pedido.getItens();
        assertEquals(1, itens.size());
        assertEquals(produto1, itens.getFirst().produto());
        assertEquals(2, itens.getFirst().quantidade());
        assertEquals(new BigDecimal("5000.00"), pedido.getValorTotal()); // 2500 * 2
    }

    @Test
    void testAdicionarItemProdutoNulo() {
        assertThrows(ProdutoNuloException.class, () -> {
            pedido.adicionarItem(null, 1);
        });
    }

    @Test
    void testAdicionarListaItens() {
        List<ItemPedido> novosItens = new ArrayList<>();
        novosItens.add(new ItemPedido(produto1, 1, new BigDecimal("2500.00")));
        novosItens.add(new ItemPedido(produto2, 2, new BigDecimal("100.00")));
        
        pedido.adicionarItem(novosItens);
        
        assertEquals(2, pedido.getItens().size());
        assertEquals(new BigDecimal("2600.00"), pedido.getValorTotal()); // 2500 + 100
    }

    @Test
    void testAdicionarListaItensNula() {
        pedido.adicionarItem(null);
        assertEquals(0, pedido.getItens().size());
        assertEquals(BigDecimal.ZERO, pedido.getValorTotal());
    }

    @Test
    void testAdicionarListaItensVazia() {
        pedido.adicionarItem(new ArrayList<>());
        assertEquals(0, pedido.getItens().size());
        assertEquals(BigDecimal.ZERO, pedido.getValorTotal());
    }

    @Test
    void testRemoverItem() {
        pedido.adicionarItem(produto1, 2);
        pedido.adicionarItem(produto2, 1);
        
        assertEquals(2, pedido.getItens().size());
        
        pedido.removerItem(produto1);
        
        assertEquals(1, pedido.getItens().size());
        assertEquals(produto2, pedido.getItens().getFirst().produto());
        assertEquals(new BigDecimal("50.00"), pedido.getValorTotal()); // apenas o mouse
    }

    @Test
    void testRemoverItemProdutoNulo() {
        assertThrows(ProdutoNuloException.class, () -> {
            pedido.removerItem(null);
        });
    }

    @Test
    void testCalcularTotal() {
        pedido.adicionarItem(produto1, 1); // 2500
        pedido.adicionarItem(produto2, 3); // 150
        
        assertEquals(new BigDecimal("2650.00"), pedido.getValorTotal());
    }

    @Test
    void testCalcularTotalSemItens() {
        pedido.calcularTotal();
        assertEquals(BigDecimal.ZERO, pedido.getValorTotal());
    }

    @Test
    void testSetStatus() {
        pedido.setStatus("Concluído");
        assertEquals("Concluído", pedido.getStatus());
    }

    @Test
    void testSetStatusNulo() {
        assertThrows(StatusPedidoInvalidoException.class, () -> {
            pedido.setStatus(null);
        });
    }

    @Test
    void testSetStatusVazio() {
        assertThrows(StatusPedidoInvalidoException.class, () -> {
            pedido.setStatus("");
        });
    }

    @Test
    void testSetFormaDePagamento() {
        pedido.setFormaDePagamento("PIX");
        assertEquals("PIX", pedido.getFormaDePagamento());
    }

    @Test
    void testSetFormaDePagamentoNula() {
        assertThrows(FormaPagamentoInvalidaException.class, () -> {
            pedido.setFormaDePagamento(null);
        });
    }

    @Test
    void testSetFormaDePagamentoVazia() {
        assertThrows(FormaPagamentoInvalidaException.class, () -> {
            pedido.setFormaDePagamento("");
        });
    }

    @Test
    void testSetFormaDeEntrega() {
        pedido.setFormaDeEntrega("Retirada");
        assertEquals("Retirada", pedido.getFormaDeEntrega());
    }

    @Test
    void testSetFormaDeEntregaNula() {
        assertThrows(FormaEntregaInvalidaException.class, () -> {
            pedido.setFormaDeEntrega(null);
        });
    }

    @Test
    void testSetFormaDeEntregaVazia() {
        assertThrows(FormaEntregaInvalidaException.class, () -> {
            pedido.setFormaDeEntrega("");
        });
    }

    @Test
    void testGetItensRetornaCopiaSafe() {
        pedido.adicionarItem(produto1, 1);
        List<ItemPedido> itens = pedido.getItens();
        
        itens.clear();
        
        assertEquals(1, pedido.getItens().size()); // lista original deve permanecer intacta
    }
}
