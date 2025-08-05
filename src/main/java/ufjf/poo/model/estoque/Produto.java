package ufjf.poo.model.estoque;

import java.math.BigDecimal;

public class Produto {
    
    private String nome;
    private String codigo;
    private BigDecimal preco;
    
    public Produto(String nome , String codigo, BigDecimal preco){
        
        if(preco == null || preco.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("O valor do produto nao deve ser negativo! ");
        }
        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("O nome do produto nao deve estar vazio! ");
        }
        if(codigo == null || codigo.trim().isEmpty()){
            throw new IllegalArgumentException("O codigo do produto nao deve estar vazio! ");
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
