package es.JIPF_Digital.library.dominio.controladores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import es.JIPF_Digital.library.dominio.entidades.EstadoPedido;
import es.JIPF_Digital.library.dominio.entidades.ItemMenu;
import es.JIPF_Digital.library.dominio.entidades.MetodoPago;
import es.JIPF_Digital.library.dominio.entidades.Pago;
import es.JIPF_Digital.library.dominio.entidades.Pedido;
import es.JIPF_Digital.library.dominio.entidades.Repartidor;
import es.JIPF_Digital.library.dominio.entidades.Restaurante;
import es.JIPF_Digital.library.dominio.entidades.ServicioEntrega;
import es.JIPF_Digital.library.persistencia.CartaMenuDAO;
import es.JIPF_Digital.library.persistencia.ClienteDAO;
import es.JIPF_Digital.library.persistencia.DireccionDAO;
import es.JIPF_Digital.library.persistencia.ItemMenuDAO;
import es.JIPF_Digital.library.persistencia.PedidoDAO;
import es.JIPF_Digital.library.persistencia.RepartidorDAO;
import es.JIPF_Digital.library.persistencia.RestauranteDAO;

@Controller
public class GestorPedidos {

	@Autowired
	private ItemMenuDAO itemMenuDAO;
	@Autowired
	private RestauranteDAO restauranteDAO;
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private CartaMenuDAO cartamenuDAO;
	@Autowired
	private PedidoDAO pedidoDAO;
	@Autowired
	private RepartidorDAO repartidorDAO;
	@Autowired
	private DireccionDAO direccionDAO;

	@GetMapping("/pedidos")
	public String pedidosForm(Model model) {
		return "pedidos";
	}

	// Obtenemos los ID's de los items para luego usarlos en el POSTmapping
	private static List<Long> itemIds = new ArrayList<>();

	private static List<ItemMenu> itemsPedidos = new ArrayList<>();

	@GetMapping("/realizarpedido/{id_cliente}/{id_restaurante}")
	public String detalleRestaurante(@PathVariable("id_cliente") String idCliente,
			@PathVariable("id_restaurante") String idRestaurante, Model model) {
		Restaurante restaurante = restauranteDAO.findById(idRestaurante).orElse(null);
		Cliente cliente = clienteDAO.findById(idCliente).orElse(null);
		model.addAttribute("menus", cartamenuDAO.findAllByRestauranteId(idRestaurante));
		model.addAttribute("restaurante", restaurante);
		model.addAttribute("cliente", cliente);
		return "realizarpedido";
	}

	@PostMapping("/realizarpedido/{id_cliente}/{id_restaurante}")
	public String procesarPedido(@PathVariable("id_cliente") String idCliente,
			@PathVariable("id_restaurante") String idRestaurante, Model model,
			@RequestParam(value = "menuId", required = false) Long idMenu, RedirectAttributes redirectAttributes) {
		Cliente cliente = clienteDAO.findById(idCliente).orElse(null);
		Restaurante restaurante = restauranteDAO.findById(idRestaurante).orElse(null);
		CartaMenu cartamenu = cartamenuDAO.findById(idMenu).orElse(null);
		List<CartaMenu> menus = cartamenuDAO.findAll();
		if (!cartamenu.getItems().isEmpty())
			redirectAttributes.addFlashAttribute("items", cartamenu.getItems());
		redirectAttributes.addFlashAttribute("menus", menus);
		redirectAttributes.addFlashAttribute("menu_seleccionado", cartamenu.getId());

		return "redirect:/realizarpedido/" + idCliente + "/" + idRestaurante;
	}

