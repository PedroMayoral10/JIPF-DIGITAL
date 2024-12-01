package es.jipfdigital.library.dominio.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Direccion {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDireccion;
	@Column(nullable = false)
	private String codigoPostal;
	@Column(nullable = false)
	private String calle;
	@Column(nullable = false)
	private String numero;
	@Column(nullable = false)
	private String complemento;
	@Column(nullable = false)
	private String municipio;
	
	public Direccion() {
    }
	
	
	public Direccion(String codigoPostal, String calle, String numero, String complemento, String municipio) {
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.numero = numero;
        this.complemento = complemento;
        this.municipio = municipio;
    }
}