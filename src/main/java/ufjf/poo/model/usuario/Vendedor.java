package ufjf.poo.model.usuario;

import java.util.ArrayList;
import ufjf.poo.model.pedido.Pedido;
import java.util.List;

public class Vendedor extends Usuario {
    
    private List<Pedido> pedidosRealizados;

    public Vendedor(String nome, int id, String key, String email) {
        super(nome, id, key, email);
        pedidosRealizados = new ArrayList<>();
    }
    
    public void cadastrarPedidos() {

    }
    
    public void visualizarPedidos() {

    }
}
