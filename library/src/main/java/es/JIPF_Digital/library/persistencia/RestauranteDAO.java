package es.JIPF_Digital.library.persistencia;

import es.JIPF_Digital.library.dominio.entidades.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestauranteDAO extends JpaRepository<Restaurante, String> {

    /**
     * Busca restaurantes por código postal.
     * @param codigoPostal Código postal del restaurante.
     * @return Lista de restaurantes que coinciden con el código postal.
     */
    List<Restaurante> findByDireccion_CodigoPostal(String codigoPostal);

    /**
     * Busca restaurantes por código postal y texto libre en el nombre.
     * @param codigoPostal Código postal del restaurante.
     * @param texto Texto a buscar en el nombre del restaurante.
     * @return Lista de restaurantes que coinciden con el código postal y contienen el texto en el nombre.
     */
    List<Restaurante> findByDireccion_CodigoPostalAndNombreContaining(String codigoPostal, String texto);

    // El método findById se elimina, ya que JpaRepository ya lo proporciona
    // Si necesitas el comportamiento específico, puedes personalizarlo en el controlador o en el servicio.

    // El método save se elimina, ya que JpaRepository ya lo proporciona

    // El método deleteById se elimina, ya que JpaRepository ya lo proporciona
}
