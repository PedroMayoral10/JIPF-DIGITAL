package es.jipfdigital.library.dominio.controladores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.jipfdigital.library.dominio.entidades.EstadoPedido;
import es.jipfdigital.library.dominio.entidades.Pedido;
import es.jipfdigital.library.dominio.entidades.Repartidor;
import es.jipfdigital.library.dominio.entidades.ServicioEntrega;
import es.jipfdigital.library.persistencia.PedidoDAO;
import es.jipfdigital.library.persistencia.RepartidorDAO;
import es.jipfdigital.library.persistencia.ServicioEntregaDAO;

@Controller
public class GestorRepartidor {
	
	private static final String IDREPARTIDOR = "idRepartidor";
	private static final String SERVICIOS = "servicios";
	private static final String REDIRIGIRREGISTRARENTREGA= "redirect:/registrar_entrega/";
	private static final String REDIRIGIRREGISTRARRECOGICA = "redirect:/registrar_recogida/";
	private static final String REGISTRARRECODIGA= "registrar_recogida";
	private static final String ERROR = "error";
	
	@Autowired
	RepartidorDAO repartidorDAO;
	@Autowired
	PedidoDAO pedidoDAO;
	@Autowired
	ServicioEntregaDAO servicioentregaDAO;
	
	
	/*
	 * GETMAPPINGS
	 */
	
	@GetMapping("/menurepartidor/{idRepartidor}")
	public String menuRepartidor(@PathVariable("idRepartidor") String idRepartidor, Model model) {
		model.addAttribute(IDREPARTIDOR, idRepartidor);
		return "menurepartidor";
	}
	
	@GetMapping("/pedidosrepartidor/{idRepartidor}")
	public String mostrarPedidos(@PathVariable("idRepartidor") String idRepartidor, Model model) {
		model.addAttribute(IDREPARTIDOR, idRepartidor);
		Repartidor repartidor = repartidorDAO.findById(idRepartidor).get();
		Collection<ServicioEntrega> servicios = repartidor.getServicios();
		model.addAttribute(SERVICIOS, servicios);
		return "pedidosrepartidor";
	}
	
	@GetMapping("/registrar_recogida/{idRepartidor}")
	public String menuRegistroRecogida(@PathVariable("idRepartidor") String idRepartidor, Model model) {
		model.addAttribute(IDREPARTIDOR, idRepartidor);
		Repartidor repartidor = repartidorDAO.findById(idRepartidor).get();
		Collection<ServicioEntrega> servicios = repartidor.getServicios();
		List<ServicioEntrega> serviciosPagados = new ArrayList<>();
		if(servicios!=null) {
			for ( ServicioEntrega servicio : servicios) {
				if(servicio.getPedido().getEstado() == EstadoPedido.PAGADO) {
					serviciosPagados.add(servicio);
				}
			}
		}
		model.addAttribute(SERVICIOS, serviciosPagados);
		return REGISTRARRECODIGA;
	}
	
	@GetMapping("/registrar_entrega/{idRepartidor}")
	public String menuRegistroEntrega(@PathVariable("idRepartidor") String idRepartidor, Model model) {
		model.addAttribute(IDREPARTIDOR, idRepartidor);
		Repartidor repartidor = repartidorDAO.findById(idRepartidor).get();
		Collection<ServicioEntrega> servicios = repartidor.getServicios();
		List<ServicioEntrega> serviciosPagados = new ArrayList<>();
		if(servicios!=null) {
			for ( ServicioEntrega servicio : servicios) {
				if(servicio.getPedido().getEstado() == EstadoPedido.RECOGIDO) {
					serviciosPagados.add(servicio);
				}
			}
		}
		model.addAttribute(SERVICIOS, serviciosPagados);
		return "registrar_entrega";
	}
	
	/*
	 * POSTMAPPINGS
	 */
	
	@PostMapping("/registrar_recogida/{idRepartidor}")
	public String submitRecogida(@PathVariable("idRepartidor") String idRepartidor, Model model,
			@RequestParam(value = "id_pedido", required = false) Long idPedido,
			RedirectAttributes redirectAttributes) {
		
		model.addAttribute(IDREPARTIDOR, idRepartidor);
		
		Optional <Pedido> optionalPedido = pedidoDAO.findById(idPedido);
		
	    if (optionalPedido.isEmpty()) {
	        redirectAttributes.addFlashAttribute(ERROR, "El pedido no existe.");
	        return REDIRIGIRREGISTRARRECOGICA + idRepartidor;
	    }
		
	    Pedido pedido = optionalPedido.get();
		pedido.setEstado(EstadoPedido.RECOGIDO);
		pedidoDAO.save(pedido);
		
		ServicioEntrega servicio = pedido.getEntrega();
		
		if (servicio == null) {
	        redirectAttributes.addFlashAttribute(ERROR, "El pedido no tiene un servicio de entrega asociado.");
	        return REDIRIGIRREGISTRARRECOGICA + idRepartidor;
	    }	
		
		servicio.setFechaRecepcion(LocalDate.now());
		servicioentregaDAO.save(servicio);
		
		redirectAttributes.addFlashAttribute("exito", "Recogida registrada con éxito");
		return REDIRIGIRREGISTRARRECOGICA + idRepartidor;
		
	}
	
	@PostMapping("/registrar_entrega/{idRepartidor}")
	public String submitEntrega(@PathVariable("idRepartidor") String idRepartidor, Model model,
			@RequestParam(value = "id_pedido", required = false) Long idPedido,
			RedirectAttributes redirectAttributes) {
		model.addAttribute(IDREPARTIDOR, idRepartidor);
		
		Optional <Pedido> optionalPedido = pedidoDAO.findById(idPedido);
		
	    if (optionalPedido.isEmpty()) {
	        redirectAttributes.addFlashAttribute(ERROR, "El pedido no existe.");
	        return REDIRIGIRREGISTRARENTREGA + idRepartidor;
	    }
		
	    Pedido pedido = optionalPedido.get();
		pedido.setEstado(EstadoPedido.ENTREGADO);
		
		ServicioEntrega servicio = pedido.getEntrega();
		
		if (servicio == null) {
		    redirectAttributes.addFlashAttribute(ERROR, "El pedido no tiene un servicio de entrega asociado.");
		    return REDIRIGIRREGISTRARENTREGA + idRepartidor;
		}
		
		servicio.setFechaEntrega(LocalDate.now());
		servicioentregaDAO.save(servicio);
		
		Optional <Repartidor> optRepartidor = repartidorDAO.findById(idRepartidor);
		
		if (optRepartidor.isEmpty()) {
	        redirectAttributes.addFlashAttribute(ERROR, "El repartidor no existe.");
	        return REDIRIGIRREGISTRARENTREGA + idRepartidor;
	    }
		
		Repartidor repartidor = optRepartidor.get();
		repartidor.actualizarEficiencia(1);
		repartidorDAO.save(repartidor);
		
		pedidoDAO.save(pedido); //Si no ha habido ningún error anteriormente, se guarda ya el pedido- 
		redirectAttributes.addFlashAttribute("exito", "Entrega registrada con éxito");
		return REDIRIGIRREGISTRARENTREGA + idRepartidor;
	}

}