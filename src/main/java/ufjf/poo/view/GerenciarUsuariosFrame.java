package ufjf.poo.view;

import ufjf.poo.model.Session;
import ufjf.poo.model.usuario.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GerenciarUsuariosFrame extends JFrame {

    private final Session session;
    private final List<Usuario> todosUsuarios;
    private DefaultTableModel modeloTabelaUsuarios;

    public GerenciarUsuariosFrame(Session session, List<Usuario> todosUsuarios) {
        this.session = session;
        this.todosUsuarios = todosUsuarios;

        setTitle("Gerenciar Usuários");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());

        // --- Tabela de Usuários ---
        String[] colunas = {"ID", "Nome", "Email", "Tipo de Usuário"};
        modeloTabelaUsuarios = new DefaultTableModel(colunas, 0);
        JTable tabelaUsuarios = new JTable(modeloTabelaUsuarios);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        preencherTabelaUsuarios();

        // --- Painel de Botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);
        
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
        
        // Garante que a janela seja visível
        setVisible(true);
    }

    private void preencherTabelaUsuarios() {
        modeloTabelaUsuarios.setRowCount(0);
        for (Usuario usuario : todosUsuarios) {
            String tipoUsuario = usuario.getClass().getSimpleName();
            modeloTabelaUsuarios.addRow(new Object[]{
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                tipoUsuario
            });
        }
    }
}