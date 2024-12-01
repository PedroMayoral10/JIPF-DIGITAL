package es.jipfdigital.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.jipfdigital.library.dominio.entidades.Cliente;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, String> {
    // Aquí puedes agregar métodos personalizados de consulta si los necesitas
}


