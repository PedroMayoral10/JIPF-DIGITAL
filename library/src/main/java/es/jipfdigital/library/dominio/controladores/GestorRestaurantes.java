package es.jipfdigital.library.dominio.controladores;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.jipfdigital.library.dominio.entidades.CartaMenu;
import es.jipfdigital.library.dominio.entidades.ItemMenu;
import es.jipfdigital.library.dominio.entidades.Restaurante;
import es.jipfdigital.library.dominio.entidades.TipoItemMenu;
import es.jipfdigital.library.persistencia.CartaMenuDAO;
import es.jipfdigital.library.persistencia.ItemMenuDAO;
import es.jipfdigital.library.persistencia.RestauranteDAO;

@Controller
public class GestorRestaurantes {

	private static final String SUCCESS = "success";
	private static final String ERROR = "error";
	private static final String REDIRIGIRNUEVOITEM = "redirect:/nuevoitem/";
	private static final String REDIRIGIRMODIFICARMENU = "redirect:/modificarmenu/";
	
	
	@Autowired
	private RestauranteDAO restauranteDAO;
	@Autowired
	private CartaMenuDAO cartamenuDAO;
	@Autowired
	private ItemMenuDAO itemDAO;


	/*
	 * GETMAPPINGS
	 */

	@GetMapping("menurestaurante/{id}")
	public String MenuRestaurante(@PathVariable("id") String idRestaurante, Model model) {
		Optional <Restaurante> restauranteOptional = restauranteDAO.findById(idRestaurante);
		if(restauranteOptional.isPresent()){
			model.addAttribute(restauranteOptional.get());
			return "menurestaurante";
		}else{
			throw new IllegalArgumentException("Restaurante no encontrado");
		}
	}

	@GetMapping("altamenu/{id}")
	public String darAltaMenu(@PathVariable("id") String idRestaurante, Model model) {
		Optional <Restaurante> restauranteOptional = restauranteDAO.findById(idRestaurante);
		if(restauranteOptional.isEmpty()){
			return ERROR; 
		}
		Restaurante restaurante = restauranteOptional.get();
		model.addAttribute(restaurante);
		return "altamenu";
	}

	@GetMapping("modificarmenu/{id}")
	public String modificarMenu(@PathVariable("id") String idRestaurante, Model model) {
		if(idRestaurante == null || idRestaurante.trim().isEmpty()){
			throw new IllegalArgumentException("El ID del restaurante no puede estar vacio");
		}
		// Obtener todos los menús del restaurante por su ID, incluyendo los ítems
		List<CartaMenu> menus = cartamenuDAO.findAllByRestauranteId(idRestaurante);
		model.addAttribute("menus", menus);
		model.addAttribute("idRestaurante", idRestaurante);
		return "modificarmenu"; 
	}

	@GetMapping("nuevoitem/{id}")
	public String nuevoItem(@PathVariable("id") Long idMenu, Model model) {
		// Obtener todos los menús del restaurante por su ID, incluyendo los ítems
		CartaMenu menu = cartamenuDAO.findById(idMenu).get();
		model.addAttribute("menu", menu);
		model.addAttribute("idRestaurante", menu.getRestaurante().getIdUsuario());
		return "nuevoitem"; 
	}

	/*
	 * POSTMAPPINGS
	 */

	@PostMapping("altamenu/{id}")
	public String postAltaMenu(@PathVariable("id") String idRestaurante,
			@RequestParam(value = "nombreMenu", required = false) String nombreMenu,
			@RequestParam(value = "nombre", required = false) String nombreItem,
			@RequestParam(value = "precio", required = false) Double precio,
			@RequestParam(value = "tipo", required = false) String tipoItem, RedirectAttributes redirectAttributes) {
        if (nombreMenu == null || nombreItem == null || precio == null) {
            redirectAttributes.addFlashAttribute(ERROR, "Faltan datos obligatorios.");
			return "redirect:/errorPage"; 
		}
		Restaurante restaurante = restauranteDAO.getById(idRestaurante);
		
		if (!comprobarSiNoExiste(nombreMenu, idRestaurante)) {
			CartaMenu cartamenu = cartamenuDAO.findByNombreAndRestauranteId(nombreMenu, idRestaurante);
			ItemMenu item = crearItem(nombreItem, tipoItem, precio);
			cartamenu.getItems().add(item);
			cartamenuDAO.save(cartamenu);
			redirectAttributes.addFlashAttribute(SUCCESS, "El item se ha creado correctamente.");
			return REDIRIGIRNUEVOITEM + cartamenu.getId();
		}
		CartaMenu cartamenu = new CartaMenu();
		cartamenu.setNombre(nombreMenu);
		cartamenu.setRestaurante(restaurante);
		cartamenuDAO.save(cartamenu);

		ItemMenu item = crearItem(nombreItem, tipoItem, precio);

		cartamenu.getItems().add(item);
		cartamenuDAO.save(cartamenu);

		// Agregar mensaje de éxito
		redirectAttributes.addFlashAttribute(SUCCESS, "El menú se ha creado correctamente.");

		return REDIRIGIRNUEVOITEM + cartamenu.getId();
	}

