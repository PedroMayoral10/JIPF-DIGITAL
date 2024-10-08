package es.JIPF_Digital.library.dominio.entidades;

import java.time.LocalDateTime;

public class ServicioEntrega {

	Pedido pedido;
	Direccion direccion;
	Repartidor repartidor;
	private LocalDateTime fechaRecepcion;
	private LocalDateTime fechaEntrega;

}