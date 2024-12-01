package es.jipfdigital.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jipfdigital.library.dominio.entidades.Pedido;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface PedidoDAO extends JpaRepository<Pedido, Long> {
	@Query("SELECT p FROM Pedido p WHERE p.cliente.idUsuario = :idCliente")
    List<Pedido> findPedidosByCliente(@Param("idCliente") String idCliente);
}