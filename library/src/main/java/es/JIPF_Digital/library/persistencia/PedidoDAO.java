package es.JIPF_Digital.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.JIPF_Digital.library.dominio.entidades.Pedido;

@Repository
public interface PedidoDAO extends JpaRepository<Pedido, Long> {
   
}