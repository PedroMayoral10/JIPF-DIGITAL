package es.JIPF_Digital.library.persistencia;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.JIPF_Digital.library.dominio.entidades.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import es.JIPF_Digital.library.dominio.entidades.CartaMenu;
import java.util.List;

public interface CartaMenuDAO extends JpaRepository<CartaMenu,Long>{
	
	
	 // Método para obtener un menú específico por nombre e ID del restaurante
    @Query("SELECT c FROM CartaMenu c WHERE c.nombre = :nombre AND c.restaurante.id = :idRestaurante")
    CartaMenu findByNombreAndRestauranteId(@Param("nombre") String nombre, @Param("idRestaurante") String idRestaurante);
	
    @Query("SELECT cm FROM CartaMenu cm JOIN FETCH cm.items WHERE cm.restaurante.id = :idRestaurante")
    List<CartaMenu> findAllByRestauranteId(@Param("idRestaurante") String idRestaurante);
    
    
}