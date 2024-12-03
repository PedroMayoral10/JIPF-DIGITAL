package es.jipfdigital.library.dominio.controladores;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.jipfdigital.library.dominio.entidades.CartaMenu;
import es.jipfdigital.library.dominio.entidades.Restaurante;
import es.jipfdigital.library.persistencia.CartaMenuDAO;
import es.jipfdigital.library.persistencia.RestauranteDAO;

public class GestorRestauranteTest {
    @InjectMocks
    private GestorRestaurantes menuRestaurante; // Clase donde está el método `postAltaMenu`

    @Mock
    private RestauranteDAO restauranteDAO;

    @Mock
    private CartaMenuDAO cartamenuDAO;

    @Mock
    private RedirectAttributes redirectAttributes;


    public GestorRestauranteTest() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testPostAltaMenu_EsNull() {            
            // Configurar mocks
            String idRestaurante = "123";
    when(restauranteDAO.getById(idRestaurante)).thenReturn(new Restaurante());

    // Ejecutar método
    String result = menuRestaurante.postAltaMenu(
        idRestaurante,
        null, // nombreMenu
        null, // nombreItem
        null, // precio
        "COMIDA", // tipoItem
        redirectAttributes
    );

    // Verificar comportamiento esperado
    verify(redirectAttributes).addFlashAttribute("error", "Faltan datos obligatorios.");
    assertEquals("redirect:/errorPage", result);
            
        
}

    @Test
    public void testPostAltaMenu_NuevoMenu() {
            // Datos de entrada
            String idRestaurante = "1";
            String nombreMenu = "Menu Especial";
            String nombreItem = "Plato Fuerte";
            Double precio = 25.0;
            String tipoItem = "COMIDA";
        
            // Configurar el mock
            Restaurante restaurante = new Restaurante();
            restaurante.setIdUsuario(idRestaurante);
        
            CartaMenu cartaMenuMock = new CartaMenu();
            cartaMenuMock.setNombre(nombreMenu);
            cartaMenuMock.setItems(new ArrayList<>());
            when(cartamenuDAO.findByNombreAndRestauranteId(nombreMenu, idRestaurante)).thenReturn(cartaMenuMock);
            
            // Ejecución del método
            String result = menuRestaurante.postAltaMenu(idRestaurante, nombreMenu, nombreItem, precio, tipoItem, redirectAttributes);
        
            // Verificaciones
            verify(restauranteDAO).getById(idRestaurante);
            verify(cartamenuDAO, times(1)).save(any(CartaMenu.class));  // Solo se debe llamar una vez
            verify(redirectAttributes).addFlashAttribute(eq("success"), anyString());
        
            // Comprobar resultado
            // Verificar que el resultado contiene la URL de redirección correcta
            assertTrue(result.contains("/nuevoitem/"), "La URL de redirección no es la esperada.");
        }
    }

    