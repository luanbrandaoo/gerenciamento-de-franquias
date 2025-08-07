package ufjf.poo.view;

import ufjf.poo.model.Session;
import ufjf.poo.model.Unidade;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.dados.DataPersistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GerenciarUsuariosFrame extends JFrame {

    private final Session session;
    private final List<Usuario> todosUsuarios;
    private final List<Unidade> todasUnidades; // Adicionado para aceitar a lista de unidades
    private DefaultTableModel modeloTabelaUsuarios;
    private JTable tabelaUsuarios;

    public GerenciarUsuariosFrame(Session session, List<Usuario> todosUsuarios, List<Unidade> todasUnidades) {
        this.session = session;
        this.todosUsuarios = todosUsuarios;
        this.todasUnidades = todasUnidades;

        setTitle("Gerenciar Usuários");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout());

        String[] colunas = {"ID", "Nome", "Email", "Tipo de Usuário"};
        modeloTabelaUsuarios = new DefaultTableModel(colunas, 0);
        tabelaUsuarios = new JTable(modeloTabelaUsuarios);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        preencherTabelaUsuarios();

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnEditar = new JButton("Editar Usuário");
        btnEditar.addActionListener(e -> editarUsuario());
        painelBotoes.add(btnEditar);

        JButton btnRemover = new JButton("Remover Usuário");
        btnRemover.addActionListener(e -> removerUsuario());
        painelBotoes.add(btnRemover);
        
        JButton btnCadastrar = new JButton("Cadastrar Novo");
        btnCadastrar.addActionListener(e -> cadastrarUsuario());
        painelBotoes.add(btnCadastrar);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);
        
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
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

    private void removerUsuario() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        if (linhaSelecionada != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja remover este usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String emailUsuarioRemover = (String) modeloTabelaUsuarios.getValueAt(linhaSelecionada, 2);
                todosUsuarios.removeIf(u -> u.getEmail().equals(emailUsuarioRemover));
                preencherTabelaUsuarios();
                JOptionPane.showMessageDialog(this, "Usuário removido com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para remover.");
        }
    }
    
    private void editarUsuario() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        if (linhaSelecionada != -1) {
            String emailUsuarioEditar = (String) modeloTabelaUsuarios.getValueAt(linhaSelecionada, 2);
            Usuario usuarioParaEditar = todosUsuarios.stream()
                .filter(u -> u.getEmail().equals(emailUsuarioEditar))
                .findFirst()
                .orElse(null);
            
            if (usuarioParaEditar != null) {
                EditarUsuarioDialog dialog = new EditarUsuarioDialog(this, usuarioParaEditar);
                dialog.setVisible(true);
                
                if(dialog.salvou()) {
                    preencherTabelaUsuarios();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.");
        }
    }

    private void cadastrarUsuario() {
        JOptionPane.showMessageDialog(this, "Funcionalidade de cadastro será implementada aqui.");
    }
}
