package ufjf.poo.model;


public abstract class Usuario {
    
    private String nome;
    private int id;
    //private List <notificacao> notificacoes;
    private String privateKey;
    private String email;
    
    public Usuario(String nome , int id , String key, String email){
        
        this.nome = nome;
        this. id = id;
        this.email = email;
        privateKey = key;
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
        return privateKey;
    }
    
            
}
