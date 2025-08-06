package ufjf.poo.view;

import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Gerente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

public class GerenciarEstoqueFrame extends JFrame {

    private final Gerente gerente;
    private final Estoque estoque;

    private DefaultTableModel modeloTabelaEstoque;
    private JTable tabelaEstoque;
    private JTextField campoCodigo, campoNome, campoPreco, campoQuantidade;

    public GerenciarEstoqueFrame(Gerente gerente) {
        this.gerente = gerente;
        this.estoque = gerente.getUnidadeFranquia().getEstoque();

        setTitle("Gerenciar Estoque - Gerente: " + gerente.getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // --- Tabela de Estoque ---
        String[] colunas = {"Código", "Nome", "Preço", "Quantidade"};
        modeloTabelaEstoque = new DefaultTableModel(colunas, 0);
        tabelaEstoque = new JTable(modeloTabelaEstoque);
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        preencherTabelaEstoque();
        JPanel painelControles = new JPanel(new GridLayout(2, 1, 10, 10));

        JPanel painelInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelInputs.setBorder(BorderFactory.createTitledBorder("Adicionar/Atualizar Produto"));
        
        campoCodigo = new JTextField(10);
        campoNome = new JTextField(15);
        campoPreco = new JTextField(10);
        campoQuantidade = new JTextField(5);
        
        painelInputs.add(new JLabel("Código:"));
        painelInputs.add(campoCodigo);
        painelInputs.add(new JLabel("Nome:"));
        painelInputs.add(campoNome);
        painelInputs.add(new JLabel("Preço:"));
        painelInputs.add(campoPreco);
        painelInputs.add(new JLabel("Quantidade:"));
        painelInputs.add(campoQuantidade);
        
        JButton btnAdicionar = new JButton("Adicionar Produto");
        btnAdicionar.addActionListener(e -> adicionarProduto());
        
        JButton btnAtualizar = new JButton("Atualizar Quantidade");
        btnAtualizar.addActionListener(e -> atualizarQuantidade());

        painelInputs.add(btnAdicionar);
        painelInputs.add(btnAtualizar);

        JPanel painelBotoesAcao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton btnRemover = new JButton("Remover Selecionado");
        btnRemover.addActionListener(e -> removerProduto());
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());

        painelBotoesAcao.add(btnRemover);
        painelBotoesAcao.add(btnVoltar);
        
        painelControles.add(painelInputs);
        painelControles.add(painelBotoesAcao);
        
        painelPrincipal.add(painelControles, BorderLayout.SOUTH);
        
        add(painelPrincipal);
        setVisible(true);
    }
    
    private void preencherTabelaEstoque() {
        modeloTabelaEstoque.setRowCount(0);
        for (Produto p : estoque.produtosEmEstoque()) {
            int quantidade = estoque.quantidadeProduto(p);
            modeloTabelaEstoque.addRow(new Object[]{p.getCodigo(), p.getNome(), p.getPreco(), quantidade});
        }
    }
    
    private void adicionarProduto() {
        try {
            String codigo = campoCodigo.getText();
            String nome = campoNome.getText();
            String precoStr = campoPreco.getText();
            String quantidadeStr = campoQuantidade.getText();
            
            if (codigo.isEmpty() || nome.isEmpty() || precoStr.isEmpty() || quantidadeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal preco = new BigDecimal(precoStr);
            int quantidade = Integer.parseInt(quantidadeStr);
            
            gerente.administrarEstoque(new Produto(codigo, nome, preco), quantidade, "adicionar");
            
            JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
            limparCampos();
            preencherTabelaEstoque();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço e quantidade devem ser números válidos.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarQuantidade() {
        try {
            int linhaSelecionada = tabelaEstoque.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String quantidadeStr = JOptionPane.showInputDialog(this, "Digite a nova quantidade:");
            if (quantidadeStr != null && !quantidadeStr.isEmpty()) {
                int novaQuantidade = Integer.parseInt(quantidadeStr);
                String codigo = (String) modeloTabelaEstoque.getValueAt(linhaSelecionada, 0);
                Produto produto = estoque.buscarProdutoPorCodigo(codigo);
                
                gerente.administrarEstoque(produto, novaQuantidade, "adicionar");
                
                JOptionPane.showMessageDialog(this, "Quantidade atualizada com sucesso!");
                preencherTabelaEstoque();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A quantidade deve ser um número inteiro.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar quantidade: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerProduto() {
        int linhaSelecionada = tabelaEstoque.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover este produto?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            String codigo = (String) modeloTabelaEstoque.getValueAt(linhaSelecionada, 0);
            Produto produto = estoque.buscarProdutoPorCodigo(codigo);
            
            gerente.administrarEstoque(produto, 0, "remover");
            
            JOptionPane.showMessageDialog(this, "Produto removido com sucesso.");
            preencherTabelaEstoque();
        }
    }

    private void limparCampos() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoPreco.setText("");
        campoQuantidade.setText("");
    }
}