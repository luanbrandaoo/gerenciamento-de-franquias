package ufjf.poo.model;
import java.util.List;


public abstract class usuario {
    
    private String nome;
    private int id;
    //private List <notificacao> notificacoes;
    private String private_Key;
    private String email;
    
    public usuario(String nome , int id , String key, String email){
        
        this.nome = nome;
        this. id = id;
        this.email = email;
        private_Key = key;
    }
    
    public void login(){
        
    }
    
    public void deslogar(){
        
    }
    
    public void apagarNotificacao(){
        
    }
    
    public void relatorioDeAtividades(){
        
    }
    
    public String getNome(){
        return nome;
    }
    
    public int getId(){
        return id;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getPrivateKey(){
        return private_Key;
    }
    
            
}
