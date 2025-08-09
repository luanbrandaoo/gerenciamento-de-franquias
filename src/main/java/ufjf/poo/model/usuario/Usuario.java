/*
Rafael Müller dos Santos Moreira 202465557C
Luan Brandão de Oliveira 202465055A
Marcos José de Oliveira Júnior 202135011
 */

package ufjf.poo.model.usuario;

import ufjf.poo.exception.NomeUsuarioInvalidoException;
import ufjf.poo.exception.IdUsuarioInvalidoException;
import ufjf.poo.exception.EmailInvalidoException;
import ufjf.poo.exception.ChavePrivadaInvalidaException;


public abstract class Usuario {
    
    private  String nome;
    private  int id;
    //private List <notificacao> notificacoes;
    private  String privateKey;
    private  String email;
    private final static String type = "Usuario";
    
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
        this.id = id;
        this.email = email;
        privateKey = key;
    }
    
    public void login(){
        
    }
    
    public void deslogar(){
        
    }
    
    public abstract void relatorioAtividades();
    
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
    
    public void setPrivateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new ChavePrivadaInvalidaException();
        }
        this.privateKey = key;
    }
    
     public void setNome(String nome) {
         if (nome == null || nome.trim().isEmpty()) {
             throw new NomeUsuarioInvalidoException();
         }
        this.nome = nome;
    }
    
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new EmailInvalidoException();
        }
        this.email = email;
    }
}
