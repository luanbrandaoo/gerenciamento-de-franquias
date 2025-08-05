package ufjf.poo.model.usuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import ufjf.poo.exception.NomeUsuarioInvalidoException;
import ufjf.poo.exception.IdUsuarioInvalidoException;
import ufjf.poo.exception.EmailInvalidoException;
import ufjf.poo.exception.ChavePrivadaInvalidaException;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("João Silva", 1, "senha123", "joao@email.com") {
        };
    }

    @Test
    void testCriacaoUsuarioValido() {
        assertEquals("João Silva", usuario.getNome());
        assertEquals(1, usuario.getId());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getPrivateKey());
    }

    @Test
    void testCriacaoUsuarioComNomeNulo() {
        assertThrows(NomeUsuarioInvalidoException.class, () -> {
            new Usuario(null, 1, "senha123", "joao@email.com") {};
        });
    }

    @Test
    void testCriacaoUsuarioComNomeVazio() {
        assertThrows(NomeUsuarioInvalidoException.class, () -> {
            new Usuario("", 1, "senha123", "joao@email.com") {};
        });
    }

    @Test
    void testCriacaoUsuarioComIdNegativo() {
        assertThrows(IdUsuarioInvalidoException.class, () -> {
            new Usuario("João Silva", -1, "senha123", "joao@email.com") {};
        });
    }

    @Test
    void testCriacaoUsuarioComIdZero() {
        assertThrows(IdUsuarioInvalidoException.class, () -> {
            new Usuario("João Silva", 0, "senha123", "joao@email.com") {};
        });
    }

    @Test
    void testCriacaoUsuarioComEmailInvalido() {
        assertThrows(EmailInvalidoException.class, () -> {
            new Usuario("João Silva", 1, "senha123", "email-invalido") {};
        });
    }

    @Test
    void testCriacaoUsuarioComEmailNulo() {
        assertThrows(EmailInvalidoException.class, () -> {
            new Usuario("João Silva", 1, "senha123", null) {};
        });
    }

    @Test
    void testCriacaoUsuarioComPrivateKeyNula() {
        assertThrows(ChavePrivadaInvalidaException.class, () -> {
            new Usuario("João Silva", 1, null, "joao@email.com") {};
        });
    }

    @Test
    void testCriacaoUsuarioComPrivateKeyVazia() {
        assertThrows(ChavePrivadaInvalidaException.class, () -> {
            new Usuario("João Silva", 1, "", "joao@email.com") {};
        });
    }

    @Test
    void testSetNome() {
        usuario.setNome("Maria Santos");
        assertEquals("Maria Santos", usuario.getNome());
    }

    @Test
    void testSetId() {
        usuario.setId(100);
        assertEquals(100, usuario.getId());
    }

    @Test
    void testSetEmail() {
        usuario.setEmail("maria@email.com");
        assertEquals("maria@email.com", usuario.getEmail());
    }
}
