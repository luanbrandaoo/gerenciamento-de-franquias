package ufjf.poo.view;

import ufjf.poo.model.Session;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.exception.ChavePrivadaInvalidaException;
import ufjf.poo.exception.IdUsuarioInvalidoException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginFrame extends JFrame {

    private final Session session;
    private final List<Usuario> usuariosCadastrados;
    private JTextField campoEmail;
    private JPasswordField campoChave;

    public LoginFrame(Session session, List<Usuario> usuarios) {
        this.session = session;
        this.usuariosCadastrados = usuarios;

        setTitle("Sistema de Franquias - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new GridLayout(3, 2, 10, 10));

        painelPrincipal.add(new JLabel("E-mail:"));
        campoEmail = new JTextField();
        painelPrincipal.add(campoEmail);

        painelPrincipal.add(new JLabel("Chave de Acesso:"));
        campoChave = new JPasswordField();
        painelPrincipal.add(campoChave);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> login());
        painelPrincipal.add(btnLogin);

        add(painelPrincipal);
        setVisible(true);
    }

    private void login() {
        String email = campoEmail.getText();
        String chave = new String(campoChave.getPassword());

        // Mostra um indicador de progresso para o usuário
        JDialog loadingDialog = new JDialog(this, "Aguarde", true);
        JLabel loadingLabel = new JLabel("Fazendo login...");
        loadingDialog.add(loadingLabel);
        loadingDialog.setSize(200, 100);
        loadingDialog.setLocationRelativeTo(this);

        new SwingWorker<Usuario, Void>() {
            Exception loginException = null;

            @Override
            protected Usuario doInBackground() throws Exception {
                // Este código roda em uma thread separada
                Usuario usuario = null;
                for (Usuario u : usuariosCadastrados) {
                    if (u.getEmail().equalsIgnoreCase(email)) {
                        usuario = u;
                        break;
                    }
                }

                if (usuario == null) {
                    throw new Exception("Usuário não encontrado.");
                }

                session.login(usuario, chave);
                return usuario;
            }

            @Override
            protected void done() {
                loadingDialog.dispose(); 
                try {
                    get(); 
                    JOptionPane.showMessageDialog(LoginFrame.this, "Login bem sucedido!");

                    new MenuPrincipalFrame(session, usuariosCadastrados);
                    LoginFrame.this.dispose();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Erro de login: " + ex.getCause().getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }.execute();

        // Mostra o dialog enquanto a tarefa do SwingWorker executa
        loadingDialog.setVisible(true);
    }
}
