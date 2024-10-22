package es.JIPF_Digital.library.dominio.controladores;

import es.JIPF_Digital.library.persistencia.*;
import es.JIPF_Digital.library.dominio.entidades.*;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class GestorPedidos {

	PedidoDAO pedidoDAO;
	ServicioEntregaDAO servicioEntregaDAO;
	Pedido pedidoEnMarcha;


	@GetMapping("/pedidos")
	public String pedidosForm(Model model) {
		return "pedidos";
	}

	
	
	public void realizarPedido(Cliente c, Restaurante r, List<ItemMenu> items) {
		// TODO - implement GestorPedidos.realizarPedido
		throw new UnsupportedOperationException();
	}


	private boolean realizarPago(Pedido p) {
		// TODO - implement GestorPedidos.realizarPago
		throw new UnsupportedOperationException();
	}


	private ServicioEntrega crearServicioEntrega(Pedido p, Direccion d) {
		// TODO - implement GestorPedidos.crearServicioEntrega
		throw new UnsupportedOperationException();
	}


	public void addItemMenu(ItemMenu item) {
		// TODO - implement GestorPedidos.addItemMenu
		throw new UnsupportedOperationException();
	}


	public void eliminarItemMenu(ItemMenu item) {
		// TODO - implement GestorPedidos.eliminarItemMenu
		throw new UnsupportedOperationException();
	}


	public void comenzarPedido(Restaurante resaturante) {
		// TODO - implement GestorPedidos.comenzarPedido
		throw new UnsupportedOperationException();
	}

}