package es.JIPF_Digital.library.dominio.controladores;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.JIPF_Digital.library.dominio.entidades.Cliente;
import es.JIPF_Digital.library.dominio.entidades.Direccion;
import es.JIPF_Digital.library.dominio.entidades.Repartidor;
import es.JIPF_Digital.library.dominio.entidades.Restaurante;
import es.JIPF_Digital.library.dominio.entidades.Usuario;
import es.JIPF_Digital.library.persistencia.ClienteDAO;
import es.JIPF_Digital.library.persistencia.RepartidorDAO;
import es.JIPF_Digital.library.persistencia.RestauranteDAO;
import java.util.Optional;


@Controller
public class GestorUsuario {
	
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private RestauranteDAO restauranteDAO;
	@Autowired
	private RepartidorDAO repartidorDAO;
	final String UsuarioStr = "usuario";
	final String LoginStr = "login";
	final String ErrorStr = "error";
	final String ContasenaStr = "Contraseña incorrecta, pruebe otra vez";
	
	
	/*
	 * GETMAPPINGS
	 */
	
	@GetMapping("/")
	public String redirigirLogin(Model model) {
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute(UsuarioStr, new Usuario());
		return LoginStr;
	}
	
	@GetMapping("/registro")
	public String registroForm(Model model) {
		model.addAttribute(UsuarioStr, new Usuario());
		return "registro";
	}

	/*
	 * POSTMAPPINGS
	 */
	
	@PostMapping("/login")
	public String loginSubmit(@ModelAttribute Usuario usuario, Model model) {
		model.addAttribute(UsuarioStr, usuario);
		Optional <Cliente> clienteOpt = clienteDAO.findById(usuario.getIdUsuario());
	    Optional <Restaurante> restauranteOpt = restauranteDAO.findById(usuario.getIdUsuario());
	    Optional <Repartidor> repartidorOpt = repartidorDAO.findById(usuario.getIdUsuario());
		if(clienteOpt.isPresent()) {
			Cliente cliente = clienteOpt.get(); 
			if(cliente.getPass().equals(usuario.getPass())) {
				return "redirect:/menucliente/"+cliente.getIdUsuario();
			}else {
				model.addAttribute( ErrorStr, ContasenaStr);
				return LoginStr;
			}
		}else if(restauranteOpt.isPresent()) {
			Restaurante restaurante = restauranteOpt.get();
			if(restaurante.getPass().equals(usuario.getPass())) {
				return "redirect:/menurestaurante/"+restaurante.getIdUsuario();
			}else {
				model.addAttribute(ErrorStr, ContasenaStr);
				return LoginStr;
			}
		}else if(repartidorOpt.isPresent()) {
			Repartidor repartidor = repartidorOpt.get();
			if(repartidor.getPass().equals(usuario.getPass())) {
				return "redirect:/menurepartidor/"+repartidor.getIdUsuario();
			}else {
				model.addAttribute(ErrorStr, ContasenaStr);
				return LoginStr;
			}
		}else {
			model.addAttribute(ErrorStr, "El usuario no existe, pruebe otra vez");
			return LoginStr;
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
			switch(rol) {
			case 1:
				Cliente cliente = new Cliente(usuario.getIdUsuario(), usuario.getNombre(), usuario.getPass(),
				 apellidosCliente, dniCliente);
				clienteDAO.save(cliente);
				break;
			case 2:
				Direccion dir = new Direccion(codigoPostalRestaurante, calleRestaurante, numeroRestaurante, complementoRestaurante, municipioRestaurante);
				Restaurante restaurante = new Restaurante(usuario.getIdUsuario(), usuario.getNombre(), usuario.getPass(), dir, cifRestaurante);
				restauranteDAO.save(restaurante);
				break;
			case 3:
				Repartidor repartidor = new Repartidor(usuario.getIdUsuario(), usuario.getNombre(), 
				usuario.getPass(),apellidosRepartidor, nifRepartidor);
				repartidorDAO.save(repartidor);
				break;
			}
		
			return LoginStr;
		}
	



		
}