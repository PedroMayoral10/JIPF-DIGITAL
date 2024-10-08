package es.JIPF_Digital.library.dominio.controladores;

import es.JIPF_Digital.library.persistencia.*;
import es.JIPF_Digital.library.dominio.entidades.*;
import java.util.List;

public class GestorPedidos {

	PedidoDAO pedidoDAO;
	ServicioEntregaDAO servicioEntregaDAO;
	Pedido pedidoEnMarcha;

	/**
	 * 
	 * @param c
	 * @param r
	 * @param items
	 */
	public void realizarPedido(Cliente c, Restaurante r, List<ItemMenu> items) {
		// TODO - implement GestorPedidos.realizarPedido
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param p
	 */
	private boolean realizarPago(Pedido p) {
		// TODO - implement GestorPedidos.realizarPago
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param p
	 * @param d
	 */
	private ServicioEntrega crearServicioEntrega(Pedido p, Direccion d) {
		// TODO - implement GestorPedidos.crearServicioEntrega
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param item
	 */
	public void addItemMenu(ItemMenu item) {
		// TODO - implement GestorPedidos.addItemMenu
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param item
	 */
	public void eliminarItemMenu(ItemMenu item) {
		// TODO - implement GestorPedidos.eliminarItemMenu
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param resaturante
	 */
	public void comenzarPedido(Restaurante resaturante) {
		// TODO - implement GestorPedidos.comenzarPedido
		throw new UnsupportedOperationException();
	}

}