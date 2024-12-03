package es.jipfdigital.library.dominio.controladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.jipfdigital.library.dominio.entidades.CartaMenu;
import es.jipfdigital.library.dominio.entidades.ItemMenu;
import es.jipfdigital.library.dominio.entidades.Restaurante;
import es.jipfdigital.library.dominio.entidades.TipoItemMenu;
import es.jipfdigital.library.persistencia.CartaMenuDAO;
import es.jipfdigital.library.persistencia.RestauranteDAO;

public class GestorRestauranteTest {
    @InjectMocks
    private GestorRestaurantes menuController; 

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
    public void testPostAltaMenuEsNull() {            
            
            String idRestaurante = "123";
    when(restauranteDAO.getById(idRestaurante)).thenReturn(new Restaurante());

    // Ejecutar método
    String result = menuController.postAltaMenu(
        idRestaurante,
        null, 
        null, 
        null, 
        "COMIDA", 
        redirectAttributes
    );

    
    verify(redirectAttributes).addFlashAttribute("error", "Faltan datos obligatorios.");
    assertEquals("redirect:/errorPage", result);
            
        
}

    @Test
    public void testPostAltaMenuNuevoMenu() {
            
            String idRestaurante = "1";
            String nombreMenu = "Menu Especial";
            String nombreItem = "Plato Fuerte";
            Double precio = 25.0;
            String tipoItem = "COMIDA";
        
            
            Restaurante restaurante = new Restaurante();
            restaurante.setIdUsuario(idRestaurante);
        
            CartaMenu cartaMenuMock = new CartaMenu();
            cartaMenuMock.setNombre(nombreMenu);
            cartaMenuMock.setItems(new ArrayList<>());
            when(cartamenuDAO.findByNombreAndRestauranteId(nombreMenu, idRestaurante)).thenReturn(cartaMenuMock);
            
            
            String result = menuController.postAltaMenu(idRestaurante, nombreMenu, nombreItem, precio, tipoItem, redirectAttributes);
        
            
            verify(restauranteDAO).getById(idRestaurante);
            verify(cartamenuDAO, times(1)).save(any(CartaMenu.class));  // Solo se debe llamar una vez
            verify(redirectAttributes).addFlashAttribute(eq("success"), anyString());
        
            
            assertTrue(result.contains("/nuevoitem/"), "La URL de redirección no es la esperada.");
        }

        public void testPostAltaMenu_CrearNuevoMenuConNuevoItem() {
            // Preparar datos de entrada
            String idRestaurante = "1";
            String nombreMenu = "Menu Especial";
            String nombreItem = "Ensalada";
            Double precio = 12.5;
            String tipoItem = "Entrante";
    
            // Simulamos que el restaurante existe
            Restaurante restaurante = new Restaurante();
            restaurante.setIdUsuario(idRestaurante);
            when(restauranteDAO.getById(idRestaurante)).thenReturn(restaurante);
            
            // Simular que no existe el menú
            when(cartamenuDAO.findByNombreAndRestauranteId(nombreMenu, idRestaurante)).thenReturn(null);
    
            // Simular el comportamiento de la creación del menú
            CartaMenu cartaMenu = new CartaMenu();
            cartaMenu.setId(1001l);
            when(cartamenuDAO.save(any(CartaMenu.class))).thenReturn(cartaMenu);
    
            // Llamar al método
            String result = menuController.postAltaMenu(idRestaurante, nombreMenu, nombreItem, precio, tipoItem, redirectAttributes);
    
            // Verificaciones
            assertEquals("redirect:/nuevoItem/1001", result);
            verify(cartamenuDAO).save(any(CartaMenu.class));
            verify(redirectAttributes).addFlashAttribute("success", "El menú se ha creado correctamente.");
        }
    
        @Test
        public void testPostAltaMenu_AgregarItemAUnMenuExistente() {
            String idRestaurante = "1";
            String nombreMenu = "Menu Especial";
            String nombreItem = "Ensalada";
            Double precio = 12.5;
            String tipoItem = "Entrante";

            // Simulamos que el restaurante existe
            Restaurante restaurante = new Restaurante();
            restaurante.setIdUsuario(idRestaurante);
            when(restauranteDAO.getById(idRestaurante)).thenReturn(restaurante);

            // Simular que el menú ya existe
            CartaMenu cartaMenuExistente = new CartaMenu();
            cartaMenuExistente.setId(1001L);
            when(cartamenuDAO.findByNombreAndRestauranteId(nombreMenu, idRestaurante)).thenReturn(cartaMenuExistente);

            // Simular que el item se agrega correctamente
            ItemMenu itemMenu = new ItemMenu();
            when(cartamenuDAO.save(any(CartaMenu.class))).thenReturn(cartaMenuExistente);

            // Llamar al método
            String result = menuController.postAltaMenu(idRestaurante, nombreMenu, nombreItem, precio, tipoItem, redirectAttributes);

            // Verificaciones
            assertEquals("redirect:/nuevoitem/1001", result); // Ajustado a minúsculas
            verify(cartamenuDAO).save(any(CartaMenu.class));
            verify(redirectAttributes).addFlashAttribute("success", "El item se ha creado correctamente.");
    }
        
    
        @Test
        public void testPostAltaMenu_FaltanDatos() {
            // Preparar datos de entrada con algunos valores nulos
            String idRestaurante = "1";
            String nombreMenu = "Menu Especial";
            String nombreItem = null;  // Faltan los datos
            Double precio = null;
            String tipoItem = "Entrante";
    
            // Simular que el restaurante existe
            Restaurante restaurante = new Restaurante();
            restaurante.setIdUsuario(idRestaurante);
            when(restauranteDAO.getById(idRestaurante)).thenReturn(restaurante);
    
            // Llamar al método
            String result = menuController.postAltaMenu(idRestaurante, nombreMenu, nombreItem, precio, tipoItem, redirectAttributes);
    
            // Verificaciones
            assertEquals("redirect:/errorPage", result);
            verify(redirectAttributes).addFlashAttribute("error", "Faltan datos obligatorios.");
        }
    

