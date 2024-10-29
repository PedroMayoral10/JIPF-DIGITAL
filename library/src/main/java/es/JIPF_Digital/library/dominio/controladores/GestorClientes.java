package es.JIPF_Digital.library.dominio.controladores;

import es.JIPF_Digital.library.persistencia.*;
import es.JIPF_Digital.library.dominio.entidades.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GestorClientes {

    @Autowired
    private RestauranteDAO restauranteDAO; // Declarar el repositorio

    @Autowired
    private ClienteDAO clienteDAO;

    @GetMapping("/menucliente")
    public String MenuCliente(Model model) {
        return "menucliente";
    }

    @GetMapping("/listarestaurantes")
    public String ListaRestaurante(Model model, @RequestParam(required = false) String codigoPostal, @RequestParam(required = false) String texto) {
        List<Restaurante> restaurantes;

        if (codigoPostal != null && !codigoPostal.isEmpty() && texto != null && !texto.isEmpty()) {
            // Busca restaurantes por código postal y texto en el nombre
            restaurantes = restauranteDAO.findByDireccion_CodigoPostalAndNombreContaining(codigoPostal, texto);
        } else if (codigoPostal != null && !codigoPostal.isEmpty()) {
            // Busca restaurantes solo por código postal
            restaurantes = restauranteDAO.findByDireccion_CodigoPostal(codigoPostal);
        } else {
            // Si no se proporciona ningún filtro, devuelve todos los restaurantes
            restaurantes = restauranteDAO.findAll();
        }

        model.addAttribute("restaurantes", restaurantes);
        return "listarestaurantes"; // Retorna el nombre del HTML
    }

   


    /**
     * 
     * @param zona
     */
    public List<Restaurante> buscarRestaurante(String zona) {
        // Implementar la búsqueda de restaurantes por zona
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * @param cliente
     * @param r
     */
    public void favorito(Cliente cliente, Restaurante r) {
        // Implementar la lógica para agregar un restaurante a los favoritos
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * @param nombre
     * @param apellido
     * @param d
     */
    public Cliente registrarCliente(String nombre, String apellido, Direccion d) {
        // Implementar la lógica para registrar un nuevo cliente
        throw new UnsupportedOperationException();
    }

    /**
     * 
     * @param calle
     * @param numero
     * @param complemento
     * @param cp
     * @param municipio
     */
    private Direccion altaDirecion(String calle, String numero, String complemento, String cp, String municipio) {
        // Implementar la lógica para crear una nueva dirección
        throw new UnsupportedOperationException();
    }
}
