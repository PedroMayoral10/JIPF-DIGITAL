package es.jipfdigital.library.dominio.controladores;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import es.jipfdigital.library.dominio.entidades.EstadoPedido;
import es.jipfdigital.library.dominio.entidades.Pedido;
import es.jipfdigital.library.dominio.entidades.Repartidor;
import es.jipfdigital.library.dominio.entidades.ServicioEntrega;
import es.jipfdigital.library.persistencia.PedidoDAO;
import es.jipfdigital.library.persistencia.RepartidorDAO;
import es.jipfdigital.library.persistencia.ServicioEntregaDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class GestorRepartidorTest {

	@Mock
    private RepartidorDAO repartidorDAO;

	@Mock
    private PedidoDAO pedidoDAO;
	
	@Mock
    private ServicioEntregaDAO servicioEntregaDAO;
	
	
    @Mock
    private Model model;
    
    @Mock
    private Repartidor repartidor;
    

    @InjectMocks
    private GestorRepartidor controller; // Clase que contiene el método mostrarPedidos

    public GestorRepartidorTest() {
        MockitoAnnotations.openMocks(this);
    }
	
	
	@Test
    void testMostrarPedidosRepartidorValidoConServicios() {
        
		//Creamos el repartidor
        String idRepartidor = "1";
        Repartidor repartidor = new Repartidor(idRepartidor, "Juan", "pass123", "Pérez", "12345678A");
        
        //Creamos servicios
        ServicioEntrega servicio1 = new ServicioEntrega();
        servicio1.setFechaRecepcion(LocalDate.now().minusDays(2));
        servicio1.setFechaEntrega(LocalDate.now());
        servicio1.setRepartidor(repartidor);
        ServicioEntrega servicio2 = new ServicioEntrega();
        servicio2.setFechaRecepcion(LocalDate.now().minusDays(3));
        servicio2.setFechaEntrega(LocalDate.now().minusDays(1));
        servicio2.setRepartidor(repartidor);
        
        //Coger los servicios que tiene el repartidor asignado
        Collection<ServicioEntrega> servicios = repartidor.getServicios();

        //Simular el comportamiento del DAO
        when(repartidorDAO.findById(idRepartidor)).thenReturn(Optional.of(repartidor));

        //Mostramos los pedidos que tiene el repartidor asignado
        String resultado = controller.mostrarPedidos(idRepartidor, model);

        //Verificamos que se muestran bien
        assertEquals("pedidosrepartidor", resultado);
        verify(model).addAttribute("idRepartidor", idRepartidor);
        verify(model).addAttribute("servicios", servicios);
    }
	
	@Test
    void testMostrarPedidosServiciosNull() {
        
		//Creamos el repartidor, en este caso,no existirá un repartidor con ID 1.
        String idRepartidor = "1";
        
        //Simular el comportamiento del DAO (en este caso, no encuentra repartidor)
        when(repartidorDAO.findById(idRepartidor)).thenReturn(Optional.of(repartidor));

        //Mostramos los pedidos que tiene el repartidor asignado
        String resultado = controller.mostrarPedidos(idRepartidor, model);

        //Verificamos que se muestran bien
        assertEquals("pedidosrepartidor", resultado); //Se devuelve la  vista de pedidosRepartidor
        verify(model).addAttribute("idRepartidor", idRepartidor);
        verify(model).addAttribute("servicios", Collections.emptyList()); //Utilizamos emptyList para decir que no tiene pedidos
        
    }
	
	
	
	@Test
	void testMenuRegistroRecogidaServiciosEstadoPagado() {
		
		// Preparar modelo y dependencias
	    Model model2 = new ConcurrentModel();

	    // Repartidor con ID "1"
	    String idRepartidor = "1";
	    Repartidor repartidor = mock(Repartidor.class);

	    // Servicios asociados al repartidor
	    ServicioEntrega servicioPagado = new ServicioEntrega();
	    Pedido pedidoPagado = new Pedido();
	    pedidoPagado.setEstado(EstadoPedido.PAGADO);
	    servicioPagado.setPedido(pedidoPagado);
	    ServicioEntrega servicioPagado2 = new ServicioEntrega();
	    pedidoPagado.setEstado(EstadoPedido.PAGADO);
	    servicioPagado2.setPedido(pedidoPagado);

	    List<ServicioEntrega> servicios = Arrays.asList(servicioPagado, servicioPagado2);
	    when(repartidor.getServicios()).thenReturn(servicios);
	    when(repartidorDAO.findById(idRepartidor)).thenReturn(Optional.of(repartidor));

	    // Llamar al método
	    String resultado = controller.menuRegistroRecogida(idRepartidor, model2);

	    // Validar resultados
	    assertEquals("registrar_recogida", resultado);
	
	}
	
	@Test
	void testMenuRegistroRecogidaSinServiciosEstadoPagado() {
		
		// Preparar modelo y dependencias
	    Model model2 = new ConcurrentModel();

	    // Repartidor con ID "1"
	    String idRepartidor = "1";
	    Repartidor repartidor = mock(Repartidor.class);

	    // Simular que los servicios del repartidor son null
	    when(repartidor.getServicios()).thenReturn(null);
	    when(repartidorDAO.findById(idRepartidor)).thenReturn(Optional.of(repartidor));

	    // Llamar al método
	    String resultado = controller.menuRegistroRecogida(idRepartidor, model2);

	    // Validar resultados
	    assertEquals("registrar_recogida", resultado);

	
	}
	
	
	
	
	@Test
	void testMenuRegistroEntrega() {
		
		// Preparar modelo y dependencias
	    Model model2 = new ConcurrentModel();

	    // Repartidor con ID "1"
	    String idRepartidor = "1";
	    Repartidor repartidor = mock(Repartidor.class);

	    // Servicios asociados al repartidor
	    ServicioEntrega servicioPagado = new ServicioEntrega();
	    Pedido pedidoPagado = new Pedido();
	    pedidoPagado.setEstado(EstadoPedido.RECOGIDO);
	    servicioPagado.setPedido(pedidoPagado);
	    ServicioEntrega servicioPagado2 = new ServicioEntrega();
	    pedidoPagado.setEstado(EstadoPedido.RECOGIDO);
	    servicioPagado2.setPedido(pedidoPagado);

	    List<ServicioEntrega> servicios = Arrays.asList(servicioPagado, servicioPagado2);
	    when(repartidor.getServicios()).thenReturn(servicios);
	    when(repartidorDAO.findById(idRepartidor)).thenReturn(Optional.of(repartidor));

	    // Llamar al método
	    String resultado = controller.menuRegistroEntrega(idRepartidor, model2);

	    // Validar resultados
	    assertEquals("registrar_entrega", resultado);
				
	}
	
	@Test
	void testMenuRegistroEntregaSinServicios() {
		
		// Preparar modelo y dependencias
	    Model model2 = new ConcurrentModel();

	    // Repartidor con ID "1"
	    String idRepartidor = "1";
	    Repartidor repartidor = mock(Repartidor.class);

	    // Simular que los servicios del repartidor son null
	    when(repartidor.getServicios()).thenReturn(null);
	    when(repartidorDAO.findById(idRepartidor)).thenReturn(Optional.of(repartidor));

	    // Llamar al método
	    String resultado = controller.menuRegistroEntrega(idRepartidor, model2);

	    // Validar resultados
	    assertEquals("registrar_entrega", resultado);
			
	}
	
	
	
	@Test
	void testSubmitRecogida() {
		
		// Preparar datos reales
        String idRepartidor = "1";
        Long idPedido = 123L;

        // Crear pedido y servicio. La fecha de entrega la crea el gestor y hay que verificarla mas tarde
        Pedido pedido = new Pedido();
        pedido.setId_pedido(idPedido);
        pedido.setEstado(EstadoPedido.PAGADO);  // El pedido está en estado PAGADO

        ServicioEntrega servicio = new ServicioEntrega();
        servicio.setPedido(pedido);
        pedido.setEntrega(servicio); // Establecer la relación entre Pedido y ServicioEntrega

        
        servicio.setRepartidor(repartidor);

        
        
        // Simulamos que se encuentra el pedido en la base de datos
        when(pedidoDAO.findById(idPedido)).thenReturn(Optional.of(pedido)); // Simulamos que el pedido se encuentra

        // Llamar al método
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        Model model = new ConcurrentModel();
        String resultado = controller.submitRecogida(idRepartidor, model, idPedido, redirectAttributes);

        // Validar resultados
        assertEquals("redirect:/registrar_recogida/" + idRepartidor, resultado); // Verificamos el return
        assertEquals(EstadoPedido.RECOGIDO, pedido.getEstado()); // Verificamos que el estado del pedido ha cambiado a recogido
        assertEquals(LocalDate.now(), servicio.getFechaRecepcion()); // Verificamos que se ha setteado fecha de recepción.

        // Verificar interacciones con los DAOs
        verify(pedidoDAO).save(pedido); // Verificamos que se guarda el pedido con el nuevo estado
        verify(servicioEntregaDAO).save(servicio); // Verificamos que se guarda el servicio con la nueva fecha de recepción

        // Verificamos el mensaje
        assertEquals("Recogida registrada con éxito", redirectAttributes.getFlashAttributes().get("exito"));
	
	}
	
	@Test
	void testSubmitRecogidaSinPedido() {
		
		String idRepartidor = "123";
		Long idPedido = 1L;

		// Configuración de los mocks
		when(pedidoDAO.findById(idPedido)).thenReturn(Optional.empty()); // Pedido no encontrado

		// Invocación del método
		RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
		Model model = new ConcurrentModel();
		String result = controller.submitRecogida(idRepartidor, model, idPedido, redirectAttributes);

		// Verificaciones
		verify(pedidoDAO, times(1)).findById(idPedido);
		verify(pedidoDAO, never()).save(any(Pedido.class)); // No debe guardar un pedido
		verify(servicioEntregaDAO, never()).save(any(ServicioEntrega.class)); // No debe guardar un servicio
		verify(redirectAttributes, times(1)).addFlashAttribute("error", "El pedido no existe.");
		assertEquals("redirect:/registrar_recogida/" + idRepartidor, result);
	
	}
		
	
	@Test
	void testSubmitRecogidaConPedidoSinServicio() {
	    String idRepartidor = "123";
	    Long idPedido = 1L;

	    // Crear un pedido válido pero con servicio de entrega null
	    Pedido pedido = new Pedido();
	    pedido.setId_pedido(idPedido);
	    pedido.setEstado(EstadoPedido.PAGADO);
	    pedido.setEntrega(null); // Servicio de entrega es null

	    // Configuración del mock
	    when(pedidoDAO.findById(idPedido)).thenReturn(Optional.of(pedido)); // Pedido encontrado

	    // Invocación del método
	    RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
	    Model model = new ConcurrentModel();
	    String result = controller.submitRecogida(idRepartidor, model, idPedido, redirectAttributes);

	    // Verificaciones
	    verify(pedidoDAO, times(1)).findById(idPedido); // Se busca el pedido
	    verify(pedidoDAO, times(1)).save(pedido); // El pedido cambia su estado a RECOGIDO
	    verify(servicioEntregaDAO, never()).save(any(ServicioEntrega.class)); // No se guarda un servicio
	    verify(redirectAttributes, times(1))
	            .addFlashAttribute("error", "El pedido no tiene un servicio de entrega asociado."); // Error esperado
	    assertEquals("redirect:/registrar_recogida/" + idRepartidor, result); // Redirección esperada
	}
		
	
	
	@Test
	void testSubmitEntrega() {
		
		String idRepartidor = "123";
	    Long idPedido = 1L;

	    // Configuración de mocks
	    Pedido pedido = new Pedido();
	    pedido.setId_pedido(idPedido);
	    pedido.setEstado(EstadoPedido.PAGADO);

	    ServicioEntrega servicio = new ServicioEntrega();
	    pedido.setEntrega(servicio);

	    Repartidor repartidor = new Repartidor();
	    repartidor.setIdUsuario(idRepartidor);

	    when(pedidoDAO.findById(idPedido)).thenReturn(Optional.of(pedido));
	    when(repartidorDAO.findById(idRepartidor)).thenReturn(Optional.of(repartidor));

	    // Invocación del método
	    RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
	    Model model = new ConcurrentModel();
	    String result = controller.submitEntrega(idRepartidor, model, idPedido, redirectAttributes);

	    // Verificaciones
	    verify(pedidoDAO, times(1)).findById(idPedido);
	    verify(pedidoDAO, times(1)).save(pedido);
	    assertEquals(EstadoPedido.ENTREGADO, pedido.getEstado());

	    verify(servicioEntregaDAO, times(1)).save(servicio);
	    assertNotNull(servicio.getFechaEntrega());

	    verify(repartidorDAO, times(1)).findById(idRepartidor);
	    verify(repartidorDAO, times(1)).save(repartidor);

	    verify(redirectAttributes, times(1))
	            .addFlashAttribute("exito", "Entrega registrada con éxito");
	    assertEquals("redirect:/registrar_entrega/" + idRepartidor, result);	
		
	}
	
	@Test
	void testSubmitEntregaSinPedido() {
	    
		String idRepartidor = "123";
	    Long idPedido = 1L;

	    // Configuración de mocks
	    when(pedidoDAO.findById(idPedido)).thenReturn(Optional.empty()); // Pedido no encontrado

	    // Invocación del método
	    RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
	    Model model = new ConcurrentModel();
	    String result = controller.submitEntrega(idRepartidor, model, idPedido, redirectAttributes);

	    // Verificaciones
	    verify(pedidoDAO, times(1)).findById(idPedido);
	    verify(pedidoDAO, never()).save(any(Pedido.class));
	    verify(servicioEntregaDAO, never()).save(any(ServicioEntrega.class));
	    verify(repartidorDAO, never()).save(any(Repartidor.class));

	    verify(redirectAttributes, times(1))
        .addFlashAttribute("error", "El pedido no existe."); // Error esperado
	    assertEquals("redirect:/registrar_entrega/" + idRepartidor, result); // Redirección esperada
	
	}
	
	
	@Test
	void testSubmitEntregaPedidoSinServicio() {
	    String idRepartidor = "123";
	    Long idPedido = 1L;

	    // Configuración de mocks
	    Pedido pedido = new Pedido();
	    pedido.setId_pedido(idPedido);
	    pedido.setEstado(EstadoPedido.PAGADO);
	    pedido.setEntrega(null); // Servicio de entrega es null

	    when(pedidoDAO.findById(idPedido)).thenReturn(Optional.of(pedido));

	    // Invocación del método
	    RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
	    Model model = new ConcurrentModel();
	    String result = controller.submitEntrega(idRepartidor, model, idPedido, redirectAttributes);

	    // Verificaciones
	    verify(pedidoDAO, times(1)).findById(idPedido);
	    verify(pedidoDAO, never()).save(any(Pedido.class));
	    verify(servicioEntregaDAO, never()).save(any(ServicioEntrega.class));
	    verify(repartidorDAO, never()).save(any(Repartidor.class));

	    verify(redirectAttributes, times(1))
        .addFlashAttribute("error", "El pedido no tiene un servicio de entrega asociado."); // Error esperado
	    assertEquals("redirect:/registrar_entrega/" + idRepartidor, result); // Redirección esperada
	
	}
	
	@Test
	void testSubmitEntregaSinRepartidor() {
		
		String idRepartidor = "123";
	    Long idPedido = 1L;

	    // Configuración de mocks
	    Pedido pedido = new Pedido();
	    pedido.setId_pedido(idPedido);
	    pedido.setEstado(EstadoPedido.PAGADO);
	    ServicioEntrega servicio = new ServicioEntrega();
	    pedido.setEntrega(servicio);

	    // Simulamos que no se encuentra el repartidor
	    when(pedidoDAO.findById(idPedido)).thenReturn(Optional.of(pedido));
	    when(repartidorDAO.findById(idRepartidor)).thenReturn(Optional.empty()); // Repartidor no encontrado

	    // Invocación del método
	    RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
	    Model model = new ConcurrentModel();
	    String result = controller.submitEntrega(idRepartidor, model, idPedido, redirectAttributes);

	    // Verificaciones
	    verify(pedidoDAO, times(1)).findById(idPedido);  // Verifica que se haya llamado a findById con el idPedido
	    verify(pedidoDAO, never()).save(any(Pedido.class));  // No debe guardar el pedido
	    verify(servicioEntregaDAO, times(1)).save(any(ServicioEntrega.class)); // Debe guardar el servicio de entrega
	    verify(repartidorDAO, times(1)).findById(idRepartidor);  // Verifica que se haya llamado a findById con el idRepartidor
	    verify(repartidorDAO, never()).save(any(Repartidor.class)); // No debe guardar el repartidor

	    // Verificación de la redirección y mensaje de error
	    verify(redirectAttributes, times(1))
	        .addFlashAttribute("error", "El repartidor no existe."); // Error esperado
	    assertEquals("redirect:/registrar_entrega/" + idRepartidor, result); // Redirección esperada
		
	}
	
}
