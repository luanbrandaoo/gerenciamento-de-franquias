/*
Rafael Müller dos Santos Moreira 202465557C
Luan Brandão de Oliveira 202465055A
Marcos José de Oliveira Júnior 202135011
 */

package ufjf.poo.model;

import ufjf.poo.model.usuario.Usuario;
import ufjf.poo.exception.ChavePrivadaInvalidaException;
import ufjf.poo.exception.IdUsuarioInvalidoException;

public class Session {
    
    private Usuario usuarioLogado;
    
     public Session() {
        this.usuarioLogado = null;
    }
    
    public void login(Usuario usuario, String privateKey) throws ChavePrivadaInvalidaException, IdUsuarioInvalidoException {
        
        if (usuario == null) {
            throw new IdUsuarioInvalidoException();
        }
        
        if (!usuario.getPrivateKey().equals(privateKey)) {
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
