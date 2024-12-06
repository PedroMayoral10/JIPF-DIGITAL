package es.jipfdigital.library.dominio.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.jipfdigital.library.dominio.entidades.Cliente;
import es.jipfdigital.library.dominio.entidades.Direccion;
import es.jipfdigital.library.dominio.entidades.Repartidor;
import es.jipfdigital.library.dominio.entidades.Restaurante;
import es.jipfdigital.library.dominio.entidades.Usuario;
import es.jipfdigital.library.persistencia.ClienteDAO;
import es.jipfdigital.library.persistencia.RepartidorDAO;
import es.jipfdigital.library.persistencia.RestauranteDAO;

import java.util.Optional;

@Controller
public class GestorUsuario {
	private static final String USUARIO_STR = "usuario";
	private static final String LOGIN_STR = "login";
	private static final String REGISTRO_STR = "registro";
	private static final String ERROR_STR = "error";
	private static final String CONTRASENA_STR = "Contraseña incorrecta, pruebe otra vez";
	private static final String RELLENA_CAMPOS = "Rellena todos los campos";
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private RestauranteDAO restauranteDAO;
	@Autowired
	private RepartidorDAO repartidorDAO;
	

	/*
	 * GETMAPPINGS
	 */

	@GetMapping("/")
	public String redirigirLogin(Model model) {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute(USUARIO_STR, new Usuario());
		return LOGIN_STR;
	}

	@GetMapping("/registro")
	public String registroForm(Model model) {
		model.addAttribute(USUARIO_STR, new Usuario());
		return REGISTRO_STR;
	}

	/*
	 * POSTMAPPINGS
	 */

	@PostMapping("/login")
	public String loginSubmit(@ModelAttribute Usuario usuario, Model model) {
		if (usuario != null) {
			model.addAttribute(USUARIO_STR, usuario);
			Optional<Cliente> clienteOpt = clienteDAO.findById(usuario.getIdUsuario());
			Optional<Restaurante> restauranteOpt = restauranteDAO.findById(usuario.getIdUsuario());
			Optional<Repartidor> repartidorOpt = repartidorDAO.findById(usuario.getIdUsuario());
			if (clienteOpt.isPresent()) {
				Cliente cliente = clienteOpt.get();
				if (cliente.getPass().equals(usuario.getPass())) {
					return "redirect:/menucliente/" + cliente.getIdUsuario();
				} else {
					model.addAttribute(ERROR_STR, CONTRASENA_STR);
					return LOGIN_STR;
				}
			} else if (restauranteOpt.isPresent()) {
				Restaurante restaurante = restauranteOpt.get();
				if (restaurante.getPass().equals(usuario.getPass())) {
					return "redirect:/menurestaurante/" + restaurante.getIdUsuario();
				} else {
					model.addAttribute(ERROR_STR, CONTRASENA_STR);
					return LOGIN_STR;
				}
			} else if (repartidorOpt.isPresent()) {
				Repartidor repartidor = repartidorOpt.get();
				if (repartidor.getPass().equals(usuario.getPass())) {
					return "redirect:/menurepartidor/" + repartidor.getIdUsuario();
				} else {
					model.addAttribute(ERROR_STR, CONTRASENA_STR);
					return LOGIN_STR;
				}
			} else {
				model.addAttribute(ERROR_STR, "El usuario no existe, pruebe otra vez");
				return LOGIN_STR;
			}
		} else {
			model.addAttribute(ERROR_STR, "El usuario no existe, pruebe otra vez");
			return LOGIN_STR;
		}

	}

