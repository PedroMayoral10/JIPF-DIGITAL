package es.jipfdigital.library.dominio.controladores;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import es.jipfdigital.library.dominio.entidades.*;
import es.jipfdigital.library.persistencia.ClienteDAO;
import es.jipfdigital.library.persistencia.RepartidorDAO;
import es.jipfdigital.library.persistencia.RestauranteDAO;

import java.util.Optional;

public class GestorUsuarioTest {

    @Mock
    private ClienteDAO clienteDAO;

    @Mock
    private RestauranteDAO restauranteDAO;

    @Mock
    private RepartidorDAO repartidorDAO;

    @InjectMocks
    private GestorUsuario gestorUsuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSubmitClienteExistente() {
        Model model = new ConcurrentModel();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("cliente");
        usuario.setNombre("usuario1");
        usuario.setPass("password123");
        Cliente cliente = new Cliente("cliente", "usuario1", "password123", "apellidos", "12345678A");

        when(clienteDAO.findById("cliente")).thenReturn(Optional.of(cliente));
        when(restauranteDAO.findById("cliente")).thenReturn(Optional.empty());
        when(repartidorDAO.findById("cliente")).thenReturn(Optional.empty());

        String result = gestorUsuario.loginSubmit(usuario, model);

        assertEquals("redirect:/menucliente/cliente", result);
    }

    @Test
    void testLoginSubmitRestauranteExistente() {
        Model model = new ConcurrentModel();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("restaurante");
        usuario.setNombre("usuario1");
        usuario.setPass("password123");
        Direccion direccion = new Direccion("12345","Calle","2","nada","test");
        Restaurante restaurante = new Restaurante("restaurante", "usuario1", "password123", direccion, "123456");
        
        when(clienteDAO.findById("restaurante")).thenReturn(Optional.empty());
        when(restauranteDAO.findById("restaurante")).thenReturn(Optional.of(restaurante));
        when(repartidorDAO.findById("restaurante")).thenReturn(Optional.empty());

        String result = gestorUsuario.loginSubmit(usuario, model);

        assertEquals("redirect:/menurestaurante/restaurante", result);
    }

    @Test
    void testLoginSubmitRepartidorExistente() {
        Model model = new ConcurrentModel();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("repartidor");
        usuario.setNombre("usuario1");
        usuario.setPass("password123");
        Repartidor repartidor = new Repartidor("repartidor", "usuario1", "password123", "apellidos", "123456");
        
        when(clienteDAO.findById("repartidor")).thenReturn(Optional.empty());
        when(restauranteDAO.findById("repartidor")).thenReturn(Optional.empty());
        when(repartidorDAO.findById("repartidor")).thenReturn(Optional.of(repartidor));

        String result = gestorUsuario.loginSubmit(usuario, model);

        assertEquals("redirect:/menurepartidor/repartidor", result);
    }

    @Test
    void testLoginSubmitContrasenaIncorrectaCliente() {
        Model model = new ConcurrentModel();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("cliente");
        usuario.setNombre("usuario1");
        usuario.setPass("incorrecta");
        Cliente cliente = new Cliente("cliente", "usuario1", "password123", "apellidos", "12345678A");

        when(clienteDAO.findById("cliente")).thenReturn(Optional.of(cliente));
        when(restauranteDAO.findById("cliente")).thenReturn(Optional.empty());
        when(repartidorDAO.findById("cliente")).thenReturn(Optional.empty());

        String result = gestorUsuario.loginSubmit(usuario, model);

        assertEquals("login", result);
        assertEquals("Contraseña incorrecta, pruebe otra vez", model.getAttribute("error"));
    }
    
    @Test
    void testLoginSubmitContrasenaIncorrectaRestaurante() {
        Model model = new ConcurrentModel();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("restaurante1");
        usuario.setNombre("restaurante1");
        usuario.setPass("incorrecta");
        Direccion direccion = new Direccion("12345","Calle","2","nada","test");
        Restaurante restaurante = new Restaurante("restaurante1","restaurante1","password123",direccion , "3459345394");

        when(clienteDAO.findById("restaurante1")).thenReturn(Optional.empty());
        when(restauranteDAO.findById("restaurante1")).thenReturn(Optional.of(restaurante));
        when(repartidorDAO.findById("restaurante1")).thenReturn(Optional.empty());

        String result = gestorUsuario.loginSubmit(usuario, model);

        assertEquals("login", result);
        assertEquals("Contraseña incorrecta, pruebe otra vez", model.getAttribute("error"));
    }
    
    @Test
    void testLoginSubmitContrasenaIncorrectaRepartidor() {
        Model model = new ConcurrentModel();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("repartidor");
        usuario.setNombre("usuario1");
        usuario.setPass("incorrecta");
        Repartidor repartidor = new Repartidor("repartidor", "usuario1", "password123", "apellidos", "123456");

        when(clienteDAO.findById("repartidor")).thenReturn(Optional.empty());
        when(restauranteDAO.findById("repartidor")).thenReturn(Optional.empty());
        when(repartidorDAO.findById("repartidor")).thenReturn(Optional.of(repartidor));

        String result = gestorUsuario.loginSubmit(usuario, model);

        assertEquals("login", result);
        assertEquals("Contraseña incorrecta, pruebe otra vez", model.getAttribute("error"));
    }
    
