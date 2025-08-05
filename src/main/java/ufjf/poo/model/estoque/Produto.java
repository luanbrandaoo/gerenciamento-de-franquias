package ufjf.poo.model.estoque;

import java.math.BigDecimal;
import ufjf.poo.exception.PrecoInvalidoException;
import ufjf.poo.exception.NomeProdutoInvalidoException;
import ufjf.poo.exception.CodigoProdutoInvalidoException;

public class Produto {
    
    private String nome;
    private String codigo;
    private BigDecimal preco;
    
    public Produto(String nome , String codigo, BigDecimal preco){
        
        if(preco == null || preco.compareTo(BigDecimal.ZERO) < 0){
            throw new PrecoInvalidoException();
        }
        if(nome == null || nome.trim().isEmpty()){
            throw new NomeProdutoInvalidoException();
        }
        if(codigo == null || codigo.trim().isEmpty()){
            throw new CodigoProdutoInvalidoException();
        }
        
        this.nome = nome;
        this.codigo = codigo;
        this.preco = preco;
    }
    
    public String getNome(){
        return nome;
    }
    
    public String getCodigo(){
        return codigo;
    }
    
    public BigDecimal getPreco(){
        return preco;
    }
    
    public void setNome(String nome){
        
        if(nome != null && !nome.trim().isEmpty())
            this.nome = nome;
    }
    
    public void setPreco(BigDecimal preco){
        
        if(preco != null && preco.compareTo(BigDecimal.ZERO) > 0 )
            this.preco = preco;
    }
    
}