	@PostMapping("/registro")
	public String registroSubmit(
			@ModelAttribute Usuario usuario,
			@RequestParam(value = "apellidosCliente", required = false) String apellidosCliente,
			@RequestParam(value = "dniCliente", required = false) String dniCliente,
			@RequestParam(value = "cifRestaurante", required = false) String cifRestaurante,
			@RequestParam(value = "codigoPostalRestaurante", required = false) String codigoPostalRestaurante,
			@RequestParam(value = "calleRestaurante", required = false) String calleRestaurante,
			@RequestParam(value = "numeroRestaurante", required = false) String numeroRestaurante,
			@RequestParam(value = "complementoRestaurante", required = false) String complementoRestaurante,
			@RequestParam(value = "municipioRestaurante", required = false) String municipioRestaurante,
			@RequestParam(value = "apellidosRepartidor", required = false) String apellidosRepartidor,
			@RequestParam(value = "nifRepartidor", required = false) String nifRepartidor,
			@RequestParam(value = "rol", required = false) Integer rol,
			Model model) {
		System.out.println(rol);
		switch (rol) {
			case 1:
				if(registrarCliente(usuario, apellidosCliente, dniCliente, model) == 0)
					return REGISTRO_STR;
				break;
			case 2:
				if(registrarRestaurante(usuario, codigoPostalRestaurante, calleRestaurante, numeroRestaurante,
						complementoRestaurante, municipioRestaurante, cifRestaurante, model) == 0)
						return REGISTRO_STR;
				break;
			case 3:
				if(registrarRepartidor(usuario, apellidosRepartidor, nifRepartidor, model) == 0)
					return REGISTRO_STR;
				break;
			default:
				model.addAttribute("rolNulo", "Ingresa un tipo de usuario");
				return REGISTRO_STR;
		}

		return LOGIN_STR;
	}

	private int registrarCliente(Usuario usuario, String apellidosCliente, String dniCliente, Model model) {
		if (usuario != null && !apellidosCliente.isEmpty() && !dniCliente.isEmpty()) {
			Cliente cliente = new Cliente(usuario.getIdUsuario(), usuario.getNombre(), usuario.getPass(),
					apellidosCliente, dniCliente);
			clienteDAO.save(cliente);
			return 1;
		} else {
			model.addAttribute(ERROR_STR, RELLENA_CAMPOS);
			return 0;
		}

	}

	private int registrarRestaurante(Usuario usuario, String codigoPostalRestaurante, String calleRestaurante,
			String numeroRestaurante,
			String complementoRestaurante, String municipioRestaurante, String cifRestaurante,
			Model model) {
		if (usuario != null && comprobarCondicionesRegistrarRestaurante(codigoPostalRestaurante, calleRestaurante, numeroRestaurante, complementoRestaurante, municipioRestaurante, cifRestaurante)) {
			Direccion dir = new Direccion(codigoPostalRestaurante, calleRestaurante, numeroRestaurante,
					complementoRestaurante, municipioRestaurante);
			Restaurante restaurante = new Restaurante(usuario.getIdUsuario(), usuario.getNombre(),
					usuario.getPass(), dir, cifRestaurante);
			restauranteDAO.save(restaurante);
			return 1;
		} else {
			model.addAttribute(ERROR_STR, RELLENA_CAMPOS);
			return 0;
		}

	}

	private int registrarRepartidor(Usuario usuario, String apellidosRepartidor, String nifRepartidor, Model model) {

		if (usuario != null && !apellidosRepartidor.isEmpty() && !nifRepartidor.isEmpty()) {
			Repartidor repartidor = new Repartidor(usuario.getIdUsuario(), usuario.getNombre(),
					usuario.getPass(), apellidosRepartidor, nifRepartidor);
			repartidorDAO.save(repartidor);
			return 1;
		}else{
			model.addAttribute(ERROR_STR, RELLENA_CAMPOS);
			return 0;
		}

	}
	
	private boolean comprobarCondicionesRegistrarRestaurante(String codigoPostal, String calle, String numero, String complemento, String municipio, String cif) {
	    return !codigoPostal.isEmpty() &&
	           !calle.isEmpty() &&
	           !numero.isEmpty() &&
	           !complemento.isEmpty() &&
	           !municipio.isEmpty() &&
	           !cif.isEmpty();
	}
	

}