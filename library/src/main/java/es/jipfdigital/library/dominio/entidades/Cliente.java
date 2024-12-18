package es.jipfdigital.library.dominio.entidades;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class Cliente {
    @Id
    private String idUsuario;

    @ManyToMany
    @JoinTable(
        name = "cliente_favoritos",
        joinColumns = @JoinColumn(name = "cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "restaurante_id")
    )
    private Collection<Restaurante> favoritos = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cliente_id") // Agrega una columna cliente_id en la tabla Direccion
    private Collection<Direccion> direcciones = new HashSet<>();

    @Column
    private String nombre;

    @Column
    private String pass;

    @Column
    private String apellidos;

    @Column
    private String dni;

    public Cliente() {
        // Constructor vacío
    }

    public Cliente(String idUsuario, String nombre, String pass, String apellidos, String dni) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.pass = pass;
        this.apellidos = apellidos;
        this.dni = dni;
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

    public String getPass() {
        return pass;
    }
    public Collection<Direccion> getDirecciones(){
        return direcciones;
    }
    public void setDirecciones(Collection<Direccion> direcciones){
        this.direcciones = direcciones;
    }

    public Collection<Restaurante> getFavoritos() {
        return favoritos;
    }

    // Método para agregar un restaurante a favoritos
    public void addFavorito(Restaurante restaurante) {
        if (restaurante != null) {
            favoritos.add(restaurante);
        }
    }

    public void addDireccion(Direccion direccion){
        if(direccion != null){
            direcciones.add(direccion);
        }
    }

    public String toString() {
        return "Cliente{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", pass='" + pass + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", dni='" + dni + '\'' +
                '}';
    }
}
