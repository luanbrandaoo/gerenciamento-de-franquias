package ufjf.poo.model.pedido;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Vendedor;
import java.util.Date;
import java.util.List;
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
        produto1 = new Produto("Notebook", "NB001", 2500.0f);
        produto2 = new Produto("Mouse", "M001", 50.0f);
        
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
        assertEquals(0.0, pedido.getValorTotal());
        assertTrue(pedido.getItens().isEmpty());
    }

    @Test
    void testCriacaoPedidoComIdNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pedido(-1, "Cliente", vendedor, data, "Cartão", "Entrega");
        });
    }

    @Test
    void testCriacaoPedidoComNomeClienteNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pedido(1, null, vendedor, data, "Cartão", "Entrega");
        });
    }

    @Test
    void testCriacaoPedidoComNomeClienteVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pedido(1, "", vendedor, data, "Cartão", "Entrega");
        });
    }

    @Test
    void testCriacaoPedidoComFormaPagamentoNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pedido(1, "Cliente", vendedor, data, null, "Entrega");
        });
    }

    @Test
    void testCriacaoPedidoComFormaPagamentoVazia() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pedido(1, "Cliente", vendedor, data, "", "Entrega");
        });
    }

    @Test
    void testCriacaoPedidoComFormaEntregaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pedido(1, "Cliente", vendedor, data, "Cartão", null);
        });
    }

    @Test
    void testCriacaoPedidoComFormaEntregaVazia() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pedido(1, "Cliente", vendedor, data, "Cartão", "");
        });
    }

    @Test
    void testAdicionarItemUnico() {
        pedido.adicionarItem(produto1, 2);
        
        List<ItemPedido> itens = pedido.getItens();
        assertEquals(1, itens.size());
        assertEquals(produto1, itens.get(0).produto());
        assertEquals(2, itens.get(0).quantidade());
        assertEquals(5000.0, pedido.getValorTotal()); // 2500 * 2
    }

    @Test
    void testAdicionarItemProdutoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            pedido.adicionarItem(null, 1);
        });
    }

    @Test
    void testAdicionarListaItens() {
        List<ItemPedido> novosItens = new ArrayList<>();
        novosItens.add(new ItemPedido(produto1, 1, 2500.0));
        novosItens.add(new ItemPedido(produto2, 2, 100.0));
        
        pedido.adicionarItem(novosItens);
        
        assertEquals(2, pedido.getItens().size());
        assertEquals(2600.0, pedido.getValorTotal()); // 2500 + 100
    }

    @Test
    void testAdicionarListaItensNula() {
        pedido.adicionarItem((List<ItemPedido>) null);
        assertEquals(0, pedido.getItens().size());
        assertEquals(0.0, pedido.getValorTotal());
    }

    @Test
    void testAdicionarListaItensVazia() {
        pedido.adicionarItem(new ArrayList<>());
        assertEquals(0, pedido.getItens().size());
        assertEquals(0.0, pedido.getValorTotal());
    }

    @Test
    void testRemoverItem() {
        pedido.adicionarItem(produto1, 2);
        pedido.adicionarItem(produto2, 1);
        
        assertEquals(2, pedido.getItens().size());
        
        pedido.removerItem(produto1);
        
        assertEquals(1, pedido.getItens().size());
        assertEquals(produto2, pedido.getItens().get(0).produto());
        assertEquals(50.0, pedido.getValorTotal()); // apenas o mouse
    }

    @Test
    void testRemoverItemProdutoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            pedido.removerItem(null);
        });
    }

    @Test
    void testCalcularTotal() {
        pedido.adicionarItem(produto1, 1); // 2500
        pedido.adicionarItem(produto2, 3); // 150
        
        assertEquals(2650.0, pedido.getValorTotal());
    }

    @Test
    void testCalcularTotalSemItens() {
        pedido.calcularTotal();
        assertEquals(0.0, pedido.getValorTotal());
    }

    @Test
    void testSetStatus() {
        pedido.setStatus("Concluído");
        assertEquals("Concluído", pedido.getStatus());
    }

    @Test
    void testSetStatusNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            pedido.setStatus(null);
        });
    }

    @Test
    void testSetStatusVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
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
        assertThrows(IllegalArgumentException.class, () -> {
            pedido.setFormaDePagamento(null);
        });
    }

    @Test
    void testSetFormaDePagamentoVazia() {
        assertThrows(IllegalArgumentException.class, () -> {
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
        assertThrows(IllegalArgumentException.class, () -> {
            pedido.setFormaDeEntrega(null);
        });
    }

    @Test
    void testSetFormaDeEntregaVazia() {
        assertThrows(IllegalArgumentException.class, () -> {
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
