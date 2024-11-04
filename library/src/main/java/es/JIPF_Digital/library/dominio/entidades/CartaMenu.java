package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class CartaMenu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carta_menu_id")
    private Collection<ItemMenu> items = new ArrayList<>();

    @Column
    private String nombre;

    public CartaMenu() {}

    public CartaMenu(Restaurante restaurante, String nombre) {
        this.restaurante = restaurante;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public Collection<ItemMenu> getItems() {
        return items;
    }

    public void setItems(Collection<ItemMenu> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String toString() {
        return "CartaMenu{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", items=" + items +
                '}';
    }
}
