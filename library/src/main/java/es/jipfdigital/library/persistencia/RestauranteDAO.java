package es.jipfdigital.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jipfdigital.library.dominio.entidades.Restaurante;

import java.util.List;

@Repository
public interface RestauranteDAO extends JpaRepository<Restaurante, String> {

}
