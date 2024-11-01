package es.JIPF_Digital.library.dominio.controladores;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.JIPF_Digital.library.dominio.entidades.*;
import es.JIPF_Digital.library.persistencia.*;


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
	
	
	@GetMapping("/realizarpago/{idCliente}/{idRestaurante}")
    public String realizarPago(
        @PathVariable("idCliente") String idCliente,
        @PathVariable("idRestaurante") String idRestaurante,
        @RequestParam Map<String, String> params,
        Model model) {
		
		List<ItemMenu> itemsPedidos = obtenerItems(params);
		double precioTotalPedido = 0.0;
		for (ItemMenu item : itemsPedidos) {
		    precioTotalPedido += item.getPrecio();
		}


        // Añadir los ítems al modelo junto con los identificadores de cliente y restaurante
        model.addAttribute("itemsPedidos", itemsPedidos);
        model.addAttribute("idCliente", idCliente);
        model.addAttribute("idRestaurante", idRestaurante);
        model.addAttribute("precioTotal", precioTotalPedido);
        
        return "realizarpago";
    }
	
	@PostMapping("/realizarpago/{idCliente}/{idRestaurante}")
	public String submitPago(@PathVariable("idCliente") String idCliente, @PathVariable("idRestaurante") String idRestaurante,
	        @RequestParam Map<String, String> params, Model model) {
		
		List<ItemMenu> itemsPedidos = obtenerItems(params);
		int precioTotalPedido = 0;
		for (ItemMenu item : itemsPedidos) {
		    precioTotalPedido += item.getPrecio();
		}
        
		System.out.println(precioTotalPedido);
		
		return "redirect:/login";
	}
	



	private List<ItemMenu> obtenerItems(Map<String, String> params) {
		List<ItemMenu> itemsPedidos = new ArrayList<>();
        int index = 0;
        while (params.containsKey("nombre" + index)) {
            String nombreItem = params.get("nombre" + index);
            String tipo_item = params.get("tipo" + index);
            double precio = Double.parseDouble(params.get("precio" + index));
            ItemMenu item;
    		if (tipo_item.equals("COMIDA")) {
    			item = new ItemMenu(nombreItem, TipoItemMenu.COMIDA, precio);
    		} else if (tipo_item.equals("BEBIDA")) {
    			item = new ItemMenu(nombreItem, TipoItemMenu.BEBIDA, precio);
    		} else {
    			item = new ItemMenu(nombreItem, TipoItemMenu.POSTRE, precio);
    		}
            
            itemsPedidos.add(item);
            index++;
        }
        return itemsPedidos;
		
	}
	
	private boolean realizarPago(Pedido p) {
		// TODO - implement GestorPedidos.realizarPago
		throw new UnsupportedOperationException();
	}


	private ServicioEntrega crearServicioEntrega(Pedido p, Direccion d) {
		// TODO - implement GestorPedidos.crearServicioEntrega
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