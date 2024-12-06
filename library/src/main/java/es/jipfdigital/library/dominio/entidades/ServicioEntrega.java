package es.jipfdigital.library.dominio.entidades;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class ServicioEntrega {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicioEntrega;
    @OneToOne
    @JoinColumn(name = "id_pedido", referencedColumnName = "idPedido")
    private Pedido pedido;
    @OneToOne
    @JoinColumn(name = "id_direccion", referencedColumnName = "idDireccion")
    private Direccion direccion; 
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_repartidor") 
    private Repartidor repartidor;
    @Column
	private LocalDate fechaRecepcion;
    @Column
	private LocalDate fechaEntrega;
	
	public ServicioEntrega() {
		
	}
	public Long getIdServicioEntrega() {
		return idServicioEntrega;
	}
	
	
	public Pedido getPedido() {
		return pedido;
	}
	
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	
	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}
	
	public void setFechaRecepcion(LocalDate fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	
	public void setFechaEntrega(LocalDate fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	
	public LocalDate getFechaRecepcion() {
		return this.fechaRecepcion;
	}
	
	public LocalDate getFechaEntrega() {
		return this.fechaEntrega;
	}
	

}