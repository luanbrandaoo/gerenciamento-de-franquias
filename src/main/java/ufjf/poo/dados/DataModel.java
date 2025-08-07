package ufjf.poo.dados;

import ufjf.poo.model.Unidade;
import ufjf.poo.model.pedido.Pedido;
import ufjf.poo.model.usuario.Usuario;
import java.util.List;

public class DataModel {

    private List<Usuario> usuarios;
    private List<Unidade> unidades;
    private List<Pedido> pedidos;
    
    // Construtor padrão (sem argumentos) - NECESSÁRIO para o DataPersistence
    public DataModel() {
    }

    // Construtor com todos os dados
    public DataModel(List<Usuario> usuarios, List<Unidade> unidades, List<Pedido> pedidos) {
        this.usuarios = usuarios;
        this.unidades = unidades;
        this.pedidos = pedidos;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Unidade> getUnidades() {
        return unidades;
    }

    public void setUnidades(List<Unidade> unidades) {
        this.unidades = unidades;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
