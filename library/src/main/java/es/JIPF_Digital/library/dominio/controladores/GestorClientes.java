package es.JIPF_Digital.library.dominio.controladores;

import es.JIPF_Digital.library.persistencia.*;
import es.JIPF_Digital.library.dominio.entidades.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GestorClientes {

	@Autowired
	private RestauranteDAO restauranteDAO; // Declarar el repositorio

	@Autowired
	private ClienteDAO clienteDAO;

	/*
	 * GETMAPPINGS
	 */
	@GetMapping("/menucliente/{id}")
	public String MenuCliente(@PathVariable("id") String idCliente, Model model) {
		model.addAttribute("id_cliente", idCliente);
		return "menucliente";
	}

	@GetMapping("/listarestaurantes/{id}")
	public String ListaRestaurante(Model model, @PathVariable("id") String idCliente) {
		List<Restaurante> restaurantes;
		Cliente cliente = clienteDAO.findById(idCliente).orElse(null);
		restaurantes = restauranteDAO.findAll();
		model.addAttribute("restaurantes", restaurantes);
		model.addAttribute("cliente", cliente);
		return "listarestaurantes"; // Retorna el nombre del HTML
	}

	/*
	 * POSTMAPPINGS
	 */

	@PostMapping("/listarestaurantes/{id}")
	public String favorito(Model model, @PathVariable("id") String idCliente,
			@RequestParam(value = "id_restaurante", required = false) String idRestaurante) {
		Restaurante restaurante = restauranteDAO.findById(idRestaurante).orElse(null);
		Cliente cliente = clienteDAO.findById(idCliente).orElse(null);
		if (cliente.getFavoritos().contains(restaurante)) {
			cliente.getFavoritos().remove(restaurante);
		} else {
			cliente.getFavoritos().add(restaurante);
		}
		clienteDAO.save(cliente);
		return "redirect:/listarestaurantes/" + idCliente;
	}

	public List<Restaurante> buscarRestaurante(String zona) {
		// Implementar la búsqueda de restaurantes por zona
		throw new UnsupportedOperationException();
	}

	public Cliente registrarCliente(String nombre, String apellido, Direccion d) {
		// Implementar la lógica para registrar un nuevo cliente
		throw new UnsupportedOperationException();
	}

	private Direccion altaDirecion(String calle, String numero, String complemento, String cp, String municipio) {
		// Implementar la lógica para crear una nueva dirección
		throw new UnsupportedOperationException();
	}
}
