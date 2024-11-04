package es.JIPF_Digital.library.dominio.entidades;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class Restaurante {

    @Id
    private String idUsuario;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Pedido> pedidos;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<CartaMenu> cartasMenu = new ArrayList<>();

    @Column
    private String nombre;

    @Column
    private String pass;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id", referencedColumnName = "id_direccion")
    private Direccion direccion;

    @Column
    private String cif;

    public Restaurante() {
    }

    public Restaurante(String idUsuario, String nombre, String pass, Direccion direccion, String cif) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.pass = pass;
        this.cif = cif;
        this.direccion = direccion;
    }

    public Collection<CartaMenu> getCartasMenu() {
        return cartasMenu;
    }

    public void setCartasMenu(Collection<CartaMenu> cartasMenu) {
        this.cartasMenu.clear();
        if (cartasMenu != null) {
            this.cartasMenu.addAll(cartasMenu);
        }
    }

    public CartaMenu getMenuPrincipal() {
        return cartasMenu.isEmpty() ? null : cartasMenu.iterator().next();
    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cif='" + cif + '\'' +
                ", direccion=" + direccion +
                '}';
    }
}
