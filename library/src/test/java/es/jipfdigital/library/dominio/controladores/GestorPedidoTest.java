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

import es.jipfdigital.library.dominio.entidades.*;
import es.jipfdigital.library.persistencia.*;

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

    @Test
    void testDetalleRestauranteRestauranteNull() {

        String idCliente = "123";
        String idRestaurante = "456";

        when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            gestorPedidos.detalleRestaurante(idCliente, idRestaurante, model);
        });

        assertEquals("No value present", exception.getMessage());

        verify(restauranteDAO).findById(idRestaurante);
        verify(clienteDAO, never()).findById(idCliente);
        verify(cartamenuDAO, never()).findAllByRestauranteId(idRestaurante);
    }

    @Test
    void testDetalleRestauranteRestauranteYMenuValido() {

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
    public void testSubmitPagoValoresValidosNuevaDireccion() {

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
                codigoPostal, MetodoPago.PAYPAL, calle, numero, complemento, municipio, null);

        // Verificaciones
        assertEquals("redirect:/confirmacionpago/" + idCliente, resultado);
        verify(model).addAttribute("mensajeExito", "El pago se ha realizado correctamente.");
        verify(pedidoDAO).save(any(Pedido.class));
        verify(direccionDAO).save(argThat(direccion -> direccion.getCodigoPostal().equals(codigoPostal) &&
                direccion.getCalle().equals(calle) &&
                direccion.getNumero().equals(numero) &&
                direccion.getComplemento().equals(complemento) &&
                direccion.getMunicipio().equals(municipio)));
    }

    

    @Test
