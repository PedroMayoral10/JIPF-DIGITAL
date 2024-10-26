package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class Pedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_pedido;
	@ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
	private Cliente cliente;
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pago_id", referencedColumnName = "idTransaccion")
	private Pago pago;
	@ManyToMany
	@JoinTable(
			name = "pedido_items",
			joinColumns = @JoinColumn(name = "pedido_id"),
			inverseJoinColumns = @JoinColumn(name = "item_menu_id")
	)
	private Collection<ItemMenu> items;
	@ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
	private Restaurante restaurante;
	@OneToOne(mappedBy="pedido",cascade = CascadeType.ALL)
	private ServicioEntrega entrega;
	@Enumerated(EnumType.STRING)
    private EstadoPedido estado;
	@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	public Pedido() {
		
	}
	
	
	/*
	public void add(ItemMenu itemMenu) {
		// TODO - implement Pedido.add
		throw new UnsupportedOperationException();
	}

	
	public void delete(ItemMenu itemMenu) {
		// TODO - implement Pedido.delete
		throw new UnsupportedOperationException();
	}*/

}