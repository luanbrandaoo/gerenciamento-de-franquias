package ufjf.poo.model.usuario;

import java.util.ArrayList;
import ufjf.poo.model.pedido.Pedido;
import java.util.List;
import java.util.Scanner;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.pedido.ItemPedido;

public class Vendedor extends Usuario {
    
    private List<Pedido> pedidosRealizados;
    private Estoque estoqueDisponivel;
    

    public Vendedor(String nome, int id, String key, String email, Estoque estoqueDisponivel) {
        super(nome, id, key, email);
        
        pedidosRealizados = new ArrayList<>();
        this.estoqueDisponivel = estoqueDisponivel;
    }
    
    public List<Pedido> getPedidosRealizados(){
        return new ArrayList<>(pedidosRealizados);
    }
    
    
    
    public void cadastrarPedidos(Scanner teclado) {
        
        if(estoqueDisponivel == null){
            System.out.println("Erro: Estoque não configurado para este vendedor. Não é possível cadastrar pedidos.");
            return;
        }
                
        System.out.println("\n--- Cadastrando Novo Pedido ---");
        System.out.print("Nome do Cliente: ");
        String nomeCliente = teclado.nextLine();
        
        List<ItemPedido> itensDoPedido = new ArrayList<>();
        double total = 0.0;
        
        //TODO: criar laco de repeticao , menu interativo e verificacoes de execao
        
    }
    
    public void visualizarPedidos() {

    }
}
