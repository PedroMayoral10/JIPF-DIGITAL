package es.jipfdigital.library.dominio.controladores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import es.jipfdigital.library.dominio.entidades.CartaMenu;
import es.jipfdigital.library.dominio.entidades.ItemMenu;
import es.jipfdigital.library.dominio.entidades.Restaurante;
import es.jipfdigital.library.dominio.entidades.TipoItemMenu;
import es.jipfdigital.library.persistencia.CartaMenuDAO;
import es.jipfdigital.library.persistencia.ItemMenuDAO;
import es.jipfdigital.library.persistencia.RestauranteDAO;

public class GestorRestauranteTest {
    @InjectMocks
    private GestorRestaurantes menuController;

    @Mock
    private RestauranteDAO restauranteDAO;

    @Mock
    private CartaMenuDAO cartamenuDAO;

    @Mock
    private ItemMenuDAO itemMenuDAO;

    @Mock
    private RedirectAttributes redirectAttributes;

    private CartaMenu mockMenu;
    private Restaurante mockRestaurante;

    @Mock
    private Model model;

    @Mock
    private ItemMenuDAO itemDAO;
    private Restaurante restaurante;

    public GestorRestauranteTest() {
        MockitoAnnotations.openMocks(this);
        restaurante = new Restaurante();
        restaurante.setIdUsuario("1");
        restaurante.setNombre("Restaurante Test");

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
                redirectAttributes);

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

        String result = menuController.postAltaMenu(idRestaurante, nombreMenu, nombreItem, precio, tipoItem,
                redirectAttributes);

        verify(restauranteDAO).getById(idRestaurante);
        verify(cartamenuDAO, times(1)).save(any(CartaMenu.class)); // Solo se debe llamar una vez
        verify(redirectAttributes).addFlashAttribute(eq("success"), anyString());

