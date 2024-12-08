package es.jipfdigital.library.dominio.controladores;

import java.util.Optional;

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

@Controller
public class GestorUsuario {
	private static final String USUARIO_STR = "usuario";
	private static final String LOGIN_STR = "login";
	private static final String REGISTRO_STR = "registro";
	private static final String ERROR_STR = "error";
	private static final String CONTRASENA_STR = "Contraseña incorrecta, pruebe otra vez";
	private static final String RELLENA_CAMPOS = "Rellena todos los campos";
	private static final String USUARIO_EXISTE_STR = "El nombre de usuario ya existe";
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
	public String loginSubmit(Usuario usuario, Model model) {
		if (usuario == null) {
			model.addAttribute(ERROR_STR, "El usuario no existe, pruebe otra vez");
			return LOGIN_STR;
		}

		model.addAttribute(USUARIO_STR, usuario);
		Optional<Cliente> clienteOpt = clienteDAO.findById(usuario.getIdUsuario());
		Optional<Restaurante> restauranteOpt = restauranteDAO.findById(usuario.getIdUsuario());
		Optional<Repartidor> repartidorOpt = repartidorDAO.findById(usuario.getIdUsuario());

		String resultadoCliente = manejarCliente(clienteOpt, usuario, model);
		String resultadoRestaurante = manejarRestaurante(restauranteOpt, usuario, model);
		String resultadoRepartidor = manejarRepartidor(repartidorOpt, usuario, model);

		if (resultadoCliente != null) {
			return resultadoCliente;
		} else if (resultadoRestaurante != null) {
			return resultadoRestaurante;
		} else if (resultadoRepartidor != null) {
			return resultadoRepartidor;
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

	    if (rol == null) {
	        model.addAttribute("rolNulo", "Ingresa un tipo de usuario");
	        return REGISTRO_STR;
	    }

	    boolean registroExitoso;
	    switch (rol) {
	        case 1:
	            registroExitoso = procesarRegistroCliente(usuario, apellidosCliente, dniCliente, model);
	            break;
	        case 2:
	            registroExitoso = procesarRegistroRestaurante(usuario, codigoPostalRestaurante, calleRestaurante,
	                    numeroRestaurante, complementoRestaurante, municipioRestaurante, cifRestaurante, model);
	            break;
	        case 3:
	            registroExitoso = procesarRegistroRepartidor(usuario, apellidosRepartidor, nifRepartidor, model);
	            break;
	        default:
	            model.addAttribute("rolNulo", "Ingresa un tipo de usuario");
	            return REGISTRO_STR;
	    }

	    if (registroExitoso) {
	        return LOGIN_STR;
	    } else {
	        return REGISTRO_STR;
	    }
	}

	private boolean procesarRegistroCliente(Usuario usuario, String apellidosCliente, String dniCliente, Model model) {
	    if (clienteDAO.findById(usuario.getIdUsuario()).isPresent()) {
	        model.addAttribute(ERROR_STR, USUARIO_EXISTE_STR);
	        return false;
	    }
	    return registrarCliente(usuario, apellidosCliente, dniCliente, model) != 0;
	}

	private boolean procesarRegistroRestaurante(Usuario usuario, String codigoPostal, String calle, String numero,
	                                            String complemento, String municipio, String cif, Model model) {
	    if (restauranteDAO.findById(usuario.getIdUsuario()).isPresent()) {
	        model.addAttribute(ERROR_STR, USUARIO_EXISTE_STR);
	        return false;
	    }
	    return registrarRestaurante(usuario, codigoPostal, calle, numero, complemento, municipio, cif, model) != 0;
	}

	private boolean procesarRegistroRepartidor(Usuario usuario, String apellidosRepartidor, 
											   String nifRepartidor, Model model) {
	    if (repartidorDAO.findById(usuario.getIdUsuario()).isPresent()) {
	        model.addAttribute(ERROR_STR, USUARIO_EXISTE_STR);
	        return false;
	    }
	    return registrarRepartidor(usuario, apellidosRepartidor, nifRepartidor, model) != 0;
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
		if (usuario != null && comprobarCondicionesRegistrarRestaurante(codigoPostalRestaurante, calleRestaurante,
				numeroRestaurante, complementoRestaurante, municipioRestaurante, cifRestaurante)) {
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
		} else {
			model.addAttribute(ERROR_STR, RELLENA_CAMPOS);
			return 0;
		}

	}

	private boolean comprobarCondicionesRegistrarRestaurante(String codigoPostal, String calle, String numero,
			String complemento, String municipio, String cif) {

		boolean correcto = true;

		if (codigoPostal.isEmpty() || calle.isEmpty() || numero.isEmpty()) {
			correcto = false;
		}
		if (complemento.isEmpty() || municipio.isEmpty() || cif.isEmpty()) {
			correcto = false;
		}

		return correcto;

	}

	private String manejarCliente(Optional<Cliente> clienteOpt, Usuario usuario, Model model) {
		if (clienteOpt.isPresent()) {
			Cliente cliente = clienteOpt.get();
			if (cliente.getPass().equals(usuario.getPass())) {
				return "redirect:/menucliente/" + cliente.getIdUsuario();
			} else {
				model.addAttribute(ERROR_STR, CONTRASENA_STR);
				return LOGIN_STR;
			}
		}
		return null;
	}

	private String manejarRestaurante(Optional<Restaurante> restauranteOpt, Usuario usuario, Model model) {
		if (restauranteOpt.isPresent()) {
			Restaurante restaurante = restauranteOpt.get();
			if (restaurante.getPass().equals(usuario.getPass())) {
				return "redirect:/menurestaurante/" + restaurante.getIdUsuario();
			} else {
				model.addAttribute(ERROR_STR, CONTRASENA_STR);
				return LOGIN_STR;
			}
		}
		return null;
	}

	private String manejarRepartidor(Optional<Repartidor> repartidorOpt, Usuario usuario, Model model) {
		if (repartidorOpt.isPresent()) {
			Repartidor repartidor = repartidorOpt.get();
			if (repartidor.getPass().equals(usuario.getPass())) {
				return "redirect:/menurepartidor/" + repartidor.getIdUsuario();
			} else {
				model.addAttribute(ERROR_STR, CONTRASENA_STR);
				return LOGIN_STR;
			}
		}
		return null;
	}

}