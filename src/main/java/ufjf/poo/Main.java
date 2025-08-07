package ufjf.poo;

import ufjf.poo.dados.DataModel;
import ufjf.poo.dados.DataPersistence;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.pedido.Pedido;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.model.usuario.Vendedor;
import ufjf.poo.model.Unidade;
import ufjf.poo.model.Session;
import ufjf.poo.view.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final String DADOS_INICIALIZADOS = "dados.inicializados";
    private static final ExecutorService executor = Executors.newFixedThreadPool(3);

    // Recriar dados
    private static final boolean FORCAR_DADOS_PADRAO = false;

    public static void main(String[] args) {
        if (!Boolean.getBoolean(DADOS_INICIALIZADOS)) {
            System.setProperty(DADOS_INICIALIZADOS, "true");
            
            SwingUtilities.invokeLater(() -> {
                JDialog loadingDialog = createLoadingDialog();
                
                new SwingWorker<DataModel, Void>() {
                    @Override
                    protected DataModel doInBackground() {
                        DataModel dados = DataPersistence.loadAllData();
                        if (FORCAR_DADOS_PADRAO || dados.getUsuarios().isEmpty()) {
                            System.out.println("Criando dados iniciais...");
                            createDefaultData(dados);
                            saveDataInParallel(dados);
                        }
                        return dados;
                    }

                    @Override
                    protected void done() {
                        loadingDialog.dispose();
                        try {
                            Session session = new Session();
                            new LoginFrame(session, get().getUsuarios(), get().getUnidades())
                                .setVisible(true);
                        } catch (Exception e) {
                            showError("Erro na inicialização", e.getMessage());
                        }
                    }
                }.execute();
                
                loadingDialog.setVisible(true);
            });
        }
    }

    private static JDialog createLoadingDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Carregando Sistema");
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Por favor, aguarde...", SwingConstants.CENTER), BorderLayout.CENTER);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, BorderLayout.SOUTH);
        
        dialog.add(panel);
        return dialog;
    }

    private static void createDefaultData(DataModel dados) {
        Map<Produto, Integer> itensEstoque1 = new HashMap<>();
        Estoque estoqueUnidade1 = new Estoque(itensEstoque1);
        
        Map<Produto, Integer> itensEstoque2 = new HashMap<>();
        Estoque estoqueUnidade2 = new Estoque(itensEstoque2);

        Produto p1 = new Produto("Smartphone", "A01", new BigDecimal("1500.00"));
        Produto p2 = new Produto("Notebook", "B02", new BigDecimal("3500.00"));
        Produto p3 = new Produto("Tablet", "C03", new BigDecimal("800.00"));
        
        estoqueUnidade1.adicionarProduto(p1, 50);
        estoqueUnidade1.adicionarProduto(p2, 20);
        estoqueUnidade2.adicionarProduto(p3, 30);

        Unidade unidade1 = new Unidade(1, "Unidade Centro", "Rua A, 123", estoqueUnidade1);
        Unidade unidade2 = new Unidade(2, "Unidade Bairro", "Av B, 456", estoqueUnidade2);
        
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Dono("Dono", 1, "dono123", "dono@empresa.com", List.of(unidade1, unidade2)));
        usuarios.add(new Gerente("Gerente_1", 2, "gerente123", "gerente1@empresa.com", unidade1));
        usuarios.add(new Gerente("Gerente_2", 3, "gerente456", "gerente2@empresa.com", unidade2));
        usuarios.add(new Vendedor("Vendedor_1", 4, "vendedor123", "vendedor1@empresa.com", estoqueUnidade1));
        usuarios.add(new Vendedor("Vendedor_2", 5, "vendedor456", "vendedor2@empresa.com", estoqueUnidade1));
        usuarios.add(new Vendedor("Vendedor_3", 6, "vendedor789", "vendedor3@empresa.com", estoqueUnidade2));
        
        dados.setUsuarios(usuarios);
        dados.setUnidades(List.of(unidade1, unidade2));
        dados.setPedidos(new ArrayList<>());
    }

    private static void saveDataInParallel(DataModel dados) {
        executor.execute(() -> {
            DataPersistence.saveAllData(dados.getUsuarios(), dados.getUnidades(), dados.getPedidos());
            executor.shutdown();
        });
    }

    private static void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}