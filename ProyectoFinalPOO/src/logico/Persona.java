package logico;
import java.time.LocalDate;

public abstract class Persona {
	protected String id; 
	protected String name; 
	protected String apellido; 
	protected LocalDate fechaNacimiento; 
	protected String sexo; 
	protected String contacto;
	
	public Persona(String id, String name, String apellido, LocalDate fechaNacimiento, String sexo, String contacto) {
		super();
		this.id = id;
		this.name = name;
		this.apellido = apellido;
		this.fechaNacimiento = fechaNacimiento;
		this.sexo = sexo;
		this.contacto = contacto;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getId() {
		return id;
	} 
}	
