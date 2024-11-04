package es.JIPF_Digital.library.persistencia;

import es.JIPF_Digital.library.dominio.entidades.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestauranteDAO extends JpaRepository<Restaurante, String> {

}
