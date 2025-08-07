package ufjf.poo.view;

import ufjf.poo.model.Unidade;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.model.usuario.Vendedor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CadastrarUsuarioDialog extends JDialog {
    
    private JTextField campoNome;
    private JTextField campoEmail;
    private JTextField campoSenha;
    private JComboBox<String> campoTipoUsuario;
    private JComboBox<Unidade> campoUnidade;
    private JLabel labelUnidade;
    
    private List<Usuario> todosUsuarios;
    private List<Unidade> todasUnidades;
    private Usuario novoUsuario;
    private boolean salvou = false;
    
    public CadastrarUsuarioDialog(JFrame owner, List<Usuario> todosUsuarios, List<Unidade> todasUnidades) {
        super(owner, "Cadastrar Novo Usuário", true);
        this.todosUsuarios = todosUsuarios;
        this.todasUnidades = todasUnidades;
        
        inicializarInterface();
        configurarDialog();
    }
    
    private void inicializarInterface() {
        setSize(450, 350);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());
        
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoNome = new JTextField(20);
        painelForm.add(campoNome, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        painelForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoEmail = new JTextField(20);
        painelForm.add(campoEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        painelForm.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoSenha = new JTextField(20);
        painelForm.add(campoSenha, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        painelForm.add(new JLabel("Tipo de Usuário:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        String[] tiposUsuario = {"Vendedor", "Gerente", "Dono"};
        campoTipoUsuario = new JComboBox<>(tiposUsuario);
        campoTipoUsuario.addActionListener(e -> atualizarCampoUnidade());
        painelForm.add(campoTipoUsuario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        labelUnidade = new JLabel("Unidade:");
        painelForm.add(labelUnidade, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        campoUnidade = new JComboBox<>();
        preencherComboUnidades();
        painelForm.add(campoUnidade, gbc);
        
        add(painelForm, BorderLayout.CENTER);
        
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnSalvar.addActionListener(e -> salvarUsuario());
        btnCancelar.addActionListener(e -> dispose());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        
        add(painelBotoes, BorderLayout.SOUTH);
        
        atualizarCampoUnidade();
    }
    
    private void preencherComboUnidades() {
        campoUnidade.removeAllItems();
        for (Unidade unidade : todasUnidades) {
            campoUnidade.addItem(unidade);
        }
    }
    
    private void atualizarCampoUnidade() {
        String tipoSelecionado = (String) campoTipoUsuario.getSelectedItem();
        boolean mostrarUnidade = !"Dono".equals(tipoSelecionado);
        
        labelUnidade.setVisible(mostrarUnidade);
        campoUnidade.setVisible(mostrarUnidade);
        
        revalidate();
        repaint();
    }
    
    private void configurarDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
    }
    
    private void salvarUsuario() {
        if (!validarCampos()) {
            return;
        }
        
        try {
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String senha = campoSenha.getText().trim();
            String tipoUsuario = (String) campoTipoUsuario.getSelectedItem();

            // verificar se email já existe
            if (emailJaExiste(email)) {
                JOptionPane.showMessageDialog(this, 
                    "Este email já está cadastrado no sistema.", 
                    "Email Duplicado", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // gerar ID único
            int novoId = gerarNovoId();
            
            // criar usuário baseado no tipo
            switch (tipoUsuario) {
                case "Vendedor":
                    Unidade unidadeVendedor = (Unidade) campoUnidade.getSelectedItem();
                    novoUsuario = new Vendedor(nome, novoId, senha, email, unidadeVendedor.getEstoque());
                    unidadeVendedor.adicionarVendedor((Vendedor) novoUsuario);
                    break;
                    
                case "Gerente":
                    Unidade unidadeGerente = (Unidade) campoUnidade.getSelectedItem();
                    novoUsuario = new Gerente(nome, novoId, senha, email, unidadeGerente);
                    unidadeGerente.trocarGerente((Gerente) novoUsuario);
                    break;
                    
                case "Dono":
                    novoUsuario = new Dono(nome, novoId, senha, email, todasUnidades);
                    break;
            }
            
            todosUsuarios.add(novoUsuario);
            salvou = true;
            
            JOptionPane.showMessageDialog(this, 
                "Usuário cadastrado com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao cadastrar usuário: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validarCampos() {
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String senha = campoSenha.getText().trim();
        String tipoUsuario = (String) campoTipoUsuario.getSelectedItem();
        
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoNome.requestFocus();
            return false;
        }
        
        if (email.isEmpty() || !email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email inválido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoEmail.requestFocus();
            return false;
        }
        
        if (senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A senha é obrigatória.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            campoSenha.requestFocus();
            return false;
        }
        
        if (!"Dono".equals(tipoUsuario) && campoUnidade.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma unidade.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private boolean emailJaExiste(String email) {
        return todosUsuarios.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
    
    private int gerarNovoId() {
        return todosUsuarios.stream()
                .mapToInt(Usuario::getId)
                .max()
                .orElse(0) + 1;
    }
    
    public boolean salvou() {
        return salvou;
    }
    
    public Usuario getNovoUsuario() {
        return novoUsuario;
    }
}
