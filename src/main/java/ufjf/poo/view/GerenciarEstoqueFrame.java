package ufjf.poo.view;

import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.Unidade;
import ufjf.poo.dados.DataPersistence;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.List;

public class GerenciarEstoqueFrame extends JFrame {

    private final Estoque estoque;
    private final Gerente gerente;
    private final List<Unidade> todasUnidades; 
    private final List<Produto> todosProdutos; 

    private DefaultTableModel modeloTabelaEstoque;
    private JTable tabelaEstoque;
    private JTextField campoCodigo, campoNome, campoPreco, campoQuantidade;

    
    public GerenciarEstoqueFrame(Gerente gerente, List<Unidade> todasUnidades) {
        this.gerente = gerente;
        this.estoque = gerente.getUnidadeFranquia().getEstoque();
        this.todasUnidades = todasUnidades;
        
        // Pega todos os produtos do estoque das unidades para a persistência, se necessário.
        this.todosProdutos = todasUnidades.stream()
            .flatMap(unidade -> unidade.getEstoque().produtosEmEstoque().stream())
            .distinct()
            .toList();

        // Configurações da janela
        setTitle("Gerenciar Estoque - Gerente: " + gerente.getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // listener para salvar os dados ao fechar a janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Salva todos os dados para persistência
                DataPersistence.saveAllData(null, todasUnidades, null);
            }
        });

        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // Cria e configura a tabela de estoque
        painelPrincipal.add(criarPainelTabela(), BorderLayout.CENTER);

        // Cria e configura o painel de controles e botões
        painelPrincipal.add(criarPainelControles(), BorderLayout.SOUTH);

        add(painelPrincipal);
        setVisible(true);
    }

    private JScrollPane criarPainelTabela() {
        String[] colunas = {"Código", "Nome", "Preço", "Quantidade"};
        modeloTabelaEstoque = new DefaultTableModel(colunas, 0);
        tabelaEstoque = new JTable(modeloTabelaEstoque);
        preencherTabelaEstoque(); // Preenche a tabela com os dados atuais
        return new JScrollPane(tabelaEstoque);
    }
    
    private JPanel criarPainelControles() {
        JPanel painelControles = new JPanel(new BorderLayout());

        // Painel para os campos de texto
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

        // Painel para os botões de ação
        JPanel painelBotoesAcao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton btnAdicionar = new JButton("Adicionar Produto");
        JButton btnAtualizar = new JButton("Atualizar Quantidade");
        JButton btnRemover = new JButton("Remover Selecionado");
        JButton btnVoltar = new JButton("Voltar");

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnAtualizar.addActionListener(e -> atualizarQuantidade());
        btnRemover.addActionListener(e -> removerProduto());
        btnVoltar.addActionListener(e -> dispose());
        
        painelInputs.add(btnAdicionar);
        painelInputs.add(btnAtualizar);
        
        painelBotoesAcao.add(btnRemover);
        painelBotoesAcao.add(btnVoltar);
        
        painelControles.add(painelInputs, BorderLayout.NORTH);
        painelControles.add(painelBotoesAcao, BorderLayout.SOUTH);

        return painelControles;
    }

    private void preencherTabelaEstoque() {
        modeloTabelaEstoque.setRowCount(0); // Limpa a tabela antes de preencher
        for (Produto p : estoque.produtosEmEstoque()) {
            int quantidade = estoque.quantidadeProduto(p);
            modeloTabelaEstoque.addRow(new Object[]{p.getCodigo(), p.getNome(), p.getPreco(), quantidade});
        }
    }
    
     //Adiciona um novo produto ao estoque ou atualiza um existente.
    private void adicionarProduto() {
        try {
            String codigo = campoCodigo.getText().trim();
            String nome = campoNome.getText().trim();
            String precoStr = campoPreco.getText().trim();
            String quantidadeStr = campoQuantidade.getText().trim();

            if (codigo.isEmpty() || nome.isEmpty() || precoStr.isEmpty() || quantidadeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal preco = new BigDecimal(precoStr);
            int quantidade = Integer.parseInt(quantidadeStr);
            
            // O método administrarEstoque do Gerente pode ser usado para adicionar ou atualizar
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

    /**
     * Atualiza a quantidade de um produto selecionado na tabela.
     */
    private void atualizarQuantidade() {
        try {
            int linhaSelecionada = tabelaEstoque.getSelectedRow();
            if (linhaSelecionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para atualizar a quantidade.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String quantidadeStr = JOptionPane.showInputDialog(this, "Digite a nova quantidade:");
            if (quantidadeStr != null && !quantidadeStr.trim().isEmpty()) {
                int novaQuantidade = Integer.parseInt(quantidadeStr.trim());
                String codigo = (String) modeloTabelaEstoque.getValueAt(linhaSelecionada, 0);
                Produto produto = estoque.buscarProdutoPorCodigo(codigo);
                
                gerente.administrarEstoque(produto, novaQuantidade, "atualizar");
                
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