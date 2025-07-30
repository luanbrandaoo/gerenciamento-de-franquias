package ufjf.poo.model.usuario;

import ufjf.poo.model.pedido.Pedido;

import java.util.List;

public class Vendedor extends Usuario {
    
    private List<Pedido> pedidosRealizados;

    public Vendedor(String nome, int id, String key, String email) {
        super(nome, id, key, email);
    }
    
    public void cadastrarPedidos() {

    }
    
    public void visualizarPedidos() {

    }
}
