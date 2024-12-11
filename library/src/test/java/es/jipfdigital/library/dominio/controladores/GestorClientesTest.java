package es.jipfdigital.library.dominio.controladores;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import es.jipfdigital.library.dominio.entidades.Cliente;
import es.jipfdigital.library.dominio.entidades.Restaurante;
import es.jipfdigital.library.persistencia.ClienteDAO;
import es.jipfdigital.library.persistencia.RestauranteDAO;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;


class GestorClientesTest {

	@Mock
    private ClienteDAO clienteDAO;

    @Mock
    private RestauranteDAO restauranteDAO;

    @Mock
    private Model model;

    @InjectMocks
    private GestorClientes gestorClientes; // Tu clase que contiene el método favorito
	
	public GestorClientesTest() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
	public void testEliminarFavorito() {
		
		// Preparar datos de prueba
        String idCliente = "1";
        String idRestaurante = "100";
        
        Restaurante restaurante = new Restaurante();
        restaurante.setIdUsuario(idRestaurante);
        
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(idCliente);
        
        // Simular favoritos actuales
        cliente.getFavoritos().add(restaurante);

        // Configurar mocks
        when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(clienteDAO.findById(idCliente)).thenReturn(Optional.of(cliente));

        // Ejecutar el método
        String resultado = gestorClientes.favorito(model, idCliente, idRestaurante);

        // Verificar comportamiento
        verify(clienteDAO, times(1)).save(cliente);
        assert !cliente.getFavoritos().contains(restaurante); // Restaurante fue eliminado de favoritos
        assert resultado.equals("redirect:/listarestaurantes/" + idCliente);
		
		
	}
	
	@Test
	public void testAnyadirFavorito() {
		
		// Preparar datos de prueba
        String idCliente = "1";
        String idRestaurante = "100";
        
        Restaurante restaurante = new Restaurante();
        restaurante.setIdUsuario(idRestaurante);
        
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(idCliente);

        // Configurar mocks
        when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(clienteDAO.findById(idCliente)).thenReturn(Optional.of(cliente));

        // Ejecutar el método
        String resultado = gestorClientes.favorito(model, idCliente, idRestaurante);

        // Verificar comportamiento
        verify(clienteDAO, times(1)).save(cliente);
        assert cliente.getFavoritos().contains(restaurante); // Restaurante fue anyadido a favoritos
        assert resultado.equals("redirect:/listarestaurantes/" + idCliente);
		
		
	}
	
	
	@Test
	public void testAnyadirFavoritoNull() {
		
		// Preparar datos de prueba
        String idCliente = "1";
        
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(idCliente);
        

        // Configurar mocks
        when(clienteDAO.findById(idCliente)).thenReturn(Optional.of(cliente));

        // Ejecutar el método
        String resultado = gestorClientes.favorito(model, idCliente, null);

        // Verificar comportamiento
        assertTrue(cliente.getFavoritos().isEmpty()); // Restaurante fue eliminado de favoritos
        assert resultado.equals("redirect:/listarestaurantes/" + idCliente);
        verify(clienteDAO, never()).save(cliente);
		
	}
	
	
	
	
	
	//Test correspondiente si hay restaurantes en la base de datos
	
	@Test
    public void testListaRestauranteHayRestaurantes() {
        // Datos de prueba
        String idCliente = "1";
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(idCliente);

        //Mockeamos
        
        List<Restaurante> restaurantes = new ArrayList<>();
        String idRestaurante1 = "restaurante1";
        String idRestaurante2 = "restaurante2";
        
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setIdUsuario(idRestaurante1);
        Restaurante restaurante2 = new Restaurante();
        restaurante2.setIdUsuario(idRestaurante2);
        restaurantes.add(restaurante1);
        restaurantes.add(restaurante2);

        // Configuración de los mocks
        when(clienteDAO.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(restauranteDAO.findAll()).thenReturn(restaurantes);

        // Llamada al método
        String resultado = gestorClientes.listaRestaurante(model, idCliente);

        // Verificación de los resultados
        verify(model).addAttribute("restaurantes", restaurantes);
        verify(model).addAttribute("cliente", cliente);
        assertEquals("listarestaurantes", resultado);
    }

	//Test correspondiente si no hay restaurantes en la base de datos.
	
    @Test
    public void testListaRestauranteNoHayRestaurantes() {
        // Datos de prueba
        String idCliente = "1";
        Cliente cliente = new Cliente();
        cliente.setIdUsuario(idCliente);

        List<Restaurante> restaurantes = new ArrayList<>(); // Lista sin restaurantes

        // Configuración de los mocks
        when(clienteDAO.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(restauranteDAO.findAll()).thenReturn(restaurantes);

        // Llamada al método
        String vista = gestorClientes.listaRestaurante(model, idCliente);

        // Verificación de los resultados
        verify(model).addAttribute("restaurantes", restaurantes);
        verify(model).addAttribute("cliente", cliente);
        assertEquals("listarestaurantes", vista);
    }
	
	
	

}
