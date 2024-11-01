package es.JIPF_Digital.library.dominio.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.JIPF_Digital.library.dominio.entidades.CartaMenu;
import es.JIPF_Digital.library.dominio.entidades.Cliente;
import es.JIPF_Digital.library.dominio.entidades.Direccion;
import es.JIPF_Digital.library.dominio.entidades.ItemMenu;
import es.JIPF_Digital.library.dominio.entidades.Pedido;
import es.JIPF_Digital.library.dominio.entidades.Restaurante;
import es.JIPF_Digital.library.dominio.entidades.ServicioEntrega;
import es.JIPF_Digital.library.persistencia.CartaMenuDAO;
import es.JIPF_Digital.library.persistencia.ClienteDAO;
import es.JIPF_Digital.library.persistencia.RestauranteDAO;


@Controller
public class GestorPedidos {
	
	@Autowired
    private RestauranteDAO restauranteDAO;
    @Autowired
    private ClienteDAO clienteDAO;
	@Autowired
	private CartaMenuDAO cartamenuDAO;
	@GetMapping("/pedidos")
	public String pedidosForm(Model model) {
		return "pedidos";
	}

    @GetMapping("/realizarpedido/{id_cliente}/{id_restaurante}")
    public String detalleRestaurante(@PathVariable("id_cliente") String idCliente, @PathVariable("id_restaurante") String idRestaurante, Model model) {
        Restaurante restaurante = restauranteDAO.findById(idRestaurante).orElse(null);
        Cliente cliente = clienteDAO.findById(idCliente).orElse(null);
		model.addAttribute("menus", cartamenuDAO.findAllByRestauranteId(idRestaurante));
        model.addAttribute("restaurante", restaurante);
        model.addAttribute("cliente", cliente);
        return "realizarpedido";
    }

	@PostMapping("/realizarpedido/{id_cliente}/{id_restaurante}")
	public String procesarPedido(@PathVariable("id_cliente") String idCliente, @PathVariable("id_restaurante")
	String idRestaurante,Model model,@RequestParam(value = "menuId", required = false) Long idMenu, 
	RedirectAttributes redirectAttributes){
		Cliente cliente = clienteDAO.findById(idCliente).orElse(null);
        Restaurante restaurante = restauranteDAO.findById(idRestaurante).orElse(null);
		CartaMenu cartamenu = cartamenuDAO.findById(idMenu).orElse(null);
		List<CartaMenu> menus = cartamenuDAO.findAll();
		if (!cartamenu.getItems().isEmpty())
			redirectAttributes.addFlashAttribute("items", cartamenu.getItems());
		redirectAttributes.addFlashAttribute("menus", menus);
		redirectAttributes.addFlashAttribute("menu_seleccionado", cartamenu.getId());

		return "redirect:/realizarpedido/"+ idCliente +"/" + idRestaurante;
		}
	/* public void realizarPedido(Cliente cliente, Restaurante restaurante, List<ItemMenu> items) {
			Pedido nuevoPedido = new Pedido();
			nuevoPedido.setCliente(cliente);
			nuevoPedido.setRestaurante(restaurante);
			nuevoPedido.setItems(items);
			nuevoPedido.setFecha(new Date());
			nuevoPedido.setEstado(EstadoPedido.PENDIENTE);
			
			PedidoDAO.save(nuevoPedido);
		}*/
		

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