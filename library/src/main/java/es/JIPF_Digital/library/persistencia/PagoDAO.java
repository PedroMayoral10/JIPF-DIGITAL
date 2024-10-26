package es.JIPF_Digital.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.JIPF_Digital.library.dominio.entidades.*;
import java.util.UUID;

public interface PagoDAO extends JpaRepository<Pago,UUID> {
}