        assertTrue(result.contains("/nuevoitem/"), "La URL de redirección no es la esperada.");
    }

    // En tu clase de prueba
    @Test
    public void testPostAltaMenuCrearNuevoMenu() {
        // Datos de prueba
        String idRestaurante = "restaurante1";
        String nombreMenu = "Nuevo Menú";
        String nombreItem = "Nuevo Item";
        Double precio = 15.0;
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        
        // Mock de las dependencias
        Restaurante restaurante = new Restaurante();
        restaurante.setIdUsuario(idRestaurante); // Establecer el restaurante mockeado
        
        // Simulamos que no hay un menú con ese nombre en la base de datos
        when(cartamenuDAO.findByNombreAndRestauranteId(nombreMenu, idRestaurante)).thenReturn(null);
        when(restauranteDAO.getById(idRestaurante)).thenReturn(restaurante);
        
        // Creamos un objeto CartaMenu con un ID simulado
        CartaMenu cartamenu = new CartaMenu();
        cartamenu.setId(1L);  // Asigna un ID simulado
        
        // Simulamos el comportamiento de cartamenuDAO.save para devolver un objeto con ID asignado
        when(cartamenuDAO.save(any(CartaMenu.class))).thenAnswer(invocation -> {
            CartaMenu savedCartamenu = invocation.getArgument(0);
            savedCartamenu.setId(1L);  // Asignar el ID al objeto guardado
            return savedCartamenu;  // Devolver el objeto con el ID asignado
        });
        
        // Ejecutamos el método
        String result = menuController.postAltaMenu(idRestaurante, nombreMenu, nombreItem, precio, "tipo", redirectAttributes);
        
        // Verifica que el ID del menú no sea null
        assertNotNull(cartamenu.getId(), "El ID del menú no debe ser null");  // Corregido aquí
        
        // Verifica la redirección a la página del nuevo ítem con el ID del nuevo menú
        assertEquals("redirect:/nuevoitem/1", result);  // Asegúrate de que el ID del menú es correcto
        
        // Verifica que el mensaje de éxito fue agregado
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("success"));
        assertEquals("El menú se ha creado correctamente.", redirectAttributes.getFlashAttributes().get("success"));
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
        String result = menuController.postAltaMenu(idRestaurante, nombreMenu, nombreItem, precio, tipoItem,
                redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/nuevoItem/1001", result);
        verify(cartamenuDAO).save(any(CartaMenu.class));
        verify(redirectAttributes).addFlashAttribute("success", "El menú se ha creado correctamente.");
    }

    @Test
    public void testPostAltaMenuAgregarItemAUnMenuExistente() {
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
        String result = menuController.postAltaMenu(idRestaurante, nombreMenu, nombreItem, precio, tipoItem,
                redirectAttributes);

        // Verificaciones
        assertEquals("redirect:/nuevoitem/1001", result); // Ajustado a minúsculas
        verify(cartamenuDAO).save(any(CartaMenu.class));
        verify(redirectAttributes).addFlashAttribute("success", "El item se ha creado correctamente.");
    }

    @Test
    public void testPostAltaMenuFaltanDatos() {
        // Preparar datos de entrada con algunos valores nulos
        String idRestaurante = "1";
        String nombreMenu = "Menu Especial";
        String nombreItem = null; // Faltan los datos
        Double precio = null;
        String tipoItem = "Entrante";

        // Simular que el restaurante existe
        Restaurante restaurante = new Restaurante();
        restaurante.setIdUsuario(idRestaurante);
        when(restauranteDAO.getById(idRestaurante)).thenReturn(restaurante);

        // Llamar al método
        String result = menuController.postAltaMenu(idRestaurante, nombreMenu, nombreItem, precio, tipoItem,
                redirectAttributes);

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
    public void testModificarMenuIdRestauranteVacio() {
        String idRestaurante = ""; // O también puede ser null
        Model model = new BindingAwareModelMap();

        try {
            menuController.modificarMenu(idRestaurante, model);
            fail("Se esperaba una IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Verifica que el mensaje sea el esperado
            assertEquals("El ID del restaurante no puede estar vacio", e.getMessage());
        }
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

    @Test
    public void testDeleteItemExiste() throws Exception {

        Long itemId = 1L;
        ItemMenu mockItem = new ItemMenu(); // Asume que tienes un objeto ItemMenu
        when(itemDAO.findById(itemId)).thenReturn(Optional.of(mockItem));

        ResponseEntity<Void> response = menuController.deleteItem(itemId);
        assertEquals(200, response.getStatusCodeValue());
        verify(itemDAO, times(1)).delete(mockItem);
    }

    // Intentar eliminar un elemento inexistente
    @Test
    public void testDeleteItemNoExiste() throws Exception {
        // Arrange
        Long itemId = 1L;
        when(itemDAO.findById(itemId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = menuController.deleteItem(itemId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(itemDAO, never()).delete(any());
    }

    @Test
    void testDeleteMenuConItems() {

        Long menuId = 1L;

        CartaMenu mockMenu = new CartaMenu();
        mockMenu.setId(menuId);
        ItemMenu item1 = new ItemMenu();
        ItemMenu item2 = new ItemMenu();
        mockMenu.setItems(Arrays.asList(item1, item2));

        when(cartamenuDAO.findById(menuId)).thenReturn(Optional.of(mockMenu));

        ResponseEntity<Void> response = menuController.deleteMenu(menuId);

        assertEquals(200, response.getStatusCodeValue());
        verify(itemDAO, times(1)).delete(item1);
        verify(itemDAO, times(1)).delete(item2);
        verify(cartamenuDAO, times(1)).delete(mockMenu);
    }

    @Test
    void testDeleteMenuSinItems() {

        Long menuId = 2L;

        CartaMenu mockMenu = new CartaMenu();
        mockMenu.setId(menuId);
        mockMenu.setItems(Collections.emptyList());

        when(cartamenuDAO.findById(menuId)).thenReturn(Optional.of(mockMenu));

        ResponseEntity<Void> response = menuController.deleteMenu(menuId);

        assertEquals(200, response.getStatusCodeValue());
        verify(itemDAO, never()).delete(any());
        verify(cartamenuDAO, times(1)).delete(mockMenu);
    }

    @Test
    void testDeleteMenuNoEncontrado() {
        // El menuId no existe
        Long menuId = 3L;

        when(cartamenuDAO.findById(menuId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = menuController.deleteMenu(menuId);

        assertEquals(404, response.getStatusCodeValue());
        verify(itemDAO, never()).delete(any());
        verify(cartamenuDAO, never()).delete(any());
    }

    // TEST de los Getmapping
    // Restaurante existe
    @Test
    public void testMenuRestauranteExiste() {

        String idRestaurante = "123";
        Restaurante mockRestaurante = new Restaurante();
        mockRestaurante.setIdUsuario(idRestaurante);

        when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.of(mockRestaurante));
        String viewName = menuController.MenuRestaurante(idRestaurante, model);

        assertEquals("menurestaurante", viewName);
        verify(model).addAttribute(mockRestaurante);
    }

    // Restaurante no existe
    @Test
    public void testMenuRestauranteNoExiste() {

        String idRestaurante = "123";
        Model model = new ConcurrentModel();
        when(restauranteDAO.findById(idRestaurante)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            menuController.MenuRestaurante(idRestaurante, model);
        });
    }

    @Test
    public void testMenuRestauranteIdNovalido() {
        String idRestaurante = null;
        Model model = new ConcurrentModel();

        assertThrows(IllegalArgumentException.class, () -> {
            menuController.MenuRestaurante(idRestaurante, model);
        });
    }

    @Test
    public void testDarAltaMenuExito() {
        // Dado que cuando se llama a findById con un id, retorna un Optional con un
        // restaurante
        when(restauranteDAO.findById("1")).thenReturn(Optional.of(restaurante));

        String viewName = menuController.darAltaMenu("1", model);

        assertEquals("altamenu", viewName);
        verify(model).addAttribute(restaurante);
    }

    @Test
    public void testDarAltaMenuRestauranteNoEncontrado() {

        when(restauranteDAO.findById("1")).thenReturn(Optional.empty());

        String viewName = menuController.darAltaMenu("1", model);

        assertEquals("error", viewName);
        verify(model, never()).addAttribute(any());
    }

    @Test
    public void testModificarMenuConMenus() {
        String idRestaurante = "restaurante123";

        Restaurante restaurante = new Restaurante();
        restaurante.setIdUsuario(idRestaurante);

        List<CartaMenu> menus = List.of(
                new CartaMenu(restaurante, "menu1"),
                new CartaMenu(restaurante, "menu2"));

        when(cartamenuDAO.findAllByRestauranteId(idRestaurante)).thenReturn(menus);

        String viewName = menuController.modificarMenu(idRestaurante, model);

        assertEquals("modificarmenu", viewName);
        verify(model).addAttribute("menus", menus);
        verify(model).addAttribute("idRestaurante", idRestaurante);
    }

    @Test
    public void testModificarMenuSinMenus() {
        // El restaurante no tiene menús asociados
        String idRestaurante = "restaurante123";

        when(cartamenuDAO.findAllByRestauranteId(idRestaurante)).thenReturn(Collections.emptyList());

        String viewName = menuController.modificarMenu(idRestaurante, model);

        assertEquals("modificarmenu", viewName);
        verify(model).addAttribute("menus", Collections.emptyList());
        verify(model).addAttribute("idRestaurante", idRestaurante);
    }

    @Test
    public void testModificarMenuIdRestauranteNulo() {
        // El idRestaurante es nulo
        String idRestaurante = null;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> menuController.modificarMenu(idRestaurante, model));

        assertNotNull(exception);
        verifyNoInteractions(cartamenuDAO, model);
    }

    @Test
    public void testModificarMenuDaoLanzaExcepcion() {

        String idRestaurante = "restaurante123";

        when(cartamenuDAO.findAllByRestauranteId(idRestaurante)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class,
                () -> menuController.modificarMenu(idRestaurante, model));

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testNuevoItemCasoExitoso() {

        mockRestaurante = new Restaurante();
        mockRestaurante.setIdUsuario("100");
        mockMenu = new CartaMenu();
        mockMenu.setRestaurante(mockRestaurante);

        when(cartamenuDAO.findById(1L)).thenReturn(Optional.of(mockMenu));

        String result = menuController.nuevoItem(1L, model);

        verify(model).addAttribute("menu", mockMenu);
        verify(model).addAttribute("idRestaurante", "100");

        assertEquals("nuevoitem", result);
    }

    @Test
    void testNuevoItemIdMenuNoExiste() {

        when(cartamenuDAO.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> menuController.nuevoItem(2L, model));
    }

}