    @Test
    void testLoginSubmitUsuarioNoExistente() {
        Model model = new ConcurrentModel();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("usuario2");
        usuario.setNombre("usuario2");
        usuario.setPass("password123");

        when(clienteDAO.findById("usuario2")).thenReturn(Optional.empty());
        when(restauranteDAO.findById("usuario2")).thenReturn(Optional.empty());
        when(repartidorDAO.findById("usuario2")).thenReturn(Optional.empty());

        String result = gestorUsuario.loginSubmit(usuario, model);

        assertEquals("login", result);
        assertEquals("El usuario no existe, pruebe otra vez", model.getAttribute("error"));
    }

    @Test
    void testLoginSubmitUsuarioNulo() {
        Model model = new ConcurrentModel();
        Usuario usuario = null;

        String result = gestorUsuario.loginSubmit(usuario, model);
        assertEquals("login", result);
        assertEquals("El usuario no existe, pruebe otra vez", model.getAttribute("error"));
    }

    @Test
    void testRegistroSubmitRolNulo() {
        Model model = new ConcurrentModel();

        Usuario usuario = new Usuario();

        String result = gestorUsuario.registroSubmit(
                usuario, null, null, null, null, null, null, null, null, null, null, 0, model);

        assertEquals("registro", result);
        assertEquals("Ingresa un tipo de usuario", model.getAttribute("rolNulo"));
    }

    @Test
    void testRegistroSubmitCliente() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("cliente1");
        usuario.setNombre("cliente1");
        usuario.setPass("test");
        Model model = new ConcurrentModel();

        String result = gestorUsuario.registroSubmit(
                usuario, "Fernández", "23405234", null, null, null, null, null, null, null, null, 1, model);

        verify(clienteDAO, times(1)).save(any(Cliente.class));
        assertEquals("login", result);
    }

    @Test
    void testRegistroSubmitRolRestaurante() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario("restaurante1");
        usuario.setNombre("restaurante1");
        usuario.setPass("test");
        Model model = new ConcurrentModel();

        String result = gestorUsuario.registroSubmit(
                usuario, null, null, "3459345394", "45677", "Jacinto Benavente", "3", "Bajo", "Madrid", null, null, 2,
                model);

        verify(restauranteDAO, times(1)).save(any(Restaurante.class));
        assertEquals("login", result);
    }

    @Test
    void testRegistroSubmitRolRepartidor() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("repartidor1");
        usuario.setNombre("repartidor1");
        usuario.setPass("test");
        Model model = new ConcurrentModel();

        String result = gestorUsuario.registroSubmit(
                usuario, null, null, null, null, null, null, null, null, "López", "45346394", 3, model);

        verify(repartidorDAO, times(1)).save(any(Repartidor.class));
        assertEquals("login", result);
    }
    
    @Test
    void testRegistroSubmitClienteCampoVacio() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("cliente1");
        usuario.setNombre("cliente1");
        usuario.setPass("test");
        Model model = new ConcurrentModel();

        String result = gestorUsuario.registroSubmit(
                usuario, "", "23405234", null, null, null, null, null, null, null, null, 1, model);

        assertEquals("registro", result);
    }

    @Test
    void testRegistroSubmitRolRestauranteCampoVacio() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario("restaurante1");
        usuario.setNombre("restaurante1");
        usuario.setPass("test");
        Model model = new ConcurrentModel();

        String result = gestorUsuario.registroSubmit(
                usuario, null, null, "", "", "Jacinto Benavente", "3", "Bajo", "Madrid", null, null, 2,
                model);

        assertEquals("registro", result);
    }

    @Test
    void testRegistroSubmitRolRepartidorCampoVacio() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario("repartidor1");
        usuario.setNombre("repartidor1");
        usuario.setPass("test");
        Model model = new ConcurrentModel();

        String result = gestorUsuario.registroSubmit(
                usuario, null, null, null, null, null, null, null, null, "", "45346394", 3, model);

        assertEquals("registro", result);
    }
    
    @Test
    public void testUsuarioClienteYaExiste() {
    	
    	Usuario usuario = new Usuario ();
    	usuario = new Usuario();
        usuario.setIdUsuario("testUser");
    		
        Model model = mock(Model.class); 
        when(clienteDAO.findById(usuario.getIdUsuario())).thenReturn(Optional.of(new Cliente()));

        String result = gestorUsuario.registroSubmit(usuario, "", "", null, null, null, null, null, null, null, null, 1, model);

        verify(model).addAttribute("error", "El nombre de usuario ya existe"); 
        assertEquals("registro", result);
    }

    @Test
    public void testRestauranteYaExiste() {
    	
    	Usuario usuario = new Usuario ();
    	usuario = new Usuario();
        usuario.setIdUsuario("testUser");
    	  	
        Model model = mock(Model.class); 
        when(restauranteDAO.findById(usuario.getIdUsuario())).thenReturn(Optional.of(new Restaurante()));

        String result = gestorUsuario.registroSubmit(usuario, null, null, null, null, null, null, null, null, null, null, 2, model);

        verify(model).addAttribute("error", "El nombre de usuario ya existe"); 
        assertEquals("registro", result);
    }

    @Test
    public void testRepartidorYaExiste() {
    	
    	Usuario usuario = new Usuario ();
    	usuario = new Usuario();
        usuario.setIdUsuario("testUser");
        
        Model model = mock(Model.class); 
        when(repartidorDAO.findById(usuario.getIdUsuario())).thenReturn(Optional.of(new Repartidor()));

        String result = gestorUsuario.registroSubmit(usuario, null, null, null, null, null, null, null, null, null, null, 3, model);

        verify(model).addAttribute("error", "El nombre de usuario ya existe"); 
        assertEquals("registro", result);
    }
    
    
    
    

}
