package es.jipfdigital.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jipfdigital.library.dominio.entidades.*;
public interface ServicioEntregaDAO extends JpaRepository<ServicioEntrega,Long>{
	
}