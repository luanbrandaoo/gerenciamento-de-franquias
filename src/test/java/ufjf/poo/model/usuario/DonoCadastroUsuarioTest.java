package ufjf.poo.model.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ufjf.poo.model.Unidade;
import ufjf.poo.model.estoque.Estoque;
import ufjf.poo.model.estoque.Produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DonoCadastroUsuarioTest {

    private Dono dono;
    private List<Unidade> unidades;
    private List<Usuario> todosUsuarios;
    private Unidade unidade1;
    private Unidade unidade2;
    private Estoque estoque1;
    private Estoque estoque2;

    @BeforeEach
    void setUp() {
        Produto produto1 = new Produto("Notebook", "NB001", new BigDecimal("2500.00"));
        Produto produto2 = new Produto("Mouse", "M001", new BigDecimal("50.00"));

        HashMap<Produto, Integer> produtos1 = new HashMap<>();
        produtos1.put(produto1, 10);
        produtos1.put(produto2, 25);
        estoque1 = new Estoque(produtos1);

        HashMap<Produto, Integer> produtos2 = new HashMap<>();
        produtos2.put(produto1, 5);
        produtos2.put(produto2, 15);
        estoque2 = new Estoque(produtos2);

        unidade1 = new Unidade(1, "Franquia Centro", "Rua Central, 123", estoque1);
        unidade2 = new Unidade(2, "Franquia Sul", "Av Sul, 456", estoque2);

        unidades = new ArrayList<>();
        unidades.add(unidade1);
        unidades.add(unidade2);

        dono = new Dono("João Dono", 1, "senha123", "dono@empresa.com", unidades);

        todosUsuarios = new ArrayList<>();
        todosUsuarios.add(dono);
    }

    @Test
    void testCadastrarVendedorComSucesso() {
        Usuario novoVendedor = dono.cadastrarUsuario(
            "Maria Vendedora", 
            "maria@empresa.com", 
            "senha456", 
            "vendedor", 
            unidade1, 
            todosUsuarios
        );

        assertNotNull(novoVendedor);
        assertInstanceOf(Vendedor.class, novoVendedor);
        assertEquals("Maria Vendedora", novoVendedor.getNome());
        assertEquals("maria@empresa.com", novoVendedor.getEmail());
        assertEquals(2, todosUsuarios.size());
        assertTrue(unidade1.getVendedores().contains(novoVendedor));
    }

    @Test
    void testCadastrarGerenteComSucesso() {
        Usuario novoGerente = dono.cadastrarUsuario(
            "Carlos Gerente", 
            "carlos@empresa.com", 
            "senha789", 
            "gerente", 
            unidade1, 
            todosUsuarios
        );

        assertNotNull(novoGerente);
        assertInstanceOf(Gerente.class, novoGerente);
        assertEquals("Carlos Gerente", novoGerente.getNome());
        assertEquals("carlos@empresa.com", novoGerente.getEmail());
        assertEquals(2, todosUsuarios.size());
        assertEquals(novoGerente, unidade1.getGerente());
    }

    @Test
    void testCadastrarDonoComSucesso() {
        Usuario novoDono = dono.cadastrarUsuario(
            "Ana Dona", 
            "ana@empresa.com", 
            "senha000", 
            "dono", 
            null, 
            todosUsuarios
        );

        assertNotNull(novoDono);
        assertInstanceOf(Dono.class, novoDono);
        assertEquals("Ana Dona", novoDono.getNome());
        assertEquals("ana@empresa.com", novoDono.getEmail());
        assertEquals(2, todosUsuarios.size());
    }

    @Test
    void testCadastrarUsuarioComEmailDuplicado() {
        Usuario primeiro = dono.cadastrarUsuario(
            "Maria Primeira", 
            "maria@empresa.com", 
            "senha456", 
            "vendedor", 
            unidade1, 
            todosUsuarios
        );
        assertNotNull(primeiro);

        Usuario segundo = dono.cadastrarUsuario(
            "Maria Segunda", 
            "maria@empresa.com", 
            "senha789", 
            "vendedor", 
            unidade2, 
            todosUsuarios
        );
        assertNull(segundo);
        assertEquals(2, todosUsuarios.size());
    }

    @Test
    void testCadastrarVendedorSemUnidade() {
        Usuario vendedor = dono.cadastrarUsuario(
            "Pedro Vendedor", 
            "pedro@empresa.com", 
            "senha456", 
            "vendedor", 
            null, 
            todosUsuarios
        );

        assertNull(vendedor);
        assertEquals(1, todosUsuarios.size());
    }

    @Test
    void testCadastrarGerenteSemUnidade() {
        Usuario gerente = dono.cadastrarUsuario(
            "Paulo Gerente", 
            "paulo@empresa.com", 
            "senha456", 
            "gerente", 
            null, 
            todosUsuarios
        );

        assertNull(gerente);
        assertEquals(1, todosUsuarios.size());
    }

    @Test
    void testCadastrarUsuarioTipoInvalido() {
        Usuario usuario = dono.cadastrarUsuario(
            "Teste", 
            "teste@empresa.com", 
            "senha456", 
            "administrador", 
            unidade1, 
            todosUsuarios
        );

        assertNull(usuario);
        assertEquals(1, todosUsuarios.size());
    }

    @Test
    void testGeracaoDeIdUnico() {
        Usuario vendedor1 = dono.cadastrarUsuario(
            "Vendedor 1", "v1@empresa.com", "senha", "vendedor", unidade1, todosUsuarios
        );
        Usuario vendedor2 = dono.cadastrarUsuario(
            "Vendedor 2", "v2@empresa.com", "senha", "vendedor", unidade2, todosUsuarios
        );

        assertNotNull(vendedor1);
        assertNotNull(vendedor2);
        assertNotEquals(vendedor1.getId(), vendedor2.getId());
        assertTrue(vendedor1.getId() > dono.getId());
        assertTrue(vendedor2.getId() > vendedor1.getId());
    }
}