	@PostMapping("modificarmenu/{id}")
	public String postModMenu(@PathVariable("id") String idRestaurante,
			@RequestParam(value = "menuId", required = false) Long idMenu, Model model,
			RedirectAttributes redirectAttributes) {
				
		if (idMenu == null) {
			redirectAttributes.addFlashAttribute(ERROR, "ID del menú no proporcionado");
			return REDIRIGIRMODIFICARMENU + idRestaurante;
		}

		Optional<CartaMenu> optionalCartaMenu = cartamenuDAO.findById(idMenu);
    
				
		if (optionalCartaMenu.isEmpty()) {
			redirectAttributes.addFlashAttribute(ERROR, "Menú no encontrado");
			return REDIRIGIRMODIFICARMENU + idRestaurante;
		}
		
		CartaMenu cartamenu = optionalCartaMenu.get();
		List<CartaMenu> menus = cartamenuDAO.findAll();
		
		if (!cartamenu.getItems().isEmpty())
			redirectAttributes.addFlashAttribute("items", cartamenu.getItems());
		redirectAttributes.addFlashAttribute("menus", menus);
		redirectAttributes.addFlashAttribute("menu_seleccionado", cartamenu.getId());

		return REDIRIGIRMODIFICARMENU + idRestaurante;
	}

	@PostMapping("nuevoitem/{id}")
	public String postNuevoItem(@PathVariable("id") Long idMenu,
			@RequestParam(value = "nombre", required = false) String nombreItem,
			@RequestParam(value = "precio", required = false) Double precio,
			@RequestParam(value = "tipo", required = false) String tipoItem,
			RedirectAttributes redirectAttributes, Model model) {
		if (idMenu == null) {
			redirectAttributes.addFlashAttribute(ERROR, "El ID del menú no puede ser nulo");
			return "redirect:/error"; 
		}
		ItemMenu item = crearItem(nombreItem, tipoItem, precio);
		
		CartaMenu menu = cartamenuDAO.findById(idMenu).get();
		menu.getItems().add(item);
		cartamenuDAO.save(menu);
		redirectAttributes.addFlashAttribute(SUCCESS,"Item añadido con exito");
		return REDIRIGIRNUEVOITEM + idMenu;
	}

	/*
	 * DELETEMAPPINGS
	 */

	@DeleteMapping("/eliminaritem/{itemId}")
	public ResponseEntity<Void> deleteItem(@PathVariable("itemId") Long itemId) {
		Optional <ItemMenu> item = itemDAO.findById(itemId);
		if (item.isPresent()) {
			itemDAO.delete(item.get());
			return ResponseEntity.ok().build(); // Responde con 200 OK si se eliminó correctamente
		} else {
			return ResponseEntity.notFound().build(); 
		}
	}
	
	@DeleteMapping("/eliminarmenu/{menuId}")
	public ResponseEntity<Void> deleteMenu(@PathVariable("menuId") Long menuId) {
		Optional <CartaMenu> optionalMenu = cartamenuDAO.findById(menuId);
		if (optionalMenu.isPresent()) {
			CartaMenu menu = optionalMenu.get();
			Collection <ItemMenu> items = menu.getItems();
			for (ItemMenu item : items) {
				itemDAO.delete(item);
			}
			cartamenuDAO.delete(menu);
			
			return ResponseEntity.ok().build(); // Responde con 200 OK si se eliminó correctamente
		} else {
			return ResponseEntity.notFound().build(); // Responde con 404 si el ítem no existe
		}
	}

	private boolean comprobarSiNoExiste(String nombre, String idRestaurante) {
		CartaMenu cartamenu = cartamenuDAO.findByNombreAndRestauranteId(nombre, idRestaurante);

		if (cartamenu != null) {
			return false;
		} else {
			return true;
		}
	}

	private ItemMenu crearItem(String nombreItem, String tipoItem, Double precio){
		if (precio == null) {
			precio = 0.0;
		}
		ItemMenu item;
		if (tipoItem.equals("COMIDA")) {
			item = new ItemMenu(nombreItem, TipoItemMenu.COMIDA, precio);
		} else if (tipoItem.equals("BEBIDA")) {
			item = new ItemMenu(nombreItem, TipoItemMenu.BEBIDA, precio);
		} else {
			item = new ItemMenu(nombreItem, TipoItemMenu.POSTRE, precio);
		}
		return item;
	}
}