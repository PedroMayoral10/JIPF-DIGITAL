package es.JIPF_Digital.library.dominio.controladores;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.JIPF_Digital.library.dominio.entidades.*;
import es.JIPF_Digital.library.persistencia.*;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class GestorUsuario {
	
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private RestauranteDAO restauranteDAO;
	@Autowired
	private RepartidorDAO repartidorDAO;
	
	@GetMapping("/")
	public String redirigirLogin(Model model) {
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "login";
	}
	
	@PostMapping("/login")
	public String loginSubmit(@ModelAttribute Usuario usuario, Model model) {
		model.addAttribute("usuario", usuario);
		Cliente cliente = clienteDAO.findById(usuario.getIdUsuario()).orElse(null);
	    Restaurante restaurante = restauranteDAO.findById(usuario.getIdUsuario()).orElse(null);
	    Repartidor repartidor = repartidorDAO.findById(usuario.getIdUsuario()).orElse(null);
		if(cliente != null) {
			if(cliente.getPass().equals(usuario.getPass())) {
				return "redirect:/menucliente/"+cliente.getIdUsuario();
			}else {
				 model.addAttribute("error", "Contraseña incorrecta, pruebe otra vez");
				 return "login";
			}
		}else if(restaurante != null) {
			if(restaurante.getPass().equals(usuario.getPass())) {
				return "redirect:/menurestaurante";
			}else {
				 model.addAttribute("error", "Contraseña incorrecta, pruebe otra vez");
				 return "login";
			}
		}else if(repartidor!=null) {
			if(repartidor.getPass().equals(usuario.getPass())) {
				return "redirect:/menurepartidor";
			}else {
				 model.addAttribute("error", "Contraseña incorrecta, pruebe otra vez");
				 return "login";
			}
		}else {
			model.addAttribute("error", "El usuario no existe, pruebe otra vez");
			return "login";
		}
	}
	
	@GetMapping("/registro")
		public String registroForm(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "registro";
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
            @RequestParam(value = "rol", required = false) int rol,
            Model model) {
			System.out.println(rol);
			switch(rol) {
			case 1:
				Cliente cliente = new Cliente(usuario.getIdUsuario(), usuario.getNombre(), usuario.getPass(), apellidosCliente, dniCliente);
				clienteDAO.save(cliente);
				break;
			case 2:
				Direccion dir = new Direccion(codigoPostalRestaurante, calleRestaurante, numeroRestaurante, complementoRestaurante, municipioRestaurante);
				Restaurante restaurante = new Restaurante(usuario.getIdUsuario(), usuario.getNombre(), usuario.getPass(), dir, cifRestaurante);
				restauranteDAO.save(restaurante);
				break;
			case 3:
				Repartidor repartidor = new Repartidor(usuario.getIdUsuario(), usuario.getNombre(), usuario.getPass(), apellidosRepartidor, nifRepartidor);
				repartidorDAO.save(repartidor);
				break;
			}
		
			return "login";
		}
	
	
	
	
	
}