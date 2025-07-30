package ufjf.poo.model.estoque;

public class Produto {
    
    private String nome;
    private String codigo;
    private float preco;
    
    public Produto(String nome , String codigo, float preco){
        
        if(preco < 0){
            throw new IllegalArgumentException("O valor do produto nao deve ser negativo! ");
        }
        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("O nome do produto nao deve estar vazio! ");
        }
        if(codigo == null || nome.trim().isEmpty()){
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
    
    public float getPreco(){
        return preco;
    }
    
    public void setNome(String nome){
        
        if(nome != null && !nome.trim().isEmpty())
            this.nome = nome;
    }
    
    public void setPreco(float preco){
        
        if(preco > 0 )
            this.preco = preco;
    }
    
}
