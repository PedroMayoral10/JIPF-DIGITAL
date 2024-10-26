package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class Repartidor extends Usuario {
	
	@Id
	String idUsuario;
	@Column
	Collection<ServicioEntrega> servicios;
	@Column
	Collection<String> zonas;
	@Column
	private String apellidos;
	@Column
	private String nif;
	@Column
	private int eficiencia;
	@Column
	String nombre;
	@Column
	String pass;
	
	public Repartidor(String idUsuario, String nombre, String pass, String apellidos, String nif) {
		this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.pass = pass;
        this.apellidos = apellidos;
        this.nif = nif;
        eficiencia = 0;
    }
	
	public String getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
}