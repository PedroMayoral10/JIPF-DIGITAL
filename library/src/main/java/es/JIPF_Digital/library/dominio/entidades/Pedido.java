package es.JIPF_Digital.library.dominio.entidades;

import java.util.Collection;
import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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
	private LocalDate fecha;

	public Pedido() {
		
	}

	public Long getId_pedido() {
		return id_pedido;
	}

	public void setId_pedido(Long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	public Collection<ItemMenu> getItems() {
		return items;
	}

	public void setItems(Collection<ItemMenu> items) {
		this.items = items;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public ServicioEntrega getEntrega() {
		return entrega;
	}

	public void setEntrega(ServicioEntrega entrega) {
		this.entrega = entrega;
	}

	public EstadoPedido getEstado() {
		return estado;
	}

	public void setEstado(EstadoPedido estado) {
		this.estado = estado;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
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