public void testSubmitPagoValoresNoNulosDireccionExistente() {
    String idCliente = "54321";
    String idRestaurante = "98765";
    Long idDireccion = 1L; // Dirección no nula, ahora tiene un valor válido

    // Mocks de la base de datos
    Restaurante restaurante = new Restaurante();
    Cliente cliente = new Cliente();

    // Creamos una dirección existente asociada al cliente
    Direccion direccion = new Direccion("45600", "Calle Alcatraz", "98", "A", "Hervás");
    direccion.setId(idDireccion);  // Asignamos un ID válido a la dirección existente

    List<Direccion> direcciones = new ArrayList<>();
    direcciones.add(direccion);
    cliente.setDirecciones(direcciones);

    Repartidor repartidor = new Repartidor();
    repartidor.setServicios(new ArrayList<>());

    // Simula la base de datos
    when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.of(restaurante));
    when(clienteDAO.findById(idCliente)).thenReturn(Optional.of(cliente));
    

    
    when(repartidorDAO.findAll()).thenReturn(Arrays.asList(repartidor));

    // Llamada al método
    String resultado = gestorPedidos.submitPago(idCliente, idRestaurante, model,
            "45600", MetodoPago.CREDIT_CARD, "Calle Alcatraz", "98", "A", "Hervás", idDireccion);

    // Verificaciones
    assertEquals("redirect:/confirmacionpago/" + idCliente, resultado);
    verify(model).addAttribute("mensajeExito", "El pago se ha realizado correctamente.");
    verify(pedidoDAO).save(any(Pedido.class));
    verify(direccionDAO, never()).save(any(Direccion.class)); // No se debe guardar la dirección, ya existe
}

    @Test
    public void testSubmitPagoSinRepartidorOptimo() {
        // Preparación de datos
        String idCliente = "12345";
        String idRestaurante = "67890";
        String codigoPostal = "45600";
        String calle = "Calle Alcatraz";
        String numero = "98";
        String complemento = "A";
        String municipio = "Hervás";
        Long idDireccion = null; // Dirección nula

        Restaurante restaurante = new Restaurante();
        Cliente cliente = new Cliente();

        // No hay repartidores disponibles (repartidor óptimo es null)
        when(repartidorDAO.findAll()).thenReturn(Collections.emptyList()); // No hay repartidores disponibles
        when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(clienteDAO.findById(idCliente)).thenReturn(Optional.of(cliente));

        // Llamada al método
        // Mocks de la base de datos

        String resultado = gestorPedidos.submitPago(idCliente, idRestaurante, model,
                codigoPostal, MetodoPago.PAYPAL, calle, numero, complemento, municipio, idDireccion);

        // Verificaciones
        assertEquals("realizarpago", resultado); // El flujo debería redirigir a realizarpago si no hay repartidor
        verify(model).addAttribute("sinRepartidor", true); // Verifica que el atributo sinRepartidor sea true
        verify(pedidoDAO, never()).save(any(Pedido.class)); // No se debe guardar el pedido si no hay repartidor
    }

    
    @Test
    void testRealizarPagoListaVacia() {

        Map<String, String> params = new HashMap<>();

        String idRestaurante = "456";
        Cliente cliente = new Cliente("cliente", "usuario1", "password123", "apellidos", "12345678A");
        when(clienteDAO.findById("cliente")).thenReturn(Optional.of(cliente));

        String result = gestorPedidos.realizarPago("cliente", idRestaurante, params, model);

        verify(model).addAttribute("itemsPedidos", Collections.emptyList());
        verify(model).addAttribute("idCliente", "cliente");
        verify(model).addAttribute("idRestaurante", idRestaurante);
        verify(model).addAttribute("precioTotal", 0.0);

        assertEquals("realizarpago", result);
    }

    @Test
    void testRealizarPagoListaValida() {
        Cliente cliente = new Cliente("cliente", "usuario1", "password123", "apellidos", "12345678A");
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
        when(clienteDAO.findById("cliente")).thenReturn(Optional.of(cliente));
        Model model = new ConcurrentModel();

        String result = gestorPedidos.realizarPago("cliente", "restaurante1", params, model);

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
    void testProcesarPedidoMenuValido() {

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
    void testProcesarPedidoMenuNull() {

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
    void testCalcularRepartidorOptimoValido() {

        Repartidor repartidor1 = new Repartidor();
        repartidor1.setServicios(new ArrayList<>());
        repartidor1.setEficiencia(80);

        Repartidor repartidor2 = new Repartidor();
        repartidor2.setServicios(new ArrayList<>());
        repartidor2.setEficiencia(85);

        Repartidor repartidor3 = new Repartidor();
        repartidor3.setServicios(new ArrayList<>());
        repartidor3.setEficiencia(75);

        when(repartidorDAO.findAll()).thenReturn(Arrays.asList(repartidor1, repartidor2, repartidor3));

        Repartidor repartidorOptimo = gestorPedidos.calcularRepartidorOptimo();

        assertEquals(repartidor2, repartidorOptimo,
                "El repartidor óptimo debería ser el de mayor eficiencia con el mismo número de servicios.");
    }

    @Test
    void testCalcularRepartidorOptimoNull() {

        when(repartidorDAO.findAll()).thenReturn(null);

        try {
            gestorPedidos.calcularRepartidorOptimo();
            fail("Debería lanzar una excepción NullPointerException");
        } catch (NullPointerException e) {

            assertNotNull(e);
        }
    }

    @Test
    void testConfirmacionPagoValido() {

        String idCliente = "12345";

        Model model = new ConcurrentModel();

        String resultado = gestorPedidos.confirmacionpago(idCliente, model);

        assertEquals(idCliente, model.getAttribute("idCliente"));

        assertEquals("confirmacionPago", resultado);
    }

    @Test
    void testConfirmacionPagoNull() {

        String idCliente = null;

        String resultado = gestorPedidos.confirmacionpago(idCliente, model);

        assertNull(model.getAttribute("idCliente"), "El atributo 'idCliente' debería ser null");

        assertEquals("confirmacionPago", resultado, "La vista devuelta debería ser 'confirmacionPago'");
    }

    @Test
    void testMostrarPedidosValido() {

        String idCliente = "123";
        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        pedidos.add(pedido1);
        pedidos.add(pedido2);

        when(pedidoDAO.findPedidosByCliente(idCliente)).thenReturn(pedidos);

        String view = gestorPedidos.mostrarPedidos(idCliente, model);

        verify(model).addAttribute("pedidos", pedidos);
        verify(model).addAttribute("idCliente", idCliente);

        assertEquals("pedidoscliente", view);
    }

    @Test
    void testMostrarPedidosNull() {

        String idCliente = null;

        when(pedidoDAO.findPedidosByCliente(idCliente))
                .thenThrow(new IllegalArgumentException("El idCliente no puede ser nulo"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorPedidos.mostrarPedidos(idCliente, model);
        });

        assertEquals("El idCliente no puede ser nulo", exception.getMessage());
    }

}