package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;

public class Repartidor extends Usuario {

	Collection<ServicioEntrega> servicios;
	Collection<String> zonas;
	private String apellidos;
	private String nif;
	private int eficiencia;
	String idUsuario, nombre, pass;
	
	public Repartidor(String idUsuario, String nombre, String pass, String apellidos, String nif) {
		this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.pass = pass;
        this.apellidos = apellidos;
        this.nif = nif;
        eficiencia = 0;
    }
}