	@GetMapping("/realizarpago/{idCliente}/{idRestaurante}")
	public String realizarPago(@PathVariable("idCliente") String idCliente,
			@PathVariable("idRestaurante") String idRestaurante, @RequestParam Map<String, String> params,
			Model model) {

		itemsPedidos = obtenerItems(params);

		for (ItemMenu item : itemsPedidos) {
			itemIds.add(item.getId()); // Suponiendo que hay un método getId() en ItemMenu
		}

		double precioTotalPedido = 0.0;
		for (ItemMenu item : itemsPedidos) {
			precioTotalPedido += item.getPrecio();
		}

		// Añadir los ítems al modelo junto con los identificadores de cliente y
		// restaurante
		model.addAttribute("itemsPedidos", itemsPedidos);
		model.addAttribute("idCliente", idCliente);
		model.addAttribute("idRestaurante", idRestaurante);
		model.addAttribute("precioTotal", precioTotalPedido);

		return "realizarpago";
	}
 
	@GetMapping("/confirmacionpago/{idCliente}")
	public String confirmacionpago(@PathVariable("idCliente") String idCliente, Model model) {
		model.addAttribute("idCliente", idCliente); 
		return "confirmacionPago";
	}
	@PostMapping("/realizarpago/{idCliente}/{idRestaurante}")
	public String submitPago(@PathVariable("idCliente") String idCliente,
			@PathVariable("idRestaurante") String idRestaurante, Model model,
			@RequestParam(value = "codigoPostal", required = false) String codigoPostal,
			@RequestParam("metodoPago") MetodoPago tipo,
			@RequestParam("fechaTransaccion") String fechaTransaccionString,
			@RequestParam(value = "calle", required = false) String calle,
			@RequestParam(value = "numero", required = false) String numero,
			@RequestParam(value = "complemento", required = false) String complemento,
			@RequestParam(value = "municipio", required = false) String municipio) {

		// Convertir la fecha de transacción de String a LocalDate
		LocalDate fechaTransaccion = LocalDate.parse(fechaTransaccionString);

		Restaurante restaurante = restauranteDAO.findById(idRestaurante).orElse(null);
		Cliente cliente = clienteDAO.findById(idCliente).orElse(null);
		Pedido pedido = new Pedido();

		pedido.setCliente(cliente);
		pedido.setRestaurante(restaurante);
		pedido.setFecha(fechaTransaccion);
		pedido.setEstado(EstadoPedido.PAGADO);
		pedido.setItems(itemsPedidos);
		Pago pago = new Pago(pedido, tipo, fechaTransaccion);
		pedido.setPago(pago);

		Direccion direccion = new Direccion(codigoPostal, calle, numero, complemento, municipio);
		direccionDAO.save(direccion);
		ServicioEntrega servicioEntrega = new ServicioEntrega();
		servicioEntrega.setPedido(pedido);
		servicioEntrega.setDireccion(direccion);
		servicioEntrega.setRepartidor(calcularRepartidorOptimo());
		pedido.setEntrega(servicioEntrega);

		pedidoDAO.save(pedido);
		model.addAttribute("mensajeExito", "El pago se ha realizado correctamente.");
		model.addAttribute("idCliente", idCliente); // Pasar el idCliente a la vista
		

		return "redirect:/confirmacionpago/" + idCliente;
	}

	private List<ItemMenu> obtenerItems(Map<String, String> params) {

		int index = 0;
		while (params.containsKey("id" + index)) {
			Long id_item = Long.parseLong(params.get("id" + index));
			ItemMenu item = itemMenuDAO.findById(id_item).orElse(null);
			itemsPedidos.add(item);
			index++;
			System.out.println(item.getNombre());
		}

		System.out.println(itemsPedidos.size());

		return itemsPedidos;

	}

	private Repartidor calcularRepartidorOptimo() {
		List<Repartidor> repartidores = repartidorDAO.findAll();

		Repartidor repartidorOptimo = null;
		int minServicios = Integer.MAX_VALUE;

		for (Repartidor repartidor : repartidores) {
			int cantidadServicios = repartidor.getServicios().size();

			if (cantidadServicios < minServicios) {
				minServicios = cantidadServicios;
				repartidorOptimo = repartidor;
			} else if (cantidadServicios == minServicios) {

				if (repartidor.getEficiencia() > repartidorOptimo.getEficiencia()) {
					repartidorOptimo = repartidor;
				}
			}
		}

		return repartidorOptimo;

	}

}