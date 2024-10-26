package es.JIPF_Digital.library.persistencia;

import es.JIPF_Digital.library.dominio.entidades.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RestauranteDAO extends JpaRepository<Restaurante,String>{

	
	/*
	public void selectPorCodigoPostal(String codigoPostal) {
		
		throw new UnsupportedOperationException();
	}

	
	public void selectPorCodigoPostalYTextoLibre(String codigoPostal, String texto) {
		
		throw new UnsupportedOperationException();
	}
	*/
}