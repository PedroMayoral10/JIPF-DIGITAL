package es.JIPF_Digital.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.JIPF_Digital.library.dominio.entidades.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.JIPF_Digital.library.dominio.entidades.CartaMenu;

public interface CartaMenuDAO extends JpaRepository<CartaMenu,Long>{
	@Query("SELECT c FROM CartaMenu c WHERE c.nombre = :nombre AND c.restaurante.id = :idRestaurante")
    CartaMenu findByNombreAndRestauranteId(@Param("nombre") String nombre, @Param("idRestaurante") String idRestaurante);
}