package es.jipfdigital.library.dominio.controladores;

import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import es.jipfdigital.library.dominio.entidades.CartaMenu;
import es.jipfdigital.library.dominio.entidades.Cliente;
import es.jipfdigital.library.dominio.entidades.Direccion;
import es.jipfdigital.library.dominio.entidades.ItemMenu;
import es.jipfdigital.library.dominio.entidades.MetodoPago;
import es.jipfdigital.library.dominio.entidades.Pedido;
import es.jipfdigital.library.dominio.entidades.Repartidor;
import es.jipfdigital.library.dominio.entidades.Restaurante;
import es.jipfdigital.library.dominio.entidades.TipoItemMenu;
import es.jipfdigital.library.persistencia.CartaMenuDAO;
import es.jipfdigital.library.persistencia.ClienteDAO;
import es.jipfdigital.library.persistencia.DireccionDAO;
import es.jipfdigital.library.persistencia.ItemMenuDAO;
import es.jipfdigital.library.persistencia.PedidoDAO;
import es.jipfdigital.library.persistencia.RepartidorDAO;
import es.jipfdigital.library.persistencia.RestauranteDAO;

@ExtendWith(MockitoExtension.class)
class GestorPedidoTestTest {

    @Mock
    private RestauranteDAO restauranteDAO;

    @Mock
    private ClienteDAO clienteDAO;
    @Mock
    private RepartidorDAO repartidorDAO;

    @Mock
    private Model model;

    @Mock
    private CartaMenuDAO cartamenuDAO;
    @Mock
    private DireccionDAO direccionDAO;
    @Mock
    private PedidoDAO pedidoDAO;

    @Mock
    private ItemMenuDAO itemMenuDAO;
    @Mock
    private MetodoPago metodoPago;
    @Mock
    private Pedido pedido;

