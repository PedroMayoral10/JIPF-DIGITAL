package es.jipfdigital.library.dominio.controladores;

import java.util.Collection;
import java.util.List;

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

	@Autowired
	private RestauranteDAO restauranteDAO;
	@Autowired
	private CartaMenuDAO cartamenuDAO;
	@Autowired
	private ItemMenuDAO itemDAO;

	private String success = "success";
	private String redirigirNuevoItem = "redirect:/nuevoitem/";

	/*
	 * GETMAPPINGS
	 */

	@GetMapping("menurestaurante/{id}")
	public String MenuRestaurante(@PathVariable("id") String idRestaurante, Model model) {
		Restaurante restaurante = restauranteDAO.findById(idRestaurante).get();
		model.addAttribute(restaurante);
		return "menurestaurante";
	}

	@GetMapping("altamenu/{id}")
	public String darAltaMenu(@PathVariable("id") String idRestaurante, Model model) {
		Restaurante restaurante = restauranteDAO.findById(idRestaurante).get();
		model.addAttribute(restaurante);
		return "altamenu";
	}

	@GetMapping("modificarmenu/{id}")
	public String modificarMenu(@PathVariable("id") String idRestaurante, Model model) {
		// Obtener todos los menús del restaurante por su ID, incluyendo los ítems
		List<CartaMenu> menus = cartamenuDAO.findAllByRestauranteId(idRestaurante);
		model.addAttribute("menus", menus);
		model.addAttribute("idRestaurante", idRestaurante);
		return "modificarmenu"; // Asegúrate de que el nombre de la vista coincide
	}

	@GetMapping("nuevoitem/{id}")
	public String nuevoItem(@PathVariable("id") Long idMenu, Model model) {
		// Obtener todos los menús del restaurante por su ID, incluyendo los ítems
		CartaMenu menu = cartamenuDAO.findById(idMenu).get();
		model.addAttribute("menu", menu);
		model.addAttribute("idRestaurante", menu.getRestaurante().getIdUsuario());
		return "nuevoitem"; // Asegúrate de que el nombre de la vista coincide
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
		Restaurante restaurante = restauranteDAO.getById(idRestaurante);
		if (!comprobarSiNoExiste(nombreMenu, idRestaurante)) {
			CartaMenu cartamenu = cartamenuDAO.findByNombreAndRestauranteId(nombreMenu, idRestaurante);
			ItemMenu item = crearItem(nombreItem, tipoItem, precio);
			cartamenu.getItems().add(item);
			cartamenuDAO.save(cartamenu);
			redirectAttributes.addFlashAttribute(success, "El item se ha creado correctamente.");
			return redirigirNuevoItem + cartamenu.getId();
		}
		CartaMenu cartamenu = new CartaMenu();
		cartamenu.setNombre(nombreMenu);
		cartamenu.setRestaurante(restaurante);
		cartamenuDAO.save(cartamenu);

		ItemMenu item = crearItem(nombreItem, tipoItem, precio);

		cartamenu.getItems().add(item);
		cartamenuDAO.save(cartamenu);

		// Agregar mensaje de éxito
		redirectAttributes.addFlashAttribute(success, "El menú se ha creado correctamente.");

		return redirigirNuevoItem + cartamenu.getId();
	}

	@PostMapping("modificarmenu/{id}")
	public String postModMenu(@PathVariable("id") String idRestaurante,
			@RequestParam(value = "menuId", required = false) Long idMenu, Model model,
			RedirectAttributes redirectAttributes) {
		CartaMenu cartamenu = cartamenuDAO.findById(idMenu).get();
		List<CartaMenu> menus = cartamenuDAO.findAll();
		if (!cartamenu.getItems().isEmpty())
			redirectAttributes.addFlashAttribute("items", cartamenu.getItems());
		redirectAttributes.addFlashAttribute("menus", menus);
		redirectAttributes.addFlashAttribute("menu_seleccionado", cartamenu.getId());

		return "redirect:/modificarmenu/" + idRestaurante;
	}

	@PostMapping("nuevoitem/{id}")
	public String postAltaMenu(@PathVariable("id") Long idMenu,
			@RequestParam(value = "nombre", required = false) String nombreItem,
			@RequestParam(value = "precio", required = false) Double precio,
			@RequestParam(value = "tipo", required = false) String tipoItem,
			RedirectAttributes redirectAttributes, Model model) {
		ItemMenu item = crearItem(nombreItem, tipoItem, precio);
		
		CartaMenu menu = cartamenuDAO.findById(idMenu).get();
		menu.getItems().add(item);
		cartamenuDAO.save(menu);
		redirectAttributes.addFlashAttribute(success,"Item añadido con exito");
		return redirigirNuevoItem + idMenu;
	}

	/*
	 * DELETEMAPPINGS
	 */

	@DeleteMapping("/eliminaritem/{itemId}")
	public ResponseEntity<Void> deleteItem(@PathVariable("itemId") Long itemId) {
		ItemMenu item = itemDAO.findById(itemId).get();
		if (item != null) {
			itemDAO.delete(item);
			return ResponseEntity.ok().build(); // Responde con 200 OK si se eliminó correctamente
		} else {
			return ResponseEntity.notFound().build(); // Responde con 404 si el ítem no existe
		}
	}
	
	@DeleteMapping("/eliminarmenu/{menuId}")
	public ResponseEntity<Void> deleteMenu(@PathVariable("menuId") Long menuId) {
		CartaMenu menu = cartamenuDAO.findById(menuId).get();
		if (menu != null) {
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

	private ItemMenu crearItem(String nombreItem, String tipoItem, double precio){
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