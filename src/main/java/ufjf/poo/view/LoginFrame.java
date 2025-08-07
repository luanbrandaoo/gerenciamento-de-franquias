package ufjf.poo.view;

import ufjf.poo.model.Session;
import ufjf.poo.model.Unidade;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.exception.ChavePrivadaInvalidaException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginFrame extends JFrame {

    private final Session session;
    private final List<Usuario> usuariosCadastrados;
    private final List<Unidade> unidadesCadastradas;
    private JTextField campoEmail;
    private JPasswordField campoChave;

    public LoginFrame(Session session, List<Usuario> usuarios, List<Unidade> unidades) {
        this.session = session;
        this.usuariosCadastrados = usuarios;
        this.unidadesCadastradas = unidades;

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

        JDialog loadingDialog = new JDialog(this, "Aguarde", true);
        JLabel loadingLabel = new JLabel("Fazendo login...", SwingConstants.CENTER);
        loadingDialog.add(loadingLabel);
        loadingDialog.setSize(200, 100);
        loadingDialog.setLocationRelativeTo(this);

        new SwingWorker<Usuario, Void>() {
            @Override
            protected Usuario doInBackground() throws Exception {
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
                    
                    // Exibe um novo JDialog para o carregamento do menu principal
                    JDialog menuLoadingDialog = new JDialog(LoginFrame.this, "Aguarde", true);
                    JLabel menuLoadingLabel = new JLabel("Carregando menu principal...", SwingConstants.CENTER);
                    menuLoadingDialog.add(menuLoadingLabel);
                    menuLoadingDialog.setSize(250, 100);
                    menuLoadingDialog.setLocationRelativeTo(LoginFrame.this);
                    
                    // Inicia um novo SwingWorker para construir a MenuPrincipalFrame em segundo plano
                    new SwingWorker<MenuPrincipalFrame, Void>() {
                        @Override
                        protected MenuPrincipalFrame doInBackground() throws Exception {
                            return new MenuPrincipalFrame(session, usuariosCadastrados, unidadesCadastradas);
                        }

                        @Override
                        protected void done() {
                            menuLoadingDialog.dispose();
                            try {
                                MenuPrincipalFrame menuPrincipal = get();
                                menuPrincipal.setVisible(true);
                                LoginFrame.this.dispose();
                            } catch (Exception e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(LoginFrame.this, "Erro ao carregar o menu principal: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }.execute();

                    menuLoadingDialog.setVisible(true);

                } catch (Exception ex) {
                    Throwable cause = ex.getCause();
                    String mensagemErro = (cause != null) ? cause.getMessage() : ex.getMessage();
                    
                    if (cause instanceof ChavePrivadaInvalidaException) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Chave de acesso incorreta.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Erro de login: " + mensagemErro, "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }.execute();
        
        loadingDialog.setVisible(true);
    }
}