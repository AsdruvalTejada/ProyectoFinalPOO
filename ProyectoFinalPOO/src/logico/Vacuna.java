package logico;
import java.io.Serializable;

public class Vacuna implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String nombre;
	
	public Vacuna(String id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Vacuna other = (Vacuna) obj;
		
		if (id == null) {
			return other.id == null;
		} else {
			return id.equals(other.id);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
}
