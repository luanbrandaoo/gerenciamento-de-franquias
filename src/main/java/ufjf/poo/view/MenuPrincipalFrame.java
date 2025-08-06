package ufjf.poo.view;

import ufjf.poo.model.Session;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.model.usuario.Vendedor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalFrame extends JFrame {

    private final Session session;
    private final List<Usuario> todosUsuarios;

    public MenuPrincipalFrame(Session session, List<Usuario> todosUsuarios) {
        this.session = session;
        this.todosUsuarios = todosUsuarios;
        
        Usuario usuarioLogado = session.getUsuarioLogado();

        if (usuarioLogado == null) {
            JOptionPane.showMessageDialog(this, "Nenhum usuário logado.", "Erro de Sessão", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }

        setTitle("Menu Principal - Bem-vindo(a), " + usuarioLogado.getNome());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelMenu = new JPanel();
        painelMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JLabel welcomeLabel = new JLabel("Você está logado como: " + usuarioLogado.getClass().getSimpleName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        painelMenu.add(welcomeLabel);

        // Exibe os botões com base no tipo de usuário
        System.out.println("DEBUG: Adicionando botões com base no tipo de usuário.");
        if (usuarioLogado instanceof Dono) {
            adicionarBotoesDono(painelMenu);
        } else if (usuarioLogado instanceof Gerente) {
            adicionarBotoesGerente(painelMenu);
        } else if (usuarioLogado instanceof Vendedor) {
            adicionarBotoesVendedor(painelMenu);
        }

        JButton botaoDeslogar = new JButton("Deslogar");
        botaoDeslogar.addActionListener(e -> deslogar());
        painelMenu.add(botaoDeslogar);

        add(painelMenu, BorderLayout.CENTER);
        
        pack();
        setSize(500, 300);
        setVisible(true);
        System.out.println("DEBUG: Construtor do MenuPrincipalFrame finalizado.");
    }
    
    private void adicionarBotoesDono(JPanel painel) {
        JButton btnRelatorioUnidades = new JButton("Relatório de Unidades");
        btnRelatorioUnidades.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidade de Relatório de Unidades (TODO)");
        });
        painel.add(btnRelatorioUnidades);
        
        JButton btnGerenciarUsuarios = new JButton("Gerenciar Usuários");
        btnGerenciarUsuarios.addActionListener(e -> {
            new GerenciarUsuariosFrame(session, todosUsuarios);
        });
        painel.add(btnGerenciarUsuarios);
    }
    
    private void adicionarBotoesGerente(JPanel painel) {
        JButton btnControlarPedidos = new JButton("Controlar Pedidos");
        btnControlarPedidos.addActionListener(e -> {
            new ControlarPedidosFrame((Gerente) session.getUsuarioLogado());
        });
        painel.add(btnControlarPedidos);
        
        JButton btnGerenciarEstoque = new JButton("Gerenciar Estoque");
        btnGerenciarEstoque.addActionListener(e -> {
            new GerenciarEstoqueFrame((Gerente) session.getUsuarioLogado());
        });
        painel.add(btnGerenciarEstoque);
    }
    
    private void adicionarBotoesVendedor(JPanel painel) {
    JButton btnCadastrarPedidos = new JButton("Cadastrar Pedidos");
    btnCadastrarPedidos.addActionListener(e -> {
        if (session.getUsuarioLogado() instanceof Vendedor) {
            new CadastrarPedidoFrame((Vendedor) session.getUsuarioLogado());
        } else {
             JOptionPane.showMessageDialog(this, "Erro: Usuário logado não é um Vendedor.");
        }
    });
    painel.add(btnCadastrarPedidos);

    JButton btnVisualizarPedidos = new JButton("Visualizar Pedidos");
    btnVisualizarPedidos.addActionListener(e -> {
        if (session.getUsuarioLogado() instanceof Vendedor) {
            new VisualizarPedidosFrame((Vendedor) session.getUsuarioLogado());
        } else {
             JOptionPane.showMessageDialog(this, "Erro: Usuário logado não é um Vendedor.");
        }
    });
    painel.add(btnVisualizarPedidos);
}

    private void deslogar() {
        session.deslogar();
        JOptionPane.showMessageDialog(this, "Sessão encerrada com sucesso.");
        this.dispose();
        new LoginFrame(session, todosUsuarios);
    }
    
    public static void main(String[] args) {
        
    }
}