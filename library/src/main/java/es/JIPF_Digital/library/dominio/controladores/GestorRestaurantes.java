package es.JIPF_Digital.library.dominio.controladores;

import es.JIPF_Digital.library.dominio.entidades.*;
import es.JIPF_Digital.library.persistencia.*; // Importa tu repositorio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Collection;
import java.util.List;

@Controller
public class GestorRestaurantes {

    @Autowired
    private RestauranteDAO restauranteDAO; // Declarar el repositorio
    @Autowired
    private ClienteDAO clienteDAO;
    

    
  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  
    /**
     * Modifica el menú de un restaurante, añadiendo o actualizando items en el menú.
     * 
     * @param restauranteId ID del restaurante a modificar
     * @param nuevoMenu    Nueva colección de items para el menú
     */
    public void modificarMenu(String restauranteId, List<ItemMenu> nuevoMenu) {
        Restaurante restaurante = restauranteDAO.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));

        // Reemplaza el menú actual con el nuevo menú
        restaurante.getCartasMenu().forEach(cartaMenu -> cartaMenu.setItems(nuevoMenu));

        restauranteDAO.save(restaurante);
    }

    /**
     * Método para actualizar los items del menú principal de un restaurante.
     * 
     * @param restaurante  El restaurante cuyo menú se va a actualizar
     * @param nuevosItems La nueva colección de items del menú
     */
    public void actualizarMenuPrincipal(Restaurante restaurante, Collection<ItemMenu> nuevosItems) {
        CartaMenu menuPrincipal = restaurante.getMenuPrincipal();

        if (menuPrincipal != null) {
            // Si ya existe un menú principal, actualizamos sus items
            menuPrincipal.setItems(nuevosItems);
        } else {
            // Si no existe un menú principal, creamos uno nuevo
            CartaMenu nuevaCarta = new CartaMenu(restaurante, "Menú Principal");
            nuevaCarta.setItems(nuevosItems);
            restaurante.getCartasMenu().add(nuevaCarta);
        }
    }

    /**
     * Obtiene una lista de todos los restaurantes.
     * 
     * @return Lista de restaurantes.
     */
    public List<Restaurante> listarRestaurantes() {
        return restauranteDAO.findAll();
    }

    /**
     * Obtiene un restaurante por su ID.
     * 
     * @param idUsuario ID del restaurante a obtener.
     * @return Restaurante correspondiente al ID.
     */
    public Restaurante obtenerRestaurantePorId(String idUsuario) {
        return restauranteDAO.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));
    }

    /**
     * Crea un nuevo item de menú.
     * Este método puede ser privado si sólo se usa internamente para modificar el menú.
     * 
     * @param nombre Nombre del item de menú
     * @param precio Precio del item de menú
     * @param tipo   Tipo del item de menú
     * @return El item de menú creado
     */
    private ItemMenu crearItem(String nombre,TipoItemMenu tipo, double precio) {
        return new ItemMenu(nombre, tipo, precio);
    }

    /**
     * Agrega un nuevo item al menú.
     * 
     * @param nombre Nombre del item
     * @param precio Precio del item
     * @param tipo   Tipo del item
     * @return El item creado
     */
    public ItemMenu agregarNuevoItem(String nombre,TipoItemMenu tipo, double precio) {
        return crearItem(nombre, tipo, precio);
    }
    
    
    
    
    

}
