package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class ItemMenu {
	@Id
	private String nombre;
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
	private TipoItemMenu tipo;
	@Column
	private double precio;

}