    @Test
    void testPostModMenuIdMenuNull() {
        
        String idRestaurante = "1";
        Long idMenu = null;
        Model model = new ConcurrentModel();

        
        String resultado = menuController.postModMenu(idRestaurante, idMenu, model, redirectAttributes);

        
        
        assertEquals("redirect:/modificarmenu/" + idRestaurante, resultado);
        verify(redirectAttributes).addFlashAttribute("error", "ID del menú no proporcionado");
    }

    @Test
    void testPostModMenuIdMenuNoNuloYCartaMenuCompleto() {
       
    String idRestaurante = "1";
    Long idMenu = 100L;
    CartaMenu cartamenuMock = new CartaMenu();
    Model model = new ConcurrentModel();
    cartamenuMock.setId(idMenu);

    
    Set<ItemMenu> items = new HashSet<>();

    
    ItemMenu item1 = new ItemMenu("Item 1", TipoItemMenu.BEBIDA, 5.0);
    ItemMenu item2 = new ItemMenu("Item 2", TipoItemMenu.COMIDA, 10.0);

    items.add(item1);
    items.add(item2);
    
    cartamenuMock.setItems(items);

    List<CartaMenu> menusMock = List.of(cartamenuMock);

    
    when(cartamenuDAO.findById(idMenu)).thenReturn(Optional.of(cartamenuMock));
    when(cartamenuDAO.findAll()).thenReturn(menusMock);

    
    String resultado = menuController.postModMenu(idRestaurante, idMenu, model, redirectAttributes);

    
    assertEquals("redirect:/modificarmenu/" + idRestaurante, resultado);

    
    verify(cartamenuDAO).findById(idMenu);
    verify(cartamenuDAO).findAll();
    verify(redirectAttributes).addFlashAttribute("items", cartamenuMock.getItems());
    verify(redirectAttributes).addFlashAttribute("menus", menusMock);
    verify(redirectAttributes).addFlashAttribute("menu_seleccionado", idMenu);
    }

        @Test
        void testPostModMenuIdMenuCompletolYCartaMenuVacio() {
           
            String idRestaurante = "1";
            Long idMenu = 101L;
            Model model = new ConcurrentModel();
            CartaMenu cartamenuMock = new CartaMenu();
            cartamenuMock.setId(idMenu);
            cartamenuMock.setItems(Collections.emptyList());

            List<CartaMenu> menusMock = List.of(cartamenuMock);

            
            when(cartamenuDAO.findById(idMenu)).thenReturn(Optional.of(cartamenuMock));
            when(cartamenuDAO.findAll()).thenReturn(menusMock);

            
            String resultado = menuController.postModMenu(idRestaurante, idMenu, model, redirectAttributes);

            
            assertEquals("redirect:/modificarmenu/" + idRestaurante, resultado);

            
            verify(cartamenuDAO).findById(idMenu);
            verify(cartamenuDAO).findAll();
            verify(redirectAttributes, never()).addFlashAttribute(eq("items"), any());
            verify(redirectAttributes).addFlashAttribute("menus", menusMock);
            verify(redirectAttributes).addFlashAttribute("menu_seleccionado", idMenu);
     }


     @Test
    void testPostNuevoItemVacio() {
        
        String tipoItem = "COMIDA";
        Model model = new ConcurrentModel();
        String result = menuController.postNuevoItem(null, null, null, tipoItem, redirectAttributes, model);

        Mockito.verify(redirectAttributes).addFlashAttribute(Mockito.eq("error"), Mockito.anyString());
        assertEquals("redirect:/error", result);
    }

    @Test
    void testPostNuevoItemCompleto() {
        Long idMenu = 1L;
        String nombreItem = "Pizza";
        Double precio = 12.50;
        String tipoItem = "COMIDA";
        Model model = new ConcurrentModel();
        
        CartaMenu mockMenu = new CartaMenu();
        Mockito.when(cartamenuDAO.findById(idMenu)).thenReturn(Optional.of(mockMenu));
        Mockito.when(cartamenuDAO.save(Mockito.any(CartaMenu.class))).thenReturn(mockMenu);

        
        String result = menuController.postNuevoItem(idMenu, nombreItem, precio, tipoItem, redirectAttributes, model);

        
        Mockito.verify(cartamenuDAO).findById(idMenu);
        Mockito.verify(cartamenuDAO).save(mockMenu);
        Mockito.verify(redirectAttributes).addFlashAttribute("success", "Item añadido con exito");
        assertEquals("redirect:/nuevoitem/" + idMenu, result);
    }
    
    }

    