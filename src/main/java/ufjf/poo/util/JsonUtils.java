package ufjf.poo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ufjf.poo.controller.RuntimeTypeAdapterFactory;
import ufjf.poo.exception.DataPersistenceException;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Dono;
import ufjf.poo.model.usuario.Gerente;
import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.model.usuario.Vendedor;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static Gson gson;

    static {
        // Adaptador para tipos de usuário
        RuntimeTypeAdapterFactory<Usuario> usuarioAdapterFactory = RuntimeTypeAdapterFactory
                .of(Usuario.class, "type")
                .registerSubtype(Dono.class, "Dono")
                .registerSubtype(Gerente.class, "Gerente")
                .registerSubtype(Vendedor.class, "Vendedor");

        // Adaptador para BigDecimal
        TypeAdapter<BigDecimal> bigDecimalAdapter = new TypeAdapter<BigDecimal>() {
            @Override
            public void write(JsonWriter out, BigDecimal value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(value.toString());
                }
            }

            @Override
            public BigDecimal read(JsonReader in) throws IOException {
                String s = in.nextString();
                return new BigDecimal(s);
            }
        };

        // Adaptador para Date
        TypeAdapter<Date> dateAdapter = new TypeAdapter<Date>() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            @Override
            public void write(JsonWriter out, Date value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(dateFormat.format(value));
                }
            }

            @Override
            public Date read(JsonReader in) throws IOException {
                try {
                    String s = in.nextString();
                    return dateFormat.parse(s);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        };

        // Adaptador para Produto
        TypeAdapter<Produto> produtoAdapter = new TypeAdapter<Produto>() {
            @Override
            public void write(JsonWriter out, Produto value) throws IOException {
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
                    out.value(value.getPreco().toString());
                }
                out.endObject();
            }

            @Override
            public Produto read(JsonReader in) throws IOException {
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
                    switch (fieldName) {
                        case "nome":
                            nome = in.nextString();
                            break;
                        case "codigo":
                            codigo = in.nextString();
                            break;
                        case "preco":
                            preco = new BigDecimal(in.nextString());
                            break;
                        default:
                            in.skipValue();
                    }
                }
                in.endObject();
                
                return new Produto(nome, codigo, preco);
            }
        };

        // Adaptador para Map<Produto, Integer>
        TypeAdapter<Map<Produto, Integer>> produtoMapAdapter = new TypeAdapter<Map<Produto, Integer>>() {
            @Override
            public void write(JsonWriter out, Map<Produto, Integer> value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }

                out.beginObject();
                for (Map.Entry<Produto, Integer> entry : value.entrySet()) {
                    out.name(gson.toJson(entry.getKey())); // Serializa a chave Produto como string JSON
                    out.value(entry.getValue());
                }
                out.endObject();
            }

            @Override
            public Map<Produto, Integer> read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }

                Map<Produto, Integer> produtos = new HashMap<>();
                in.beginObject();
                while (in.hasNext()) {
                    String produtoJson = in.nextName();
                    if (produtoJson == null || produtoJson.isEmpty()) {
                        in.skipValue();
                        continue;
                    }
                    
                    Produto produto = gson.fromJson(produtoJson, Produto.class);
                    if (produto == null) {
                        in.skipValue();
                        continue;
                    }

                    int quantidade = in.nextInt();
                    if (quantidade < 0) {
                        quantidade = 0;
                    }
                    
                    produtos.put(produto, quantidade);
                }
                in.endObject();
                return produtos;
            }
        };

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(usuarioAdapterFactory)
                .registerTypeAdapter(BigDecimal.class, bigDecimalAdapter)
                .registerTypeAdapter(Date.class, dateAdapter)
                .registerTypeAdapter(Produto.class, produtoAdapter)
                .registerTypeAdapter(new TypeToken<Map<Produto, Integer>>(){}.getType(), produtoMapAdapter)
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    public static <T> T loadFromJson(String filename, Class<T> classOfT) {
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, classOfT);
        } catch (IOException e) {
            System.out.println("Arquivo " + filename + " não encontrado.");
            return null;
        }
    }

    public static <T> void saveToJson(T data, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new DataPersistenceException("Erro ao salvar arquivo " + filename, e);
        }
    }

    public static <T> void saveListToJson(List<T> data, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new DataPersistenceException("Erro ao salvar arquivo " + filename, e);
        }
    }

    public static <T> List<T> loadListFromJson(String filename, Type typeOfT) {
        try (FileReader reader = new FileReader(filename)) {
            List<T> data = gson.fromJson(reader, typeOfT);
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Arquivo " + filename + " não encontrado.");
            return new ArrayList<>();
        }
    }
}

