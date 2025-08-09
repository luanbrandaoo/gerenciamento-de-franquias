package ufjf.poo.view;

import ufjf.poo.model.Session;
import ufjf.poo.model.Unidade;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.model.usuario.Vendedor;
import ufjf.poo.controller.DataPersistence;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciarUsuariosFrame extends JFrame {

    private final Session session;
    private List<Usuario> todosUsuarios = new ArrayList<>();
    private List<Vendedor> vendedores = new ArrayList<>();
    private  List<Usuario> todosUsuariosSistema; // Lista completa de usuários do sistema
    private final List<Unidade> todasUnidadesSistema; // Lista completa de unidades do sistema
    private  DefaultTableModel modeloTabelaUsuarios;
    private  JTable tabelaUsuarios;
    private  List<Usuario> usuariosExibidosNaTabela; // Lista de usuários que será exibida na tabela

    // Construtor para o Dono (que vê todos os usuários)
    public GerenciarUsuariosFrame(Session session, List<Usuario> todosUsuarios, List<Unidade> todasUnidades) {
        this.session = session;
        this.todosUsuariosSistema = todosUsuarios;
        this.todasUnidadesSistema = todasUnidades;
        this.usuariosExibidosNaTabela = todosUsuarios; // Dono visualiza todos os usuários

        configurarJanela();
        inicializarComponentes();
    }

    public GerenciarUsuariosFrame(Session session, Gerente gerente) {
        
        Unidade unidadeEspecifica = gerente.getUnidadeFranquia();
                
        this.session = session;
        this.todosUsuariosSistema = null; 
        this.todasUnidadesSistema = null;
        this.usuariosExibidosNaTabela = new ArrayList<>(gerente.getUnidadeFranquia().getVendedores());
       
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Gerenciar Usuários");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Adiciona listener para salvar os dados ao fechar a janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DataPersistence.saveAllData(todosUsuariosSistema, todasUnidadesSistema, null);
            }
        });
    }

    private void inicializarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());

        String[] colunas = {"ID", "Nome", "Email", "Tipo de Usuário"};
        modeloTabelaUsuarios = new DefaultTableModel(colunas, 0);
        tabelaUsuarios = new JTable(modeloTabelaUsuarios);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        preencherTabelaUsuarios(); // Preenche a tabela com a lista correta (todos ou filtrada)

        JPanel painelBotoes = criarPainelBotoes();
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
        setVisible(true);
    }

    private JPanel criarPainelBotoes() {
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
        return painelBotoes;
    }

    private void preencherTabelaUsuarios() {
        modeloTabelaUsuarios.setRowCount(0);
        for (Usuario usuario : usuariosExibidosNaTabela) { // Usa a lista filtrada/completa
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
        if (linhaSelecionada!= -1) {
            String emailUsuarioRemover = (String) modeloTabelaUsuarios.getValueAt(linhaSelecionada, 2);
            Usuario usuarioRemover = todosUsuariosSistema.stream() // Busca na lista completa para remover
                   .filter(u -> u.getEmail().equals(emailUsuarioRemover))
                   .findFirst().orElse(null);
            
            // Validação de segurança: Dono não pode ser removido
            if (usuarioRemover instanceof Dono) {
                JOptionPane.showMessageDialog(this, "Você não tem permissão para remover um Dono do sistema.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja remover este usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                todosUsuariosSistema.remove(usuarioRemover); // Remove da lista completa
                preencherTabelaUsuarios(); // Atualiza a tabela com a lista correta
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
            // CORREÇÃO: A chamada do construtor agora tem apenas 2 argumentos
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
        // Passa a lista completa de usuários e unidades para o diálogo de cadastro
        CadastrarUsuarioDialog dialog = new CadastrarUsuarioDialog(this, todosUsuariosSistema, todasUnidadesSistema);
        dialog.setVisible(true);

        if (dialog.salvou()) {
            preencherTabelaUsuarios(); // Atualiza a tabela
            JOptionPane.showMessageDialog(this,
                    "Usuário '" + dialog.getNovoUsuario().getNome() + "' cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}