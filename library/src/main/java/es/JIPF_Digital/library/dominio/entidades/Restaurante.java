package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;

public class Restaurante {

	Collection<Pedido> pedidos;
	Collection<CartaMenu> cartasMenu;
	String idUsuario, nombre, pass;
	Direccion direccion;
	private String cif;

	public Restaurante(String idUsuario, String nombre, String pass, Direccion direccion, String cif) {
		this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.pass = pass; // Llamada al constructor de Usuario
        this.cif = cif;
        this.direccion = direccion;
	}
	
	
	public List<ItemMenu> listarMenu(String idRestaurante) {
		// TODO - implement Restaurante.listarMenu
		throw new UnsupportedOperationException();
	}

}