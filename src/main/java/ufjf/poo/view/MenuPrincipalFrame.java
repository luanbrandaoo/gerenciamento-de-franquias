/*
Rafael Müller dos Santos Moreira 202465557C
Luan Brandão de Oliveira 202465055A
Marcos José de Oliveira Júnior 202135011
 */

package ufjf.poo.view;

import ufjf.poo.model.Session;
import ufjf.poo.model.Unidade;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.controller.DataPersistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuPrincipalFrame extends JFrame {
    
    private final Session session;
    private final List<Usuario> todosUsuarios;
    private final List<Unidade> todasUnidades;

    public MenuPrincipalFrame(Session session, List<Usuario> todosUsuarios, List<Unidade> todasUnidades) {
        this.session = session;
        this.todosUsuarios = todosUsuarios;
        this.todasUnidades = todasUnidades;
        
        configurarJanela();
        configurarFechamento();
        inicializarInterface();
    }
   /* 
    private void inicializaListaVendedores() {
    
    
        Usuario u = Session.getUsuarioLogado;
        if(
    
    
        List<Vendedor> vendedores;
        for (Usuario usuario : todosUsuarios) {
            String tipoUsuario = usuario.getClass().getSimpleName();
    
            if(tpoUsuario == Vendedor)
            modeloTabelaUsuarios.addRow(new Object[]{
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                tipoUsuario
            });
        }
    }
*/
    private void configurarJanela() {
        setTitle("Menu Principal - " + session.getUsuarioLogado().getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void configurarFechamento() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSaida();
            }
        });
    }

    private void confirmarSaida() {
        int resposta = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair do sistema?",
            "Confirmação de Saída",
            JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {
            salvarDadosEVoltarParaLogin();
        }
    }

    private void salvarDadosEVoltarParaLogin() {
        JDialog savingDialog = criarDialogoSalvamento();
        
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                DataPersistence.saveAllData(todosUsuarios, todasUnidades, null);
                return null;
            }
            
            @Override
            protected void done() {
                savingDialog.dispose();
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new LoginFrame(new Session(), todosUsuarios, todasUnidades).setVisible(true);
                });
            }
        }.execute();
        
        savingDialog.setVisible(true);
    }

    private JDialog criarDialogoSalvamento() {
        JDialog dialog = new JDialog(this, "Salvando dados...", true);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Salvando dados, por favor aguarde..."), BorderLayout.CENTER);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, BorderLayout.SOUTH);
        
        dialog.add(panel);
        return dialog;
    }

    private void inicializarInterface() {
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        adicionarTitulo(painelPrincipal);
        painelPrincipal.add(Box.createVerticalStrut(30));
        adicionarBotoesMenu(painelPrincipal);

        add(painelPrincipal);
    }

    private void adicionarTitulo(JPanel painel) {
        String tipoUsuario = session.getUsuarioLogado().getClass().getSimpleName();
        JLabel titulo = new JLabel("Bem-vindo(a), " + session.getUsuarioLogado().getNome() + " (" + tipoUsuario + ")");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        painel.add(titulo);
    }

    private void adicionarBotoesMenu(JPanel painel) {
        Usuario usuario = session.getUsuarioLogado();
        
        if (usuario instanceof Dono) {
            adicionarBotaoDono(painel);
        } else if (usuario instanceof Gerente) {
            adicionarBotaoGerente(painel, (Gerente) usuario);
        } else if (usuario instanceof Vendedor) {
            adicionarBotaoVendedor(painel, (Vendedor) usuario);
        }
        
        adicionarBotaoSair(painel);
    }

    private void adicionarBotaoDono(JPanel painel) {
        JButton btnGerenciarUsuarios = criarBotao(
            "Gerenciar Usuários",
            e -> new GerenciarUsuariosFrame(session, todosUsuarios, todasUnidades).setVisible(true)
        );
        painel.add(btnGerenciarUsuarios);
        painel.add(Box.createVerticalStrut(15));
    }
    
    // método para adicionar os botões do Gerente
    private void adicionarBotaoGerente(JPanel painel, Gerente gerente) {
        
        Unidade unidadeEspecifica =  gerente.getUnidadeFranquia();
                        
        JButton btnGerenciarEquipe = criarBotao(
            "Gerenciar Equipe de Vendas",
            e -> new GerenciarUsuariosFrame(session, gerente).setVisible(true)
        );
        painel.add(btnGerenciarEquipe);
        painel.add(Box.createVerticalStrut(10));
        
        JButton btnGerenciarEstoque = criarBotao(
            "Gerenciar Estoque",
            e -> new GerenciarEstoqueFrame(gerente, todasUnidades).setVisible(true)
        );
        painel.add(btnGerenciarEstoque);
        painel.add(Box.createVerticalStrut(10));
        
    }

    private void adicionarBotaoVendedor(JPanel painel, Vendedor vendedor) {
        JButton btnNovoPedido = criarBotao(
            "Cadastrar Novo Pedido",
            e -> new CadastrarPedidoFrame(vendedor).setVisible(true)
        );
        painel.add(btnNovoPedido);
        painel.add(Box.createVerticalStrut(10));
        
        JButton btnVisualizarPedidos = criarBotao(
            "Visualizar Meus Pedidos",
            e -> new VisualizarPedidosFrame(vendedor).setVisible(true)
        );
        painel.add(btnVisualizarPedidos);
        painel.add(Box.createVerticalStrut(15));
    }

    private void adicionarBotaoSair(JPanel painel) {
        JButton btnSair = criarBotao("Sair do Sistema", e -> confirmarSaida());
        painel.add(btnSair);
    }

    private JButton criarBotao(String texto, ActionListener acao) {
        JButton botao = new JButton(texto);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(200, 30));
        botao.addActionListener(acao);
        return botao;
    }
}