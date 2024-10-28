package es.JIPF_Digital.library.dominio.controladores;

import es.JIPF_Digital.library.persistencia.*;
import es.JIPF_Digital.library.dominio.entidades.*;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class GestorClientes {

	RestauranteDAO restauranteDAO;

	@GetMapping("/menucliente")
	public String MenuCliente(Model model) {
		return "menucliente";
	}

	@GetMapping("/listarestaurantes")
	public String ListaRestaurante(Model model) {
		return "listarestaurantes";
	}

	/**
	 * 
	 * @param zona
	 */
	public List<Restaurante> buscarRestaurante(String zona) {
		// TODO - implement GestorClientes.buscarRestaurante
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param cliente
	 * @param r
	 */
	public void favorito(Cliente cliente, Restaurante r) {
		// TODO - implement GestorClientes.favorito
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param nombre
	 * @param apellido
	 * @param d
	 */
	public Cliente registrarCliente(String nombre, String apellido, Direccion d) {
		// TODO - implement GestorClientes.registrarCliente
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param calle
	 * @param numeero
	 * @param complemento
	 * @param cp
	 * @param municipio
	 */
	private Direccion altaDirecion(String calle, String numeero, String complemento, String cp, String municipio) {
		// TODO - implement GestorClientes.altaDirecion
		throw new UnsupportedOperationException();
	}

}