package ufjf.poo.model.usuario;

import java.util.ArrayList;
import java.util.Date;
import ufjf.poo.model.pedido.Pedido;
import java.util.List;
import java.util.Scanner;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;
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
        
        double totalDoPedido = 0.0;
        
        boolean addItem = true;
        
        while(addItem){
            
            System.out.println("\n Produtos Disponíveis! ");
            estoqueDisponivel.listarProdutos();
            
            System.out.print("Digite o código do produto (ou 'sair' para finalizar o pedido): ");
            String codigoProduto = teclado.nextLine();
            
            if(codigoProduto.equalsIgnoreCase("sair")){
                addItem =  false;
                break;
            }
            
            Produto produtoEscolhido = estoqueDisponivel.buscarProdutoPorCodigo(codigoProduto);
            if(produtoEscolhido != null){
                System.out.print("Qual a quantidade desejada para " + produtoEscolhido.getNome() + ": ");
                int quantidade = Integer.parseInt(teclado.nextLine());
                
                //esta funcao retorna um valor logioco e so faz o pedido caso hava estoque disponivel
                if(estoqueDisponivel.estaDisponivel(produtoEscolhido, quantidade)){
                    
                    double subtotalDoItem = produtoEscolhido.getPreco() * quantidade;
                    ItemPedido novoItem = new ItemPedido(produtoEscolhido, quantidade, subtotalDoItem);
                    itensDoPedido.add(novoItem);
                    totalDoPedido += subtotalDoItem;
                    
                    System.out.println("Item adicionado: " + produtoEscolhido.getNome() + " " + quantidade);
                }     
            }
            
            System.out.print("Deseja adicionar mais itens a este pedido? (s/n): ");
            addItem = teclado.nextLine().equalsIgnoreCase("s");
        }
        
        if(itensDoPedido.isEmpty()){
            System.out.println("Nenhum item adicionado. Pedido cancelado.");
            return;
        }
        
        System.out.print("Forma de Pagamento: Dinheiro, Cartão, PIX: ");
        String formaDePagamento = teclado.nextLine();
        
        System.out.print("Forma de Entrega: Entrega ou Retirada ");
        String formaDeEntrega = teclado.nextLine();
        
        String idAleatorio = java.util.UUID.randomUUID().toString();
        int idPedido = Integer.parseInt(idAleatorio);
                
        Date dataHoraAtual = new Date();
        
        
        Pedido novoPedido = new Pedido(idPedido, nomeCliente, this, dataHoraAtual, formaDePagamento, formaDeEntrega);
        
        novoPedido.adicionarItem(itensDoPedido);
        novoPedido.calcularTotal();
        
        for (ItemPedido item : itensDoPedido) {
            estoqueDisponivel.subtraiProduto(item.produto(), item.quantidade());
        }
        
        pedidosRealizados.add(novoPedido);
        System.out.println("\nPedido " + novoPedido.getIdPedido() + " cadastrado com sucesso!");
        System.out.println("Cliente: " + novoPedido.getNomeCliente() + ", Valor Total: R$" + String.format("%.2f", novoPedido.getValorTotal()));
        
    }
    
    public void visualizarPedidos() {

    }
}
