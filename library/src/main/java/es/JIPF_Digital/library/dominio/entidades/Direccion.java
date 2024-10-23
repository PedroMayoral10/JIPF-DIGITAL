package es.JIPF_Digital.library.dominio.entidades;

public class Direccion {

	private String codigoPostal;
	private String calle;
	private String numero;
	private String complemento;
	private String municipio;
	
	
	public Direccion(String codigoPostal, String calle, String numero, String complemento, String municipio) {
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.numero = numero;
        this.complemento = complemento;
        this.municipio = municipio;
    }
}