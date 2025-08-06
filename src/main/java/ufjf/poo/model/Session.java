package ufjf.poo.model;

import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.exception.ChavePrivadaInvalidaException;
import ufjf.poo.exception.IdUsuarioInvalidoException;

public class Session {
    
    private Usuario usuarioLogado;
    
    public void login(Usuario usuario, String privateKey) throws ChavePrivadaInvalidaException, IdUsuarioInvalidoException {
        
        if (usuario == null) {
            throw new IdUsuarioInvalidoException();
        }
        
        if (privateKey == null || !usuario.getPrivateKey().equals(privateKey)) {
            throw new ChavePrivadaInvalidaException();
        }
        
        this.usuarioLogado = usuario;
    }
    
    public void deslogar() {
        if (this.usuarioLogado != null) {
            this.usuarioLogado = null;
        }
    }
    
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
    public boolean ehUsuarioLogado() {
        return usuarioLogado != null;
    }
}
