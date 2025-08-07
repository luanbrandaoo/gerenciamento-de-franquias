package ufjf.poo.model.usuario;

import java.util.ArrayList;
import java.util.Date;
import ufjf.poo.model.pedido.Pedido;
import java.util.List;

import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.pedido.ItemPedido;

public class Vendedor extends Usuario {

    private List<Pedido> pedidosRealizados;
    private Estoque estoqueDisponivel;
    private static int proximoIdPedido = 0;

    public Vendedor(String nome, int id, String key, String email, Estoque estoqueDisponivel) {
        super(nome, id, key, email);

        pedidosRealizados = new ArrayList<>();
        this.estoqueDisponivel = estoqueDisponivel;
    }

    public List<Pedido> getPedidosRealizados() {
        return new ArrayList<>(pedidosRealizados);
    }

    private static int gerarProximoIdPedido() {
        return proximoIdPedido++;
    }
    
    public Estoque getEstoqueDisponivel(){
        return estoqueDisponivel;
    }

    public void cadastrarPedidos(String nomeCliente, String formaDePagamento, String formaDeEntrega, List<ItemPedido> itensDoPedido) {

        if (itensDoPedido == null || itensDoPedido.isEmpty()) {
            System.out.println("Nenhum item adicionado. Pedido cancelado.");
            return;
        }

        int idPedido = Vendedor.proximoIdPedido++;
        Date dataHoraAtual = new Date();

        Pedido novoPedido = new Pedido(idPedido, nomeCliente, this, dataHoraAtual, formaDePagamento, formaDeEntrega);
        novoPedido.adicionarItem(itensDoPedido);
        novoPedido.calcularTotal(); 

        for (ItemPedido item : itensDoPedido) {
            estoqueDisponivel.subtraiProduto(item.produto(), item.quantidade());
        }

        pedidosRealizados.add(novoPedido);
        System.out.println("Pedido " + novoPedido.getIdPedido() + " cadastrado com sucesso!");
        System.out.println("Cliente: " + novoPedido.getNomeCliente() + ", Valor Total: R$" + String.format("%.2f", novoPedido.getValorTotal()));
    }

    public void visualizarPedidos() {

        if (pedidosRealizados.isEmpty()) {
            System.out.println("Sem pedidos feitos! ");
            return;
        }

        System.out.println("\n--- Histórico de Pedidos Realizados ---");

        for (Pedido pedido : pedidosRealizados) {
            System.out.println("ID do pedido: " + pedido.getIdPedido());
            System.out.println("Cliente: " + pedido.getNomeCliente());
            System.out.println("Data: " + pedido.getDataPedido());
            System.out.println("Vendedor: " + pedido.getVendedor());
            System.out.println("Status: " + pedido.getStatus());
            System.out.println("Forma de Pagamento: " + pedido.getFormaDePagamento());
            System.out.println("Forma de Entrega: " + pedido.getFormaDeEntrega());
            System.out.println("Valor Total: R$" + String.format("%.2f", pedido.getValorTotal()));

            System.out.println("Itens do Pedido: ");

            for (ItemPedido item : pedido.getItens()) {
                System.out.println(" - " + item.produto().getNome()
                        + " - " + item.quantidade() + " Subtotal: R$"
                        + String.format("%.2f", item.subtotal()));
            }
        }

        System.out.println("Total de pedidos no histórico: " + pedidosRealizados.size());
    }
}
