package es.jipfdigital.library.dominio.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Pago {

    @Id
    private UUID idTransaccion;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago tipo;

    @Column(nullable = false)
    private LocalDate fechaTransaccion;

    public Pago() {}

    public Pago(Pedido pedido, MetodoPago tipo, LocalDate fechaTransaccion) {
        this.pedido = pedido;
        this.tipo = tipo;
        this.fechaTransaccion = fechaTransaccion;
        this.idTransaccion = UUID.randomUUID();
    }

    public UUID getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(UUID idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public MetodoPago getTipo() {
        return tipo;
    }

    public void setTipo(MetodoPago tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDate fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }
}