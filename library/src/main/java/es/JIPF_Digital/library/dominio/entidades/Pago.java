package es.JIPF_Digital.library.dominio.entidades;

import java.util.UUID;
import java.time.*;

public class Pago {

	Pedido pedido;
	MetodoPago tipo;
	private UUID idTransaccion;
	private LocalDate fechaTransaccion;

}