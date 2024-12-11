package es.jipfdigital.library.dominio.entidades;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class CartaMenu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carta_menu_id", referencedColumnName = "id") // Define explícitamente la columna
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
    public void setId(long id){
        this.id=id;
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
