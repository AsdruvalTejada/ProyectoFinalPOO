package logico;

import java.time.LocalDateTime;
import java.io.Serializable;
//
public class Cita implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String idPaciente;
	private String nameVisitante;
	private Medico medico;
	private LocalDateTime fechaCitada;
	private String estado;
	
	public Cita(String id, String idPaciente, String nameVisitante, Medico medico, LocalDateTime fechaCitada, String estado) {
		super();
		this.id = id;
		this.idPaciente = idPaciente;
		this.nameVisitante = nameVisitante;
		this.medico = medico;
		this.fechaCitada = fechaCitada;
		this.estado = estado;
	}

	public String getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	public String getNameVisitante() {
		return nameVisitante;
	}

	public void setNameVisitante(String nameVisitante) {
		this.nameVisitante = nameVisitante;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public LocalDateTime getFechaCitada() {
		return fechaCitada;
	}

	public void setFechaCitada(LocalDateTime fechaCitada) {
		this.fechaCitada = fechaCitada;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getId() {
		return id;
	}
	public boolean citaPuedeCancelarse() {
		boolean valido = false;
		if(this.getFechaCitada().isAfter(LocalDateTime.now())) {
			valido = true;
		}
		return valido;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Cita other = (Cita) obj;
		
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
