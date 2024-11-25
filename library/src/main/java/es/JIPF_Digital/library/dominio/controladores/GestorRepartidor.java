package es.JIPF_Digital.library.dominio.controladores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.JIPF_Digital.library.dominio.entidades.EstadoPedido;
import es.JIPF_Digital.library.dominio.entidades.Pedido;
import es.JIPF_Digital.library.dominio.entidades.Repartidor;
import es.JIPF_Digital.library.dominio.entidades.ServicioEntrega;
import es.JIPF_Digital.library.persistencia.PedidoDAO;
import es.JIPF_Digital.library.persistencia.RepartidorDAO;
import es.JIPF_Digital.library.persistencia.ServicioEntregaDAO;

@Controller
public class GestorRepartidor {
	
	@Autowired
	RepartidorDAO repartidorDAO;
	@Autowired
	PedidoDAO pedidoDAO;
	@Autowired
	ServicioEntregaDAO servicioentregaDAO;
	
	private static final String IDREPARTIDOR = "idRepartidor";
	private static final String SERVICIOS = "servicios";
	
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
		for ( ServicioEntrega servicio : servicios) {
			if(servicio.getPedido().getEstado() == EstadoPedido.PAGADO) {
				serviciosPagados.add(servicio);
			}
		}
		model.addAttribute(SERVICIOS, serviciosPagados);
		return "registrar_recogida";
	}
	
	@GetMapping("/registrar_entrega/{idRepartidor}")
	public String menuRegistroEntrega(@PathVariable("idRepartidor") String idRepartidor, Model model) {
		model.addAttribute(IDREPARTIDOR, idRepartidor);
		Repartidor repartidor = repartidorDAO.findById(idRepartidor).get();
		Collection<ServicioEntrega> servicios = repartidor.getServicios();
		List<ServicioEntrega> serviciosPagados = new ArrayList<>();
		for ( ServicioEntrega servicio : servicios) {
			if(servicio.getPedido().getEstado() == EstadoPedido.RECOGIDO) {
				serviciosPagados.add(servicio);
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
		Pedido pedido = pedidoDAO.findById(idPedido).get();
		pedido.setEstado(EstadoPedido.RECOGIDO);
		pedidoDAO.save(pedido);
		ServicioEntrega servicio = pedido.getEntrega();
		servicio.setFechaRecepcion(LocalDate.now());
		servicioentregaDAO.save(servicio);
		
		redirectAttributes.addFlashAttribute("exito", "Recogida registrada con éxito");
		return "redirect:/registrar_recogida/" + idRepartidor;
	}
	
	@PostMapping("/registrar_entrega/{idRepartidor}")
	public String submitEntrega(@PathVariable("idRepartidor") String idRepartidor, Model model,
			@RequestParam(value = "id_pedido", required = false) Long idPedido,
			RedirectAttributes redirectAttributes) {
		model.addAttribute(IDREPARTIDOR, idRepartidor);
		
		Pedido pedido = pedidoDAO.findById(idPedido).get();
		pedido.setEstado(EstadoPedido.ENTREGADO);
		pedidoDAO.save(pedido);
		
		ServicioEntrega servicio = pedido.getEntrega();
		servicio.setFechaEntrega(LocalDate.now());
		servicioentregaDAO.save(servicio);
		
		Repartidor repartidor = repartidorDAO.findById(idRepartidor).get();
		repartidor.actualizarEficiencia(1);
		repartidorDAO.save(repartidor);
		
		redirectAttributes.addFlashAttribute("exito", "Entrega registrada con éxito");
		return "redirect:/registrar_entrega/" + idRepartidor;
	}

}