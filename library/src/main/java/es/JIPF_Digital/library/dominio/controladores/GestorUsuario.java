package es.JIPF_Digital.library.dominio.controladores;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.JIPF_Digital.library.dominio.entidades.*;

@Controller
public class GestorUsuario {
	@GetMapping("/login")
		public String loginForm(Model model) {
			model.addAttribute("usuario", new Usuario());
			return "login";
	}
	
	@PostMapping("/login")
	public String loginSubmit(@ModelAttribute Usuario usuario, Model model) {
		model.addAttribute("usuario", usuario);
		
		return "menucliente";
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
				break;
			case 2:
				Direccion dir = new Direccion(codigoPostalRestaurante, calleRestaurante, numeroRestaurante, complementoRestaurante, municipioRestaurante);
				Restaurante restaurante = new Restaurante(usuario.getIdUsuario(), usuario.getNombre(), usuario.getPass(), dir, cifRestaurante);
				break;
			case 3:
				Repartidor repartidor = new Repartidor(usuario.getIdUsuario(), usuario.getNombre(), usuario.getPass(), apellidosRepartidor, nifRepartidor);
				break;
			}
		
			return "login";
		}
	
	
	
	
	
}