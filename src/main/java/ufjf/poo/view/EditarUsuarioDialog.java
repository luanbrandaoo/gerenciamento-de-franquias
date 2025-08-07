package ufjf.poo.view;

import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EditarUsuarioDialog extends JDialog {

    private final Usuario usuarioParaEditar;
    private JTextField campoNome;
    private JTextField campoEmail;
    private JTextField campoSenha;
    private JComboBox<String> campoTipoUsuario;
    private boolean salvou = false;

    public EditarUsuarioDialog(JFrame owner, Usuario usuario) {
        super(owner, "Editar Usuário", true);
        this.usuarioParaEditar = usuario;

        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel painelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painelForm.add(new JLabel("Nome:"));
        campoNome = new JTextField(usuario.getNome());
        painelForm.add(campoNome);

        painelForm.add(new JLabel("Email:"));
        campoEmail = new JTextField(usuario.getEmail());
        painelForm.add(campoEmail);

        painelForm.add(new JLabel("Senha:"));
        campoSenha = new JTextField(usuario.getPrivateKey());
        painelForm.add(campoSenha);

        // Adiciona um campo para mudar o tipo de usuário
        painelForm.add(new JLabel("Tipo de Usuário:"));
        String[] tipos = {"Dono", "Gerente", "Vendedor"};
        campoTipoUsuario = new JComboBox<>(tipos);
        campoTipoUsuario.setSelectedItem(usuario.getClass().getSimpleName());
        painelForm.add(campoTipoUsuario);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarEdicao());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        add(painelForm, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    public boolean salvou() {
        return salvou;
    }
    
    public Usuario getUsuarioAtualizado() {
        return usuarioParaEditar;
    }
    
    private void salvarEdicao() {
        String novoNome = campoNome.getText();
        String novoEmail = campoEmail.getText();
        String novaSenha = campoSenha.getText();
        String novoTipo = (String) campoTipoUsuario.getSelectedItem();

        // Validação básica dos campos
        if (novoNome.isEmpty() || novoEmail.isEmpty() || novaSenha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuarioParaEditar.setNome(novoNome);
        usuarioParaEditar.setEmail(novoEmail);
        usuarioParaEditar.setPrivateKey(novaSenha);
        
        if (!usuarioParaEditar.getClass().getSimpleName().equals(novoTipo)) {
            JOptionPane.showMessageDialog(this, "O tipo de usuário não pode ser alterado diretamente por aqui. Apenas os dados foram salvos.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        
        JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
        salvou = true;
        dispose();
    }
}