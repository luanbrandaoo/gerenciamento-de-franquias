package ufjf.poo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonToken;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.math.BigDecimal;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import ufjf.poo.exception.DataPersistenceException;
import ufjf.poo.model.Unidade;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.pedido.Pedido;
import ufjf.poo.model.pedido.ItemPedido;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Vendedor;
import java.util.Date;

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

    static TypeAdapter<BigDecimal> bigDecimalAdapter = new TypeAdapter<BigDecimal>() {
        @Override
        public void write(JsonWriter out, BigDecimal value) throws java.io.IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }
        @Override
        public BigDecimal read(JsonReader in) throws java.io.IOException {
            String s = in.nextString();
            return new BigDecimal(s);
        }
    };

    static {
        // Adaptador para tipos de usuário
        RuntimeTypeAdapterFactory<Usuario> usuarioAdapterFactory = RuntimeTypeAdapterFactory
                .of(Usuario.class, "type")
                .registerSubtype(Dono.class, "Dono")
                .registerSubtype(Gerente.class, "Gerente")
                .registerSubtype(Vendedor.class, "Vendedor");

        // Adaptador para serialização dos usuários
        TypeAdapter<Usuario> usuarioAdapter = new TypeAdapter<Usuario>() {
            @Override
            public void write(JsonWriter out, Usuario value) throws java.io.IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }

                out.beginObject();
                out.name("id").value(value.getId());
                out.name("nome").value(value.getNome());
                out.name("email").value(value.getEmail());
                out.name("tipo").value(value.getType());

                // Serializar campos específicos de cada tipo
                if (value instanceof Vendedor) {
                    Vendedor v = (Vendedor) value;
                    out.name("pedidosRealizados");
                    out.beginArray();
                    for (Pedido p : v.getPedidosRealizados()) {
                        out.beginObject();
                        out.name("id").value(p.getIdPedido());
                        out.name("nomeCliente").value(p.getNomeCliente());
                        out.name("valorTotal");
                        bigDecimalAdapter.write(out, p.getValorTotal());
                        out.name("status").value(p.getStatus());
                        out.endObject();
                    }
                    out.endArray();
                } else if (value instanceof Gerente) {
                    Gerente g = (Gerente) value;
                    out.name("unidadeId").value(g.getUnidadeFranquia().getId());
                    out.name("unidadeNome").value(g.getUnidadeFranquia().getNome());
                } else if (value instanceof Dono) {
                    Dono d = (Dono) value;
                    out.name("unidades");
                    out.beginArray();
                    for (Unidade u : d.getUnidadesGerenciadas()) {
                        out.beginObject();
                        out.name("id").value(u.getId());
                        out.name("nome").value(u.getNome());
                        out.endObject();
                    }
                    out.endArray();
                }
                out.endObject();
            }

            @Override
            public Usuario read(JsonReader in) throws java.io.IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }

                in.beginObject();
                int id = 0;
                String nome = null;
                String email = null;
                String tipo = null;
                List<Pedido> pedidos = new ArrayList<>();
                int unidadeId = 0;
                String unidadeNome = null;
                List<Unidade> unidades = new ArrayList<>();

                while (in.hasNext()) {
                    String field = in.nextName();
                    switch (field) {
                        case "id": id = in.nextInt(); break;
                        case "nome": nome = in.nextString(); break;
                        case "email": email = in.nextString(); break;
                        case "tipo": tipo = in.nextString(); break;
                        case "pedidosRealizados":
                            in.beginArray();
                            while (in.hasNext()) {
                                in.beginObject();
                                int pedidoId = 0;
                                String nomeCliente = null;
                                BigDecimal valorTotal = null;
                                String status = null;
                                while (in.hasNext()) {
                                    String pedidoField = in.nextName();
                                    switch (pedidoField) {
                                        case "id": pedidoId = in.nextInt(); break;
                                        case "nomeCliente": nomeCliente = in.nextString(); break;
                                        case "valorTotal": valorTotal = bigDecimalAdapter.read(in); break;
                                        case "status": status = in.nextString(); break;
                                        default: in.skipValue();
                                    }
                                }
                                Pedido p = new Pedido(pedidoId, nomeCliente, null, new Date(), "PENDENTE", "PENDENTE");
                                p.setStatus(status);
                                pedidos.add(p);
                                in.endObject();
                            }
                            in.endArray();
                            break;
                        case "unidadeId": unidadeId = in.nextInt(); break;
                        case "unidadeNome": unidadeNome = in.nextString(); break;
                        case "unidades":
                            in.beginArray();
                            while (in.hasNext()) {
                                in.beginObject();
                                int uId = 0;
                                String uNome = null;
                                while (in.hasNext()) {
                                    String unidadeField = in.nextName();
                                    switch (unidadeField) {
                                        case "id": uId = in.nextInt(); break;
                                        case "nome": uNome = in.nextString(); break;
                                        default: in.skipValue();
                                    }
                                }
                                unidades.add(new Unidade(uId, uNome, "", null));
                                in.endObject();
                            }
                            in.endArray();
                            break;
                        default: in.skipValue();
                    }
                }
                in.endObject();

                Usuario usuario;
                switch (tipo) {
                    case "Vendedor":
                        usuario = new Vendedor(nome, id, "senha", email, null);
                        break;
                    case "Gerente":
                        Unidade u = new Unidade(unidadeId, unidadeNome, "", null);
                        usuario = new Gerente(nome, id, "senha", email, u);
                        break;
                    case "Dono":
                        usuario = new Dono(nome, id, "senha", email, unidades);
                        break;
                    default:
                        throw new IOException("Tipo de usuário desconhecido: " + tipo);
                }
                return usuario;
            }
        };

        TypeAdapter<java.util.Date> dateAdapter = new TypeAdapter<java.util.Date>() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            @Override
            public void write(JsonWriter out, java.util.Date value) throws java.io.IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(dateFormat.format(value));
                }
            }
            @Override
            public java.util.Date read(JsonReader in) throws java.io.IOException {
                String s = in.nextString();
                try {
                    return dateFormat.parse(s);
                } catch (ParseException e) {
                    throw new java.io.IOException(e);
                }
            }
        };

        TypeAdapter<ItemPedido> itemPedidoAdapter = new TypeAdapter<ItemPedido>() {
            @Override
            public void write(JsonWriter out, ItemPedido value) throws java.io.IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.beginObject();
                out.name("produto");
                if (value.produto() == null) {
                    out.nullValue();
                } else {
                    gson.toJson(value.produto(), Produto.class, out);
                }
                out.name("quantidade");
                out.value(value.quantidade());
                out.name("subtotal");
                if (value.subtotal() == null) {
                    out.nullValue();
                } else {
                    gson.toJson(value.subtotal(), BigDecimal.class, out);
                }
                out.endObject();
            }

            @Override
            public ItemPedido read(JsonReader in) throws java.io.IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                
                in.beginObject();
                Produto produto = null;
                int quantidade = 0;
                BigDecimal subtotal = null;

                while (in.hasNext()) {
                    String name = in.nextName();
                    try {
                        switch (name) {
                            case "produto":
                                if (in.peek() != JsonToken.NULL) {
                                    produto = gson.fromJson(in, Produto.class);
                                } else {
                                    in.nextNull();
                                }
                                break;
                            case "quantidade":
                                if (in.peek() != JsonToken.NULL) {
                                    quantidade = in.nextInt();
                                } else {
                                    in.nextNull();
                                }
                                break;
                            case "subtotal":
                                if (in.peek() != JsonToken.NULL) {
                                    subtotal = gson.fromJson(in, BigDecimal.class);
                                } else {
                                    in.nextNull();
                                }
                                break;
                            default:
                                in.skipValue();
                                break;
                        }
                    } catch (IllegalStateException | JsonSyntaxException e) {
                        in.skipValue();
                    }
                }
                in.endObject();
                
                if (produto == null) {
                    return null;
                }
                
                return new ItemPedido(produto, quantidade, subtotal != null ? subtotal : BigDecimal.ZERO);
            }
        };

        // Adaptador para Produto
        TypeAdapter<Produto> produtoAdapter = new TypeAdapter<Produto>() {
            @Override
            public void write(JsonWriter out, Produto value) throws java.io.IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.beginObject();
                out.name("nome").value(value.getNome() != null ? value.getNome() : "");
                out.name("codigo").value(value.getCodigo() != null ? value.getCodigo() : "");
                out.name("preco");
                if (value.getPreco() == null) {
                    out.nullValue();
                } else {
                    gson.toJson(value.getPreco(), BigDecimal.class, out);
                }
                out.endObject();
            }

            @Override
            public Produto read(JsonReader in) throws java.io.IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                
                in.beginObject();
                String nome = "";
                String codigo = "";
                BigDecimal preco = BigDecimal.ZERO;

                while (in.hasNext()) {
                    String fieldName = in.nextName();
                    try {
                        switch (fieldName) {
                            case "nome":
                                if (in.peek() != JsonToken.NULL) {
                                    nome = in.nextString();
                                } else {
                                    in.nextNull();
                                }
                                break;
                            case "codigo":
                                if (in.peek() != JsonToken.NULL) {
                                    codigo = in.nextString();
                                } else {
                                    in.nextNull();
                                }
                                break;
                            case "preco":
                                if (in.peek() != JsonToken.NULL) {
                                    preco = gson.fromJson(in, BigDecimal.class);
                                } else {
                                    in.nextNull();
                                }
                                break;
                            default:
                                in.skipValue();
                                break;
                        }
                    } catch (IllegalStateException | JsonSyntaxException e) {
                        in.skipValue();
                    }
                }
                in.endObject();
                
                if (nome == null || nome.isEmpty() || codigo == null || codigo.isEmpty()) {
                    return null;
                }
                
                return new Produto(nome, codigo, preco != null ? preco : BigDecimal.ZERO);
            }
        };

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(usuarioAdapterFactory)
                .registerTypeAdapter(new TypeToken<Map<Produto, Integer>>(){}.getType(), new ProdutoMapAdapter())
                .registerTypeAdapter(java.util.Date.class, dateAdapter)
                .registerTypeAdapter(BigDecimal.class, bigDecimalAdapter)
                .registerTypeAdapter(ItemPedido.class, itemPedidoAdapter)
                .registerTypeAdapter(Produto.class, produtoAdapter)
                .registerTypeAdapter(Usuario.class, usuarioAdapter)
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    private static void validateData(List<Usuario> usuarios, List<Unidade> unidades, List<Pedido> pedidos) {
        if (usuarios == null || unidades == null || pedidos == null) {
            throw new IllegalArgumentException("As listas de dados não podem ser nulas");
        }
    }


    public static DataModel loadAllData() {
        DataModel dados = new DataModel();
        
        try {
            List<Usuario> usuarios = loadData(USUARIOS_FILE, new TypeToken<List<Usuario>>(){}.getType());
            List<Unidade> unidades = loadData(UNIDADES_FILE, new TypeToken<List<Unidade>>(){}.getType());
            List<Pedido> pedidos = loadData(PEDIDOS_FILE, new TypeToken<List<Pedido>>(){}.getType());
            
            dados.setUsuarios(usuarios != null ? usuarios.stream().filter(u -> u != null).toList() : new ArrayList<>());
            dados.setUnidades(unidades != null ? unidades.stream().filter(u -> u != null).toList() : new ArrayList<>());
            dados.setPedidos(pedidos != null ? pedidos.stream().filter(p -> p != null).toList() : new ArrayList<>());
        } catch (Exception e) {
            dados.setUsuarios(new ArrayList<>());
            dados.setUnidades(new ArrayList<>());
            dados.setPedidos(new ArrayList<>());
            System.out.println("Erro ao carregar dados. Iniciando com listas vazias: " + e.getMessage());
        }

        return dados;
    }

    private static <T> List<T> loadData(String filename, Type typeOfT) {
        try (FileReader reader = new FileReader(filename)) {
            List<T> data = gson.fromJson(reader, typeOfT);
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Arquivo de " + filename.replace(".json", "") + " não encontrado. Iniciando com dados padrão.");
            return null;
        } catch (JsonSyntaxException e) {
            throw new DataPersistenceException("Erro ao ler arquivo " + filename, e);
        }

    }
    
    public static void saveAllData(List<Usuario> usuarios, List<Unidade> unidades, List<Pedido> pedidos) {
        validateData(usuarios, unidades, pedidos);
        saveData(usuarios, USUARIOS_FILE);
        saveData(unidades, UNIDADES_FILE);
        saveData(pedidos, PEDIDOS_FILE);
    }

        private static <T> void saveData(List<T> data, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new DataPersistenceException("Erro ao salvar arquivo " + filename, e);
        }
    }    public static void savePedido(List<Pedido> pedidos) {
        saveAllData(loadAllData().getUsuarios(), loadAllData().getUnidades(), pedidos);
    }

    public static void saveUsuario(List<Usuario> usuarios) {
        saveAllData(usuarios, loadAllData().getUnidades(), loadAllData().getPedidos());
    }

    public static void saveUnidade(List<Unidade> unidades) {
        saveAllData(loadAllData().getUsuarios(), unidades, loadAllData().getPedidos());
    }

}