package es.jipfdigital.library.dominio.entidades;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class Repartidor {
	
	@Id
	String idUsuario;
	@OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL, orphanRemoval = true)
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
	
	public Repartidor() {
		
	}
	
	
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
	
	public Collection<ServicioEntrega> getServicios(){
		return servicios;
	}
	
	
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getPass() {
		return pass;
	}
	
	public int getEficiencia() {
		return eficiencia;
	}
	
	public void actualizarEficiencia(int num) {
		eficiencia += num;
	}

}