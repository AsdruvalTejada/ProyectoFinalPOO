package logico;

public class Enfermedad {
	private String id;
	private String nombre; // nombre enfermedad
	private Boolean estaBajoVigilancia;
	
	public Enfermedad(String id, String nombre, Boolean estaBajoVigilancia) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.estaBajoVigilancia = estaBajoVigilancia;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Boolean getEstaBajoVigilancia() {
		return estaBajoVigilancia;
	}
	public void setEstaBajoVigilancia(Boolean estaBajoVigilancia) {
		this.estaBajoVigilancia = estaBajoVigilancia;
	}
	
	
}
