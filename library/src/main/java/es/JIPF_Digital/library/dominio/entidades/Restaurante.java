package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class Restaurante {
	
	@Id
	String idUsuario;
	@OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
	Collection<Pedido> pedidos;
	@OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
	Collection<CartaMenu> cartasMenu;
	@Column
	String nombre;
	@Column
	String pass;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "direccion_id", referencedColumnName = "id_direccion")
	private Direccion direccion;
	@Column
	private String cif;

	public Restaurante(String idUsuario, String nombre, String pass, Direccion direccion, String cif) {
		this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.pass = pass; // Llamada al constructor de Usuario
        this.cif = cif;
        this.direccion = direccion;
	}
	
	/*
	public List<ItemMenu> listarMenu(String idRestaurante) {
		// TODO - implement Restaurante.listarMenu
		throw new UnsupportedOperationException();
	}*/

}