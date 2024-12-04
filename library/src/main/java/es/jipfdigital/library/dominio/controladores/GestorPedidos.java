package es.jipfdigital.library.dominio.controladores;

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

import java.math.BigDecimal;
import java.math.RoundingMode;

import es.jipfdigital.library.dominio.entidades.*;
import es.jipfdigital.library.persistencia.*;

@Controller
public class GestorPedidos {

	private static final String IDCLIENTE = "idCliente";
	private static List<Long> itemIds = new ArrayList<>();
	private static List<ItemMenu> itemsPedidos = new ArrayList<>();

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

	/*
	 * GETMAPPINGS
	 */

	@GetMapping("/realizarpedido/{id_cliente}/{id_restaurante}")
	public String detalleRestaurante(@PathVariable("id_cliente") String idCliente,
			@PathVariable("id_restaurante") String idRestaurante, Model model) {
		Restaurante restaurante = restauranteDAO.findById(idRestaurante).get();
		Cliente cliente = clienteDAO.findById(idCliente).get();
		model.addAttribute("menus", cartamenuDAO.findAllByRestauranteId(idRestaurante));
		model.addAttribute("restaurante", restaurante);
		model.addAttribute("cliente", cliente);
		return "realizarpedido";
	}

	@GetMapping("/realizarpago/{idCliente}/{idRestaurante}")
	public String realizarPago(@PathVariable("idCliente") String idCliente,
			@PathVariable("idRestaurante") String idRestaurante, @RequestParam Map<String, String> params,
			Model model) {

		itemsPedidos = obtenerItems(params);

		for (ItemMenu item : itemsPedidos) {
			itemIds.add(item.getId());
		}

		double precioTotalPedido = 0.0;
		for (ItemMenu item : itemsPedidos) {
			precioTotalPedido += item.getPrecio();
		}
		BigDecimal precioTotalRedondeado = BigDecimal.valueOf(precioTotalPedido).setScale(2, RoundingMode.HALF_UP);
		double precioTotal = precioTotalRedondeado.doubleValue();

		model.addAttribute("itemsPedidos", itemsPedidos);
		model.addAttribute(IDCLIENTE, idCliente);
		model.addAttribute("idRestaurante", idRestaurante);
		model.addAttribute("precioTotal", precioTotal);

		return "realizarpago";
	}

	@GetMapping("/confirmacionpago/{idCliente}")
	public String confirmacionpago(@PathVariable("idCliente") String idCliente, Model model) {
		model.addAttribute(IDCLIENTE, idCliente);
		return "confirmacionPago";
	}

	@GetMapping("/pedidoscliente/{idCliente}")
	public String mostrarPedidos(@PathVariable("idCliente") String idCliente, Model model) {
		List<Pedido> pedidos = pedidoDAO.findPedidosByCliente(idCliente);
		model.addAttribute("pedidos", pedidos);
		model.addAttribute(IDCLIENTE, idCliente);

		return "pedidoscliente";
	}

	/*
	 * POSTMAPPINGS
	 */

	@PostMapping("/realizarpedido/{id_cliente}/{id_restaurante}")
	public String procesarPedido(@PathVariable("id_cliente") String idCliente,
			@PathVariable("id_restaurante") String idRestaurante, Model model,
			@RequestParam(value = "menuId", required = false) Long idMenu, RedirectAttributes redirectAttributes) {
		CartaMenu cartamenu = cartamenuDAO.findById(idMenu).get();
		List<CartaMenu> menus = cartamenuDAO.findAll();
		if (!cartamenu.getItems().isEmpty())
			redirectAttributes.addFlashAttribute("items", cartamenu.getItems());
		redirectAttributes.addFlashAttribute("menus", menus);
		redirectAttributes.addFlashAttribute("menu_seleccionado", cartamenu.getId());

		return "redirect:/realizarpedido/" + idCliente + "/" + idRestaurante;
	}

	@PostMapping("/realizarpago/{idCliente}/{idRestaurante}")
	public String submitPago(@PathVariable("idCliente") String idCliente,
			@PathVariable("idRestaurante") String idRestaurante, Model model,
			@RequestParam(value = "codigoPostal", required = false) String codigoPostal,
			@RequestParam("metodoPago") MetodoPago tipo,
			@RequestParam(value = "calle", required = false) String calle,
			@RequestParam(value = "numero", required = false) String numero,
			@RequestParam(value = "complemento", required = false) String complemento,
			@RequestParam(value = "municipio", required = false) String municipio) {

		LocalDate fechaTransaccion = LocalDate.now();

		Restaurante restaurante = restauranteDAO.findById(idRestaurante).get();
		Cliente cliente = clienteDAO.findById(idCliente).get();
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
		model.addAttribute(IDCLIENTE, idCliente);

		return "redirect:/confirmacionpago/" + idCliente;
	}

	public List<ItemMenu> obtenerItems(Map<String, String> params) {

		int index = 0;
		while (params.containsKey("id" + index)) {
			Long idItem = Long.parseLong(params.get("id" + index));
			ItemMenu item = itemMenuDAO.findById(idItem).get();
			itemsPedidos.add(item);
			index++;
		}

		return itemsPedidos;

	}

	public Repartidor calcularRepartidorOptimo() {
		List<Repartidor> repartidores = repartidorDAO.findAll();

		Repartidor repartidorOptimo = repartidores.get(0);
		int minServicios = Integer.MAX_VALUE;

		for (Repartidor repartidor : repartidores) {
			int cantidadServicios = repartidor.getServicios().size();

			if (cantidadServicios < minServicios) {
				minServicios = cantidadServicios;
				repartidorOptimo = repartidor;
			} else {
				if (cantidadServicios == minServicios) {
					if (repartidor.getEficiencia() > repartidorOptimo.getEficiencia()) {
						repartidorOptimo = repartidor;
					}
				}
			}
		}

		return repartidorOptimo;
	}

}