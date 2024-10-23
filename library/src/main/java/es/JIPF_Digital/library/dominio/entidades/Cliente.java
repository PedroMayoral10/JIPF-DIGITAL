package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;
import es.JIPF_Digital.library.dominio.entidades.Usuario;

public class Cliente {
	
	Collection<Restaurante> favoritos;
	Collection<Pedido> pedidos;
	Collection<Direccion> direcciones;
	String idUsuario, nombre, pass;
	private String apellidos;
	private String dni;
	
	public Cliente(String idUsuario, String nombre, String pass, String apellidos, String dni) {
		this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.pass = pass;
		this.apellidos = apellidos;
        this.dni = dni;
    }
	
	public String getNombre() {
		return nombre;
	}
	
	public String toString() {
	    return "Cliente{" +
	            "idUsuario='" + idUsuario + '\'' +
	            ", nombre='" + nombre + '\'' +
	            ", pass='" + pass + '\'' +
	            ", apellidos='" + apellidos + '\'' +
	            ", dni='" + dni + '\'' +
	            '}';
	}
	
	
	

}