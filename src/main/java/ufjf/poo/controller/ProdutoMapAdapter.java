package ufjf.poo.controller;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ufjf.poo.model.estoque.Produto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            String produtoJson = gson.toJson(entry.getKey());
            out.name(produtoJson);
            out.value(entry.getValue());
        }
        out.endObject();
    }

    @Override
    public Map<Produto, Integer> read(JsonReader in) throws IOException {
        Map<Produto, Integer> produtos = new HashMap<>();
        in.beginObject();
        while (in.hasNext()) {
            String produtoJson = in.nextName();
            Produto produto = gson.fromJson(produtoJson, Produto.class);
            int quantidade = in.nextInt();
            produtos.put(produto, quantidade);
        }
        in.endObject();
        return produtos;
    }
}