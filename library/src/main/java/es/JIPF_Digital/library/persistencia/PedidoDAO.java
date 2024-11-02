package es.JIPF_Digital.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import es.JIPF_Digital.library.dominio.entidades.Pedido;

@Repository
public interface PedidoDAO extends JpaRepository<Pedido, Long> {
	@Query("SELECT p FROM Pedido p WHERE p.cliente.idUsuario = :idCliente")
    List<Pedido> findPedidosByCliente(@Param("idCliente") String idCliente);
}