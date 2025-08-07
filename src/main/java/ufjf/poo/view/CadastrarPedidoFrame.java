package ufjf.poo.view;

import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.pedido.ItemPedido;
import ufjf.poo.model.usuario.Vendedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CadastrarPedidoFrame extends JFrame {

    private final Vendedor vendedor;
    private final Estoque estoque;

    private final DefaultTableModel modeloTabelaProdutos;
    private final JTable tabelaProdutos;
    private final JTextField campoNomeCliente;
    private final JTextField campoFormaPagamento;
    private final JTextField campoFormaEntrega;

    public CadastrarPedidoFrame(Vendedor vendedor) {
        this.vendedor = vendedor;
        this.estoque = vendedor.getEstoqueDisponivel();

        setTitle("Cadastrar Novo Pedido - Vendedor: " + vendedor.getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());

        JPanel painelDadosCliente = new JPanel(new GridLayout(3, 2, 5, 5));
        campoNomeCliente = new JTextField(20);
        campoFormaPagamento = new JTextField(20);
        campoFormaEntrega = new JTextField(20);

        painelDadosCliente.setBorder(BorderFactory.createTitledBorder("Dados do Pedido"));
        painelDadosCliente.add(new JLabel("Nome do Cliente:"));
        painelDadosCliente.add(campoNomeCliente);
        painelDadosCliente.add(new JLabel("Forma de Pagamento:"));
        painelDadosCliente.add(campoFormaPagamento);
        painelDadosCliente.add(new JLabel("Forma de Entrega:"));
        painelDadosCliente.add(campoFormaEntrega);

        String[] colunas = {"Nome", "Código", "Preço", "Disponível", "Quant. Pedida"};
        modeloTabelaProdutos = new DefaultTableModel(colunas, 0);
        tabelaProdutos = new JTable(modeloTabelaProdutos);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        preencherTabelaProdutos();

        JButton botaoFinalizar = new JButton("Finalizar Pedido");
        botaoFinalizar.addActionListener(e -> finalizarPedido());

        // Adiciona o botão de voltar
        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> dispose());

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(botaoVoltar); // Adiciona o botão de voltar
        painelBotoes.add(botaoFinalizar);

        painelPrincipal.add(painelDadosCliente, BorderLayout.NORTH);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
        setVisible(true);
    }

    private void preencherTabelaProdutos() {
        modeloTabelaProdutos.setRowCount(0);
        for (Produto p : estoque.produtosEmEstoque()) {
            int quantidadeDisponivel = estoque.quantidadeProduto(p);
            modeloTabelaProdutos.addRow(new Object[]{
                p.getCodigo(),
                p.getNome(),
                p.getPreco(),
                quantidadeDisponivel,
                0
            });
        }
    }

    private void finalizarPedido() {
        try {
            String nomeCliente = campoNomeCliente.getText();
            String formaPagamento = campoFormaPagamento.getText();
            String formaEntrega = campoFormaEntrega.getText();

            if (nomeCliente.isEmpty() || formaPagamento.isEmpty() || formaEntrega.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos do pedido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<ItemPedido> itensDoPedido = new ArrayList<>();
            for (int i = 0; i < modeloTabelaProdutos.getRowCount(); i++) {
                int quantidade = Integer.parseInt(modeloTabelaProdutos.getValueAt(i, 4).toString());
                if (quantidade > 0) {
                    String codigoProduto = (String) modeloTabelaProdutos.getValueAt(i, 0);
                    Produto produto = estoque.buscarProdutoPorCodigo(codigoProduto);

                    if (estoque.quantidadeProduto(produto) < quantidade) {
                        JOptionPane.showMessageDialog(this, "Quantidade de " + produto.getNome() + " indisponível em estoque.", "Erro de Estoque", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
                    itensDoPedido.add(new ItemPedido(produto, quantidade, subtotal));
                }
            }

            if (itensDoPedido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum item foi adicionado ao pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            vendedor.cadastrarPedidos(nomeCliente, formaPagamento, formaEntrega, itensDoPedido);
            JOptionPane.showMessageDialog(this, "Pedido para " + nomeCliente + " cadastrado com sucesso!");

            this.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "A quantidade pedida deve ser um número válido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar pedido: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}