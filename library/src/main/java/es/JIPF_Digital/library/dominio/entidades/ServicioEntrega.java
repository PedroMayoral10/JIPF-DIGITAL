package es.JIPF_Digital.library.dominio.entidades;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class ServicioEntrega {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private Pedido pedido;
	private Direccion direccion;
	private Repartidor repartidor;
	private LocalDateTime fechaRecepcion;
	private LocalDateTime fechaEntrega;

}