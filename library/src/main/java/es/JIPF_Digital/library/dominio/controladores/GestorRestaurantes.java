package es.JIPF_Digital.library.dominio.controladores;

import es.JIPF_Digital.library.dominio.entidades.*;
import es.JIPF_Digital.library.persistencia.*;
import es.JIPF_Digital.library.persistencia.RestauranteDAO;
import es.JIPF_Digital.library.persistencia.ClienteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Collection;
import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GestorRestaurantes {

    @Autowired
    private RestauranteDAO restauranteDAO;
    @Autowired
    private CartaMenuDAO cartamenuDAO;
    @Autowired
    private ItemMenuDAO itemDAO;
    
    
    
    @GetMapping("menurestaurante/{id}")
    public String MenuRestaurante(@PathVariable("id") String idRestaurante,Model model) {
    	Restaurante restaurante = restauranteDAO.findById(idRestaurante).orElse(null);
    	model.addAttribute(restaurante);
    	return "menurestaurante";
    }
    
    @GetMapping("altamenu/{id}")
    public String darAltaMenu(@PathVariable("id") String idRestaurante,Model model) {
    	Restaurante restaurante = restauranteDAO.findById(idRestaurante).orElse(null);
    	model.addAttribute(restaurante);
    	return "altamenu";
    }
    
    @GetMapping("modificarmenu/{id}")
    public String modificarMenu(@PathVariable("id") String idRestaurante,Model model) {
    	Restaurante restaurante = restauranteDAO.findById(idRestaurante).orElse(null);
    	model.addAttribute(restaurante);
    	return "modificarmenu";
    }
    
    @PostMapping("altamenu/{id}")
    public String postAltaMenu(@PathVariable("id") String idRestaurante,
            @RequestParam(value = "nombreMenu", required = false) String nombreMenu,
            @RequestParam(value = "nombre", required = false) String nombreItem,
            @RequestParam(value = "precio", required = false) double precio,
            @RequestParam(value = "tipo", required = false) String tipo_menu,
            RedirectAttributes redirectAttributes) {
    	
    	Restaurante restaurante = restauranteDAO.getById(idRestaurante);
    	if(!comprobarSiExiste(nombreMenu, idRestaurante)) {
    		CartaMenu cartamenu = cartamenuDAO.findByNombreAndRestauranteId(nombreMenu, idRestaurante);
    		ItemMenu item;
            if (tipo_menu.equals("COMIDA")) {
                item = new ItemMenu(nombreItem, TipoItemMenu.COMIDA, precio);
            } else if (tipo_menu.equals("BEBIDA")) {
                item = new ItemMenu(nombreItem, TipoItemMenu.BEBIDA, precio);
            } else {
                item = new ItemMenu(nombreItem, TipoItemMenu.POSTRE, precio);
            }
            
            cartamenu.getItems().add(item);
            cartamenuDAO.save(cartamenu);
            redirectAttributes.addFlashAttribute("success", "El item se ha creado correctamente.");
        	return "redirect:/altamenu/"+idRestaurante;
    	}
    	CartaMenu cartamenu = new CartaMenu();
        cartamenu.setNombre(nombreMenu);
        cartamenu.setRestaurante(restaurante);
        cartamenuDAO.save(cartamenu);

        ItemMenu item;
        if (tipo_menu.equals("COMIDA")) {
            item = new ItemMenu(nombreItem, TipoItemMenu.COMIDA, precio);
        } else if (tipo_menu.equals("BEBIDA")) {
            item = new ItemMenu(nombreItem, TipoItemMenu.BEBIDA, precio);
        } else {
            item = new ItemMenu(nombreItem, TipoItemMenu.POSTRE, precio);
        }

        cartamenu.getItems().add(item);
        cartamenuDAO.save(cartamenu);

        // Agregar mensaje de éxito
        redirectAttributes.addFlashAttribute("success", "El menú se ha creado correctamente.");
    	
    	return "redirect:/altamenu/"+idRestaurante;
    }
    
    private boolean comprobarSiExiste(String nombre, String idRestaurante) {
    	CartaMenu cartamenu = cartamenuDAO.findByNombreAndRestauranteId(nombre, idRestaurante);
    	System.out.println(cartamenu.getNombre());
    	if(cartamenu != null) {
    		return true;
    	}else {
    		return false;
    	}
    }
}