/*
Rafael Müller dos Santos Moreira 202465557C
Luan Brandão de Oliveira 202465055A
Marcos José de Oliveira Júnior 202135011
 */

package ufjf.poo.view;

import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;
import ufjf.poo.util.RelatorioUtils;

import javax.swing.*;
import java.awt.*;

public class RelatorioFrame extends JFrame {
    
    private final Usuario usuario;
    private JTextArea areaRelatorio;
    private JScrollPane scrollPane;
    private JPanel painelBotoes;
    
    public RelatorioFrame(Usuario usuario) {
        this.usuario = usuario;
        configurarJanela();
        inicializarInterface();
    }
    
    private void configurarJanela() {
        setTitle("Relatórios - " + usuario.getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void inicializarInterface() {
        setLayout(new BorderLayout());
        
        areaRelatorio = new JTextArea();
        areaRelatorio.setEditable(false);
        areaRelatorio.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaRelatorio.setMargin(new Insets(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(areaRelatorio);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        add(scrollPane, BorderLayout.CENTER);
        
        criarPainelBotoes();
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    private void criarPainelBotoes() {
        painelBotoes = new JPanel(new FlowLayout());
        
        if (usuario instanceof Dono) {
            criarBotoesDono();
        } else if (usuario instanceof Gerente) {
            criarBotoesGerente();
        } else if (usuario instanceof Vendedor) {
            criarBotoesVendedor();
        }
        
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        painelBotoes.add(btnFechar);
    }
    
    private void criarBotoesDono() {
        Dono dono = (Dono) usuario;
        
        JButton btnRelatorioUnidades = new JButton("Relatório de Unidades");
        btnRelatorioUnidades.addActionListener(e -> {
            String relatorio = RelatorioUtils.gerarRelatorioAtividades(dono);
            exibirRelatorio("Relatório Consolidado de Unidades", relatorio);
        });
        painelBotoes.add(btnRelatorioUnidades);
        
        JButton btnRelatorioVendedores = new JButton("Relatório de Vendedores");
        btnRelatorioVendedores.addActionListener(e -> {
            String relatorio = RelatorioUtils.gerarRelatorioVendedores(dono);
            exibirRelatorio("Relatório de Desempenho por Vendedor", relatorio);
        });
        painelBotoes.add(btnRelatorioVendedores);
        
        JButton btnResumoUnidades = new JButton("Resumo de Unidades");
        btnResumoUnidades.addActionListener(e -> {
            String relatorio = RelatorioUtils.gerarResumoUnidades(dono);
            exibirRelatorio("Gerenciamento de Unidades", relatorio);
        });
        painelBotoes.add(btnResumoUnidades);
    }
    
    private void criarBotoesGerente() {
        Gerente gerente = (Gerente) usuario;
        
        JButton btnRelatorioEquipe = new JButton("Relatório da Equipe");
        btnRelatorioEquipe.addActionListener(e -> {
            String relatorio = RelatorioUtils.gerarRelatorioAtividades(gerente);
            exibirRelatorio("Gerenciamento de Equipe", relatorio);
        });
        painelBotoes.add(btnRelatorioEquipe);
        
        JButton btnControlePedidos = new JButton("Controle de Pedidos");
        btnControlePedidos.addActionListener(e -> {
            String relatorio = RelatorioUtils.gerarControlePedidos(gerente);
            exibirRelatorio("Controle de Pedidos da Unidade", relatorio);
        });
        painelBotoes.add(btnControlePedidos);
        
        JButton btnRelatorioDesempenho = new JButton("Relatório de Desempenho");
        btnRelatorioDesempenho.addActionListener(e -> {
            String relatorio = RelatorioUtils.gerarRelatorioDesempenho(gerente);
            exibirRelatorio("Relatório da Unidade", relatorio);
        });
        painelBotoes.add(btnRelatorioDesempenho);
    }
    
    private void criarBotoesVendedor() {
        Vendedor vendedor = (Vendedor) usuario;
        
        JButton btnMeusPedidos = new JButton("Meus Pedidos");
        btnMeusPedidos.addActionListener(e -> {
            String relatorio = RelatorioUtils.gerarRelatorioAtividades(vendedor);
            exibirRelatorio("Histórico de Pedidos Realizados", relatorio);
        });
        painelBotoes.add(btnMeusPedidos);
    }
    
    private void exibirRelatorio(String titulo, String conteudo) {
        StringBuilder relatorioCompleto = new StringBuilder();
        relatorioCompleto.append("=".repeat(60)).append("\n");
        relatorioCompleto.append(titulo.toUpperCase()).append("\n");
        relatorioCompleto.append("=".repeat(60)).append("\n\n");
        relatorioCompleto.append(conteudo);
        relatorioCompleto.append("\n\n");
        relatorioCompleto.append("Relatório gerado em: ").append(new java.util.Date()).append("\n");
        relatorioCompleto.append("=".repeat(60));
        
        areaRelatorio.setText(relatorioCompleto.toString());
        areaRelatorio.setCaretPosition(0); // Volta para o topo
    }
}
