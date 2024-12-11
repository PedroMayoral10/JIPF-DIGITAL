package es.jipfdigital.library.dominio.controladores;

import es.jipfdigital.library.dominio.entidades.*;
import es.jipfdigital.library.persistencia.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GestorClientes {

    @Autowired
    private RestauranteDAO restauranteDAO;

    @Autowired
    private ClienteDAO clienteDAO;
    
    /*
	 * GETMAPPINGS
	 */
    
    @GetMapping("/menucliente/{id}")
    public String MenuCliente(@PathVariable("id") String idCliente, Model model) {
    	model.addAttribute("id_cliente", idCliente); 
    	return "menucliente";
    }

    @GetMapping("/listarestaurantes/{id}")
    public String listaRestaurante(Model model, @PathVariable("id") String idCliente) {
        List<Restaurante> restaurantes;
        Cliente cliente = clienteDAO.findById(idCliente).orElse(null);
        restaurantes = restauranteDAO.findAll();
        model.addAttribute("restaurantes", restaurantes);
        model.addAttribute("cliente", cliente);
        return "listarestaurantes";
    }
    
    /*
	 * POSTMAPPINGS
	 */
    
    @PostMapping("/listarestaurantes/{id}")
    public String favorito(Model model, @PathVariable("id") String idCliente, 
    @RequestParam(value = "id_restaurante", required = false) String idRestaurante) {
    	
    	if(idRestaurante == null) {
    		return "redirect:/listarestaurantes/" + idCliente;
    	}	
        Restaurante restaurante = restauranteDAO.findById(idRestaurante).get();
        Cliente cliente = clienteDAO.findById(idCliente).get();
        if(cliente.getFavoritos().contains(restaurante)) {
        	cliente.getFavoritos().remove(restaurante);
        }else {
        	cliente.getFavoritos().add(restaurante);
        }
        clienteDAO.save(cliente);
        return "redirect:/listarestaurantes/" + idCliente;
    }

}
