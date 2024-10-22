package es.JIPF_Digital.library.dominio.controladores;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.JIPF_Digital.library.dominio.entidades.Usuario;

@Controller
public class GestorLogin {
	
	@GetMapping("/login")
		public String loginForm(Model model) {
			model.addAttribute("usuario", new Usuario());
			return "login";
	}
	
	@GetMapping("/menuprincipal")
		public String menu() {
		
			return "menuprincipal";
		}
	
	@PostMapping("/login")
	public String loginSubmit(@ModelAttribute Usuario usuario, Model model) {
		model.addAttribute("usuario", usuario);
		return "menuprincipal";
	}
	
	
}