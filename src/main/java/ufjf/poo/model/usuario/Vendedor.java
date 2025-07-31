package ufjf.poo.model.usuario;

import java.util.ArrayList;
import ufjf.poo.model.pedido.Pedido;
import java.util.List;
import java.util.Scanner;
import ufjf.poo.model.pedido.ItemPedido;

public class Vendedor extends Usuario {
    
    private List<Pedido> pedidosRealizados;

    public Vendedor(String nome, int id, String key, String email) {
        super(nome, id, key, email);
        pedidosRealizados = new ArrayList<>();
    }
    
    public void cadastrarPedidos(Scanner teclado) {
        
        System.out.println("\n--- Cadastrando Novo Pedido ---");
        System.out.print("Nome do Cliente: ");
        
        String nomeCliente = teclado.nextLine();
        
        List<ItemPedido> itensDoPedido = new ArrayList<>();
        float total = 0;
        
    }
    
    public void visualizarPedidos() {

    }
}