    @InjectMocks
    private GestorPedidos gestorPedidos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    void testDetalleRestauranteCP1() {

        String idCliente = "123";
        String idRestaurante = "456";

        when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.empty());
        when(clienteDAO.findById(idCliente)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            gestorPedidos.detalleRestaurante(idCliente, idRestaurante, model);
        });

        assertEquals("Restaurante o Cliente no encontrado", exception.getMessage());

        verify(restauranteDAO).findById(idRestaurante);

        verify(clienteDAO, never()).findById(idCliente);

        verify(cartamenuDAO, never()).findAllByRestauranteId(idRestaurante);
    }

    @Test
    void testDetalleRestauranteCP2() {

        String idCliente = "123";
        String idRestaurante = "456";

        Restaurante mockRestaurante = new Restaurante();
        Cliente mockCliente = new Cliente();
        List<CartaMenu> mockMenus = Arrays.asList(new CartaMenu(), new CartaMenu());

        when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.of(mockRestaurante));
        when(clienteDAO.findById(idCliente)).thenReturn(Optional.of(mockCliente));
        when(cartamenuDAO.findAllByRestauranteId(idRestaurante)).thenReturn(mockMenus);

        String viewName = gestorPedidos.detalleRestaurante(idCliente, idRestaurante, model);

        assertEquals("realizarpedido", viewName);

        verify(restauranteDAO).findById(idRestaurante);
        verify(clienteDAO).findById(idCliente);
        verify(cartamenuDAO).findAllByRestauranteId(idRestaurante);

        verify(model).addAttribute("menus", mockMenus);
        verify(model).addAttribute("restaurante", mockRestaurante);
        verify(model).addAttribute("cliente", mockCliente);
    }

    @Test
    public void testSubmitPagoCP1() {

        String idCliente = "12345";
        String idRestaurante = "67890";
        String codigoPostal = "45600";

        String calle = "Calle Alcatraz";
        String numero = "98";
        String complemento = "A";
        String municipio = "Hervás";

        Restaurante restaurante = new Restaurante();
        Cliente cliente = new Cliente();

        Repartidor repartidor = new Repartidor();
        repartidor.setServicios(new ArrayList<>());

        when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(clienteDAO.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(repartidorDAO.findAll()).thenReturn(Arrays.asList(repartidor));

        // Llamada al método
        String resultado = gestorPedidos.submitPago(idCliente, idRestaurante, model,
                codigoPostal, MetodoPago.PAYPAL, calle, numero, complemento, municipio);

        // Verificaciones
        assertEquals("redirect:/confirmacionpago/" + idCliente, resultado);
        verify(model).addAttribute("mensajeExito", "El pago se ha realizado correctamente.");
        verify(pedidoDAO).save(any(Pedido.class));
        verify(direccionDAO).save(any(Direccion.class));
    }

    @Test
    public void testSubmitPagoCP2() {

        // Parametros de entrada (caso CP2)
        String idCliente = "54321";
        String idRestaurante = "98765";
        String codigoPostal = null;
        String calle = "Calle Falsa";
        String numero = "123";
        String complemento = null;
        String municipio = null;

        // Mocks
        Restaurante restaurante = new Restaurante();
        Cliente cliente = new Cliente();

        Repartidor repartidor = new Repartidor();
        repartidor.setServicios(new ArrayList<>());

        when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(clienteDAO.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(repartidorDAO.findAll()).thenReturn(Arrays.asList(repartidor));

        String resultado = gestorPedidos.submitPago(idCliente, idRestaurante, model,
                codigoPostal, MetodoPago.CREDIT_CARD, calle, numero, complemento, municipio);

        assertEquals("redirect:/confirmacionpago/" + idCliente, resultado);
        verify(model).addAttribute("mensajeExito", "El pago se ha realizado correctamente.");
        verify(pedidoDAO).save(any(Pedido.class));
        verify(direccionDAO).save(any(Direccion.class));
    }

    @Test
    void testRealizarPagoCP1() {

        Map<String, String> params = new HashMap<>();
        String idCliente = "123";
        String idRestaurante = "456";

        String viewName = gestorPedidos.realizarPago(idCliente, idRestaurante, params, model);

        verify(model).addAttribute("itemsPedidos", Collections.emptyList());
        verify(model).addAttribute("idCliente", idCliente);
        verify(model).addAttribute("idRestaurante", idRestaurante);
        verify(model).addAttribute("precioTotal", 0.0);

        assertEquals("realizarpago", viewName);
    }

    @Test
    void testRealizarPagoCP2() {
        // Mock de los items pedidos
        ItemMenu item1 = new ItemMenu();
        item1.setId(1L);
        item1.setNombre("Pizza");
        item1.setTipo(TipoItemMenu.COMIDA);
        item1.setPrecio(7.50);
        item1.setCartaMenu(null);

        ItemMenu item2 = new ItemMenu();
        item2.setId(2L);
        item2.setNombre("Pasta");
        item2.setTipo(TipoItemMenu.COMIDA);
        item2.setPrecio(7.50);
        item2.setCartaMenu(null);

        Map<String, String> params = new HashMap<>();
        params.put("id0", "1");
        params.put("id1", "2");

        when(itemMenuDAO.findById(1L)).thenReturn(Optional.of(item1));
        when(itemMenuDAO.findById(2L)).thenReturn(Optional.of(item2));

        Model model = new ConcurrentModel();

        String result = gestorPedidos.realizarPago("cliente1", "restaurante1", params, model);

        assertEquals("realizarpago", result);

        @SuppressWarnings("unchecked")
        List<ItemMenu> itemsPedidos = (List<ItemMenu>) model.getAttribute("itemsPedidos");
        assertNotNull(itemsPedidos);
        assertEquals(2, itemsPedidos.size());
        assertTrue(itemsPedidos.contains(item1));
        assertTrue(itemsPedidos.contains(item2));

        Double precioTotal = (Double) model.getAttribute("precioTotal");
        assertNotNull(precioTotal);
        assertEquals(15.0, precioTotal, 0.01);
    }

    @Test
    void testProcesarPedidoCP1() {

        String idCliente = "123";
        String idRestaurante = "restaurante1";
        Long idMenu = 1L;

        Direccion direccion = new Direccion("28001", "Calle Falsa", "123", "Apartamento 1A", "Madrid");

        Restaurante restaurante = new Restaurante(idRestaurante, "Restaurante de Prueba", "pass123", direccion,
                "CIF12345");

        ItemMenu item1 = new ItemMenu();
        item1.setId(1L);
        item1.setNombre("Pizza");
        item1.setPrecio(8.50);

        ItemMenu item2 = new ItemMenu();
        item2.setId(2L);
        item2.setNombre("Pasta");
        item2.setPrecio(7.50);

        CartaMenu cartaMenu = new CartaMenu(restaurante, "Menú Especial");
        cartaMenu.setId(idMenu);
        cartaMenu.setItems(List.of(item1, item2));
        restaurante.setCartasMenu(List.of(cartaMenu));

        List<CartaMenu> menus = List.of(cartaMenu);
        when(cartamenuDAO.findById(idMenu)).thenReturn(Optional.of(cartaMenu));
        when(cartamenuDAO.findAll()).thenReturn(menus);

        RedirectAttributes flashAttributes = mock(RedirectAttributes.class);

        String result = gestorPedidos.procesarPedido(idCliente, idRestaurante, new ConcurrentModel(), idMenu,
                flashAttributes);

        assertEquals("redirect:/realizarpedido/123/restaurante1", result);
        verify(flashAttributes).addFlashAttribute("items", cartaMenu.getItems());
        verify(flashAttributes).addFlashAttribute("menus", menus);
        verify(flashAttributes).addFlashAttribute("menu_seleccionado", idMenu);
    }

    @Test
    void testProcesarPedidoCP2() {

        String idCliente = "123";
        String idRestaurante = "456";
        Long idMenu = null;
        RedirectAttributes flashAttributes = mock(RedirectAttributes.class);

        when(cartamenuDAO.findById(idMenu)).thenThrow(new IllegalArgumentException("El idMenu no puede ser nulo"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorPedidos.procesarPedido(idCliente, idRestaurante, new ConcurrentModel(), idMenu, flashAttributes);
        });

        assertEquals("El idMenu no puede ser nulo", exception.getMessage());
    }

    @Test
    void testCalcularRepartidorOptimo() {
        // Crear mock de Repartidores
        Repartidor repartidor1 = new Repartidor();
        repartidor1.setServicios(new ArrayList<>()); // 0 servicios
        repartidor1.setEficiencia(80);

        Repartidor repartidor2 = new Repartidor();
        repartidor2.setServicios(new ArrayList<>());
        repartidor2.setEficiencia(85); // Eficiencia mayor

        Repartidor repartidor3 = new Repartidor();
        repartidor3.setServicios(new ArrayList<>());
        repartidor3.setEficiencia(75); // Eficiencia menor

        // Simular el DAO de repartidores
        when(repartidorDAO.findAll()).thenReturn(Arrays.asList(repartidor1, repartidor2, repartidor3));

        // Llamar al método calcularRepartidorOptimo
        Repartidor repartidorOptimo = gestorPedidos.calcularRepartidorOptimo();

        // Verificar que el repartidor óptimo seleccionado sea el correcto
        assertEquals(repartidor2, repartidorOptimo,
                "El repartidor óptimo debería ser el de mayor eficiencia con el mismo número de servicios.");
    }

    @Test
void testProcesarPedidoConRepartidorOptimo() {
    // Parámetros de entrada
    String idCliente = "123";
    String idRestaurante = "456";
    Long idMenu = 1L; // Caso con un idMenu válido

    // Crear un mock del repartidor óptimo
    Repartidor repartidorOptimo = new Repartidor();
    repartidorOptimo.setServicios(new ArrayList<>());
    repartidorOptimo.setEficiencia(90); // Alta eficiencia

    // Configurar mock para encontrar el repartidor óptimo de forma leniente
    lenient().when(repartidorDAO.findAll()).thenReturn(Arrays.asList(new Repartidor(), repartidorOptimo));
    
    // Configurar mock del cartamenuDAO para encontrar un menú
    CartaMenu cartaMenu = new CartaMenu();
    cartaMenu.setId(1L);
    lenient().when(cartamenuDAO.findById(idMenu)).thenReturn(Optional.of(cartaMenu));

    // Simular comportamiento de Redirección de atributos
    RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

    // Ejecutar el método
    String resultado = gestorPedidos.procesarPedido(idCliente, idRestaurante, new ConcurrentModel(), idMenu, redirectAttributes);

    // Verificar el resultado esperado
    assertEquals("redirect:/realizarpedido/123/456", resultado);
}

}
