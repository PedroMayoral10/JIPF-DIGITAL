package es.JIPF_Digital.library.dominio.controladores;

import es.JIPF_Digital.library.dominio.entidades.*;
import es.JIPF_Digital.library.persistencia.RestauranteDAO;
import es.JIPF_Digital.library.persistencia.ClienteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Collection;
import java.util.List;

@Controller
public class GestorRestaurantes {

    @Autowired
    private RestauranteDAO restauranteDAO;
    @Autowired
    private ClienteDAO clienteDAO;

    @GetMapping("@{/detallerestaurantes/{id}(id=${restaurante.id})}")
    public String verRestaurante(@PathVariable String idUsuario, Model model) {
        Restaurante restaurante = restauranteDAO.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));
        model.addAttribute("restaurante", restaurante);
        return "detallerestaurantes";
    }

    @GetMapping("/detallerestaurante/{id}")
    public String detalleRestaurante(@PathVariable("id") String id, Model model) {
        Restaurante restaurante = restauranteDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));
        model.addAttribute("restaurante", restaurante);
        return "detallerestaurante";
    }

    public void modificarMenu(String restauranteId, List<ItemMenu> nuevoMenu) { // Cambiado a String
        Restaurante restaurante = restauranteDAO.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));
        restaurante.getCartasMenu().forEach(cartaMenu -> cartaMenu.setItems(nuevoMenu));
        restauranteDAO.save(restaurante);
    }

    public void actualizarMenuPrincipal(Restaurante restaurante, Collection<ItemMenu> nuevosItems) {
        CartaMenu menuPrincipal = restaurante.getMenuPrincipal();

        if (menuPrincipal != null) {
            menuPrincipal.setItems(nuevosItems);
        } else {
            CartaMenu nuevaCarta = new CartaMenu(restaurante, "Menú Principal");
            nuevaCarta.setItems(nuevosItems);
            restaurante.getCartasMenu().add(nuevaCarta);
        }
    }

    public List<Restaurante> listarRestaurantes() {
        return restauranteDAO.findAll();
    }

    public Restaurante obtenerRestaurantePorId(String idUsuario) {
        return restauranteDAO.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));
    }

    private ItemMenu crearItem(String nombre, TipoItemMenu tipo, double precio) {
        return new ItemMenu(nombre, tipo, precio);
    }

    public ItemMenu agregarNuevoItem(String nombre, TipoItemMenu tipo, double precio) {
        return crearItem(nombre, tipo, precio);
    }
}