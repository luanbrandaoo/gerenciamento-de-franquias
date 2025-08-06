package ufjf.poo.view;

import ufjf.poo.model.pedido.Pedido;
import ufjf.poo.model.usuario.Vendedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class VisualizarPedidosFrame extends JFrame {

    private final Vendedor vendedor;
    private DefaultTableModel modeloTabelaPedidos;
    private JTable tabelaPedidos;

    public VisualizarPedidosFrame(Vendedor vendedor) {
        this.vendedor = vendedor;

        setTitle("Pedidos Realizados - Vendedor: " + vendedor.getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // --- Tabela de Pedidos ---
        String[] colunas = {"ID Pedido", "Cliente", "Data", "Valor Total"};
        modeloTabelaPedidos = new DefaultTableModel(colunas, 0);
        tabelaPedidos = new JTable(modeloTabelaPedidos);
        JScrollPane scrollPane = new JScrollPane(tabelaPedidos);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        preencherTabelaPedidos();

        // --- Painel de Botões ---
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (Pedido pedido : vendedor.getPedidosRealizados()) {
            modeloTabelaPedidos.addRow(new Object[]{
                pedido.getIdPedido(),
                pedido.getNomeCliente(),
                sdf.format(pedido.getDataPedido()),
                String.format("R$ %.2f", pedido.getValorTotal())
            });
        }
    }
}