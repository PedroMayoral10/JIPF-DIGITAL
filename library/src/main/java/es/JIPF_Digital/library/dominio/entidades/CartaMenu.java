package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class CartaMenu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
	private Restaurante restaurante;
	@OneToMany(mappedBy = "cartaMenu", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<ItemMenu> items;
	@Column
	private String nombre;

}