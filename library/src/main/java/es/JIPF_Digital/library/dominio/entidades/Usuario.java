package es.JIPF_Digital.library.dominio.entidades;

public class Usuario {

	private String idUsuario;
	private String nombre;
	private String pass;
	private int attribute;
	
	public String getIdUsuario() {
		return idUsuario;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getPass() {
		return pass;
	}
	
	public int getAttribute() {
		return attribute;
	}
	
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public void setAtribute(int attribute) {
		this.attribute = attribute;
	}

}