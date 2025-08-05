package ufjf.poo.model.usuario;

import ufjf.poo.exception.NomeUsuarioInvalidoException;
import ufjf.poo.exception.IdUsuarioInvalidoException;
import ufjf.poo.exception.EmailInvalidoException;
import ufjf.poo.exception.ChavePrivadaInvalidaException;


public abstract class Usuario {
    
    private  String nome;
    private  int id;
    //private List <notificacao> notificacoes;
    private final String privateKey;
    private  String email;
    
    public Usuario(String nome , int id , String key, String email){
        
    if (nome == null || nome.trim().isEmpty()) {
        throw new NomeUsuarioInvalidoException();
    }
    if (id <= 0) {
        throw new IdUsuarioInvalidoException();
    }
    if (email == null || !email.contains("@")) { 
        throw new EmailInvalidoException();
    }
    if (key == null || key.trim().isEmpty()) { 
        throw new ChavePrivadaInvalidaException();
    }
        
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
    
     public void setNome(String nome) {
        this.nome = nome;
    }
     
    public void setId(int id) {
        this.id = id;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
            
}
