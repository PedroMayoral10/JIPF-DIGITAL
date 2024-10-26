package es.JIPF_Digital.library.dominio.entidades;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import java.util.*;

@Entity
public class ServicioEntrega {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_servicioEntrega;
    @OneToOne
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    private Pedido pedido;
    @JoinColumn(name = "id_direccion",  referencedColumnName = "id_direccion") 
    private Direccion direccion;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idUsuario") 
    private Repartidor repartidor;
	private LocalDateTime fechaRecepcion;
	private LocalDateTime fechaEntrega;

}