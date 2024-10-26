package es.JIPF_Digital.library.dominio.entidades;

import jakarta.persistence.*;

@Entity
public class Direccion {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column
	private String codigoPostal;
	@Column
	private String calle;
	@Column
	private String numero;
	@Column
	private String complemento;
	@Column
	private String municipio;
	
	
	public Direccion(String codigoPostal, String calle, String numero, String complemento, String municipio) {
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.numero = numero;
        this.complemento = complemento;
        this.municipio = municipio;
    }
}