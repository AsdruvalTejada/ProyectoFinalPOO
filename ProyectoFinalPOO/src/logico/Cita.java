package logico;

import java.time.LocalDate;
import java.time.LocalDateTime;
//
public class Cita {
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
	
}
