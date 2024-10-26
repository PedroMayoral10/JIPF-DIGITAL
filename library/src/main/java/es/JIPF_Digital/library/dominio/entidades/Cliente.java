package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class Cliente {
	@Id
	private String idUsuario;
	@ManyToMany
    @JoinTable(
        name = "cliente_favoritos",
        joinColumns = @JoinColumn(name = "cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "restaurante_id")
    )
	private Collection<Restaurante> favoritos;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<Pedido> pedidos;
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<Direccion> direcciones;
	@Column
	private String nombre;
	@Column
	private String pass;
	@Column
	private String apellidos;
	@Column
	private String dni;
	
	public Cliente(String idUsuario, String nombre, String pass, String apellidos, String dni) {
		this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.pass = pass;
		this.apellidos = apellidos;
        this.dni = dni;
    }
	
	public String getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
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