package ufjf.poo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ufjf.poo.model.Unidade;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.pedido.Pedido;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataPersistence {

    private static final String USUARIOS_FILE = "usuarios.json";
    private static final String UNIDADES_FILE = "unidades.json";
    private static final String PEDIDOS_FILE = "pedidos.json";

    private static final Gson gson;

    static {
        RuntimeTypeAdapterFactory<Usuario> usuarioAdapterFactory = RuntimeTypeAdapterFactory
                .of(Usuario.class, "type")
                .registerSubtype(Dono.class, "Dono")
                .registerSubtype(Gerente.class, "Gerente")
                .registerSubtype(Vendedor.class, "Vendedor");

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(usuarioAdapterFactory)
                .registerTypeAdapter(new TypeToken<Map<Produto, Integer>>(){}.getType(), new ProdutoMapAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static DataModel loadAllData() {
        DataModel dados = new DataModel();
        
        dados.setUsuarios(loadData(USUARIOS_FILE, new TypeToken<List<Usuario>>(){}.getType()));
        dados.setUnidades(loadData(UNIDADES_FILE, new TypeToken<List<Unidade>>(){}.getType()));
        dados.setPedidos(loadData(PEDIDOS_FILE, new TypeToken<List<Pedido>>(){}.getType()));

        if (dados.getUsuarios() == null) dados.setUsuarios(new ArrayList<>());
        if (dados.getUnidades() == null) dados.setUnidades(new ArrayList<>());
        if (dados.getPedidos() == null) dados.setPedidos(new ArrayList<>());

        return dados;
    }

    private static <T> List<T> loadData(String filename, Type typeOfT) {
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, typeOfT);
        } catch (IOException e) {
            System.out.println("Arquivo de " + filename.replace(".json", "") + " não encontrado. Iniciando com dados padrão.");
            return null;
        }
    }
    
    public static void saveAllData(List<Usuario> usuarios, List<Unidade> unidades, List<Pedido> pedidos) {
        saveUsuarios(usuarios);
        saveData(unidades, UNIDADES_FILE);
        saveData(pedidos, PEDIDOS_FILE);
    }
    
    private static void saveUsuarios(List<Usuario> usuarios) {
        try (FileWriter writer = new FileWriter(USUARIOS_FILE)) {
            Type listType = new TypeToken<List<Usuario>>(){}.getType();
            gson.toJson(usuarios, listType, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> void saveData(List<T> data, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}