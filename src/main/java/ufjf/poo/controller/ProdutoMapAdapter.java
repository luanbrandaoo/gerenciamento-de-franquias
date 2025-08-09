package ufjf.poo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ufjf.poo.model.estoque.Produto;

public class ProdutoMapAdapter extends TypeAdapter<Map<Produto, Integer>> {

    private final Gson gson = new Gson();

    @Override
    public void write(JsonWriter out, Map<Produto, Integer> produtos) throws IOException {
        if (produtos == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        for (Map.Entry<Produto, Integer> entry : produtos.entrySet()) {
            Produto produto = entry.getKey();
            Integer quantidade = entry.getValue();
            
            if (produto != null) {
                String produtoJson = gson.toJson(produto);
                out.name(produtoJson);
                out.value(quantidade != null ? quantidade : 0);
            }
        }
        out.endObject();
    }

    @Override
    public Map<Produto, Integer> read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            return null;
        }

        Map<Produto, Integer> produtos = new HashMap<>();
        in.beginObject();
        while (in.hasNext()) {
            try {
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
            } catch (com.google.gson.JsonSyntaxException | IllegalStateException e) {
                // Skip invalid entries due to malformed JSON or type mismatches
                in.skipValue();
            } catch (IOException e) {
                // Re-throw IO exceptions as they indicate more serious problems
                throw e;
            }
        }
        in.endObject();
        return produtos;
    }
}