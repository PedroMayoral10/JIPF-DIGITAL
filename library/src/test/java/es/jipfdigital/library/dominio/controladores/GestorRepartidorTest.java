package es.jipfdigital.library.dominio.controladores;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import es.jipfdigital.library.dominio.entidades.Repartidor;
import es.jipfdigital.library.dominio.entidades.ServicioEntrega;
import es.jipfdigital.library.persistencia.RepartidorDAO;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

class GestorRepartidorTest {

	@Mock
    private RepartidorDAO repartidorDAO;

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
    public void testMostrarPedidosRepartidorValidoConServicios() {
        
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
    public void testMostrarPedidosServiciosNull() {
        
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
	
	
	/**
	*	@Test
	*	void testMenuRegistroRecogida() {
	*		fail("Not yet implemented");
	*	}
	*
	*	@Test
	*	void testMenuRegistroEntrega() {
	*		fail("Not yet implemented");
	*	}
	*
	*	@Test
	*	void testSubmitRecogida() {
	*		fail("Not yet implemented");
	*	}
	*
	*	@Test
	*	void testSubmitEntrega() {
	*		fail("Not yet implemented");
	*	}
	*
	*/
	
}
