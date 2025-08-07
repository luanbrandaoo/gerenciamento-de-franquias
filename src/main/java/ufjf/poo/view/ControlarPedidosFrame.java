package ufjf.poo.view;

import ufjf.poo.model.pedido.Pedido;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ControlarPedidosFrame extends JFrame {

    private final Gerente gerente;
    private final DefaultTableModel modeloTabelaPedidos;
    private final JTable tabelaPedidos;

    public ControlarPedidosFrame(Gerente gerente) {
        this.gerente = gerente;
        
        setTitle("Controle de Pedidos - Unidade " + gerente.getUnidadeFranquia().getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // --- Tabela de Pedidos ---
        String[] colunas = {"ID Pedido", "Cliente", "Vendedor", "Data", "Valor Total"};
        modeloTabelaPedidos = new DefaultTableModel(colunas, 0);
        tabelaPedidos = new JTable(modeloTabelaPedidos);
        JScrollPane scrollPane = new JScrollPane(tabelaPedidos);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        preencherTabelaPedidos();

        // --- Painel de Botoes ---
        JPanel painelBotoes = new JPanel();
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);
        
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);
        
        add(painelPrincipal);
        setVisible(true);
    }
    
    private void preencherTabelaPedidos() {
        modeloTabelaPedidos.setRowCount(0);
        
        List<Vendedor> vendedores = gerente.getUnidadeFranquia().getVendedores();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Vendedor vendedor : vendedores) {
            for (Pedido pedido : vendedor.getPedidosRealizados()) {
                modeloTabelaPedidos.addRow(new Object[]{
                    pedido.getIdPedido(),
                    pedido.getNomeCliente(),
                    vendedor.getNome(),
                    sdf.format(pedido.getDataPedido()),
                    String.format("R$ %.2f", pedido.getValorTotal())
                });
            }
        }
    }
}