package es.JIPF_Digital.library.dominio.entidades;

import jakarta.persistence.*;

@Entity
public class ItemMenu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoItemMenu tipo;
    
    @Column(nullable = false)
    private double precio;
    
    @ManyToOne
    @JoinColumn(name = "carta_menu_id", referencedColumnName = "id")
    private CartaMenu cartaMenu;

    public ItemMenu() {}

    public ItemMenu(String nombre, TipoItemMenu tipo, double precio) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoItemMenu getTipo() {
        return tipo;
    }

    public void setTipo(TipoItemMenu tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public CartaMenu getCartaMenu() {
        return cartaMenu;
    }

    public void setCartaMenu(CartaMenu cartaMenu) {
        this.cartaMenu = cartaMenu;
    }

    @Override
    public String toString() {
        return "ItemMenu{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", precio=" + precio +
                '}';
    }
}

