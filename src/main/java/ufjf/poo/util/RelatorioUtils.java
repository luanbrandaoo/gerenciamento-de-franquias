/*
Rafael Müller dos Santos Moreira 202465557C
Luan Brandão de Oliveira 202465055A
Marcos José de Oliveira Júnior 202135011
 */

package ufjf.poo.util;

import ufjf.poo.model.Unidade;
import ufjf.poo.model.pedido.ItemPedido;
import ufjf.poo.model.pedido.Pedido;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class RelatorioUtils {
    public static String gerarRelatorioAtividades(Dono dono) {
        StringBuilder relatorio = new StringBuilder();
        
        List<Unidade> unidades = dono.getUnidadesGerenciadas();
        
        if (unidades.isEmpty()) {
            relatorio.append("Nenhuma unidade para gerar relatório.");
            return relatorio.toString();
        }
        
        for (Unidade u : unidades) {
            relatorio.append("Relatorio da Unidade: ").append(u.getNome()).append("\n");
            relatorio.append(gerarRelatorioVendasUnidade(u));
            relatorio.append("\n");
        }
        
        return relatorio.toString();
    }

    public static String gerarRelatorioVendedores(Dono dono) {
        StringBuilder relatorio = new StringBuilder();
        
        List<Unidade> unidades = dono.getUnidadesGerenciadas();
        
        if (unidades.isEmpty()) {
            relatorio.append("Nenhuma unidade para gerar relatório.");
            return relatorio.toString();
        }
        
        for (Unidade u : unidades) {
            for (Vendedor v : u.getVendedores()) {
                BigDecimal totalVendas = BigDecimal.ZERO;
                for (Pedido p : v.getPedidosRealizados()) {
                    totalVendas = totalVendas.add(p.getValorTotal());
                }
                
                relatorio.append("Vendedor: ").append(v.getNome())
                         .append(" | Unidade: ").append(u.getNome())
                         .append(" | Total de vendas: R$").append(String.format("%.2f", totalVendas))
                         .append("\n");
            }
        }
        
        return relatorio.toString();
    }

    public static String gerarResumoUnidades(Dono dono) {
        StringBuilder relatorio = new StringBuilder();
        
        List<Unidade> unidades = dono.getUnidadesGerenciadas();
        
        if (unidades.isEmpty()) {
            relatorio.append("Nenhuma unidade cadastrada!");
            return relatorio.toString();
        }
        
        relatorio.append("Unidades gerenciadas:\n");
        for (Unidade u : unidades) {
            relatorio.append("ID: ").append(u.getId())
                     .append(" | Nome: ").append(u.getNome())
                     .append(" | Endereço: ").append(u.getEndereco()).append("\n");
            
            relatorio.append("Gerentes Subordinados:\n");
            if (u.getGerente() != null) {
                relatorio.append("  ID: ").append(u.getGerente().getId())
                         .append(" | Nome: ").append(u.getGerente().getNome())
                         .append(" | Unidade: ").append(u.getNome()).append("\n");
            } else {
                relatorio.append("  Não há gerentes subordinados para a unidade: ").append(u.getNome()).append("\n");
            }
            relatorio.append("\n");
        }
        
        return relatorio.toString();
    }

    public static String gerarRelatorioAtividades(Gerente gerente) {
        StringBuilder relatorio = new StringBuilder();
        
        List<Vendedor> vendedores = gerente.getUnidadeFranquia().getVendedores();
        
        if (vendedores.isEmpty()) {
            relatorio.append("A unidade não possui vendedores.");
            return relatorio.toString();
        }
        
        relatorio.append("Vendedores da unidade ").append(gerente.getUnidadeFranquia().getNome()).append(":\n\n");
        
        for (Vendedor v : vendedores) {
            relatorio.append("- ID: ").append(v.getId())
                     .append(" | Nome: ").append(v.getNome())
                     .append(" | E-mail: ").append(v.getEmail()).append("\n");
            relatorio.append("  Pedidos realizados: ").append(v.getPedidosRealizados().size()).append("\n\n");
        }
        
        return relatorio.toString();
    }

    public static String gerarControlePedidos(Gerente gerente) {
        StringBuilder relatorio = new StringBuilder();
        
        List<Vendedor> vendedores = gerente.getUnidadeFranquia().getVendedores();
        
        if (vendedores.isEmpty()) {
            relatorio.append("Nenhum vendedor cadastrado!");
            return relatorio.toString();
        }
        
        for (Vendedor v : vendedores) {
            relatorio.append("Pedidos do Vendedor: ").append(v.getNome()).append("\n");
            relatorio.append(gerarRelatorioAtividades(v));
            relatorio.append("\n");
        }
        
        return relatorio.toString();
    }

    public static String gerarRelatorioDesempenho(Gerente gerente) {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append(gerarRelatorioVendasUnidade(gerente.getUnidadeFranquia()));
        return relatorio.toString();
    }

    public static String gerarRelatorioAtividades(Vendedor vendedor) {
        StringBuilder relatorio = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        if (vendedor.getPedidosRealizados().isEmpty()) {
            relatorio.append("Sem pedidos feitos!");
            return relatorio.toString();
        }
        
        for (Pedido pedido : vendedor.getPedidosRealizados()) {
            relatorio.append("ID do pedido: ").append(pedido.getIdPedido()).append("\n");
            relatorio.append("Cliente: ").append(pedido.getNomeCliente()).append("\n");
            relatorio.append("Data: ").append(sdf.format(pedido.getDataPedido())).append("\n");
            relatorio.append("Vendedor: ").append(pedido.getVendedor()).append("\n");
            relatorio.append("Status: ").append(pedido.getStatus()).append("\n");
            relatorio.append("Forma de Pagamento: ").append(pedido.getFormaDePagamento()).append("\n");
            relatorio.append("Forma de Entrega: ").append(pedido.getFormaDeEntrega()).append("\n");
            relatorio.append("Valor Total: R$").append(String.format("%.2f", pedido.getValorTotal())).append("\n");
            
            relatorio.append("Itens do Pedido:\n");
            for (ItemPedido item : pedido.getItens()) {
                relatorio.append("  - ").append(item.produto().getNome())
                         .append(" - Qtd: ").append(item.quantidade())
                         .append(" | Subtotal: R$").append(String.format("%.2f", item.subtotal())).append("\n");
            }
            relatorio.append("\n");
        }
        
        relatorio.append("Total de pedidos no histórico: ").append(vendedor.getPedidosRealizados().size());
        
        return relatorio.toString();
    }

    private static String gerarRelatorioVendasUnidade(Unidade unidade) {
        StringBuilder relatorio = new StringBuilder();
        
        if (unidade.getVendedores().isEmpty()) {
            relatorio.append("Não há vendedores!\n");
            return relatorio.toString();
        }
        
        BigDecimal totalVendasUnidade = BigDecimal.ZERO;
        int totalPedidosUnidade = 0;
        
        relatorio.append("Relatório de vendas por vendedor:\n");
        
        for (Vendedor vendedorAnalisado : unidade.getVendedores()) {
            BigDecimal totalVendasVendedor = BigDecimal.ZERO;
            
            for (Pedido pedidoAnalisado : vendedorAnalisado.getPedidosRealizados()) {
                totalVendasVendedor = totalVendasVendedor.add(pedidoAnalisado.getValorTotal());
            }
            
            relatorio.append("- Vendedor: ").append(vendedorAnalisado.getNome())
                     .append(" | ").append(vendedorAnalisado.getPedidosRealizados().size())
                     .append(" pedidos | Total: R$").append(String.format("%.2f", totalVendasVendedor)).append("\n");
            
            totalVendasUnidade = totalVendasUnidade.add(totalVendasVendedor);
            totalPedidosUnidade += vendedorAnalisado.getPedidosRealizados().size();
        }
        
        relatorio.append("\nResumo da Unidade:\n");
        relatorio.append("Total de Pedidos: ").append(totalPedidosUnidade).append("\n");
        relatorio.append("Total de Vendas: R$").append(String.format("%.2f", totalVendasUnidade)).append("\n");
        
        return relatorio.toString();
    }
}
