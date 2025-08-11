package ufjf.poo.util;

import java.math.BigDecimal;
import java.util.Map;

import ufjf.poo.model.Unidade;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
import ufjf.poo.model.usuario.Vendedor;

public class JsonUtilsExample {
    public static void main(String[] args) {
        //objetos de exemplo
        Unidade unidade = new Unidade(1, "Unidade Centro", "Rua A, 123", null);
        Estoque estoque = new Estoque(Map.of(
            new Produto("cadeira", "CAD01", BigDecimal.valueOf(9.90)), 10,
            new Produto("mesa", "MES01", BigDecimal.valueOf(20.00)), 5
            ));
        Vendedor usuario1 = new Vendedor("luan", 1234, "luan123", "luan@empresa.com", estoque);
        //salva
        JsonUtils.saveToJson(unidade, "unidade_exemplo.json");

        // Primeiro vamos testar o Estoque diretamente
        JsonUtils.saveToJson(estoque, "estoque_exemplo.json");
        Estoque estoqueCarregado = JsonUtils.loadFromJson("estoque_exemplo.json", Estoque.class);
        System.out.println("Estoque carregado tem " + estoqueCarregado.mapeiaEstoque().size() + " produtos");
        
        // Agora testamos a unidade
        Unidade unidadeCarregada = JsonUtils.loadFromJson("unidade_exemplo.json", Unidade.class);
        System.out.println("Unidade carregada: " + unidadeCarregada.getNome());

        // Por fim testamos o usuário que contém um estoque
        JsonUtils.saveToJson(usuario1, "usuario_exemplo.json");
        Vendedor vendedorCarregado = JsonUtils.loadFromJson("usuario_exemplo.json", Vendedor.class);
        System.out.println("Vendedor carregado: " + vendedorCarregado.getNome());
        System.out.println("Estoque do vendedor tem " + 
            (vendedorCarregado.getEstoqueDisponivel() != null ? 
             vendedorCarregado.getEstoqueDisponivel().mapeiaEstoque().size() : 0) + 
            " produtos");
    }
}
