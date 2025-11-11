package logico;

import java.time.LocalDateTime;

public class Consulta {
	private String id;
	private Cita citaAsociada;
	private Paciente paciente;
	private Medico medico;
	private LocalDateTime fechaConsulta;
	private String sintomas;
	private Enfermedad diagnostico;
	private String tratamiento;
	private boolean esImportante;
	
	public Consulta(String id, Cita citaAsociada, Paciente paciente, Medico medico, LocalDateTime fechaConsulta, String sintomas,
			Enfermedad diagnostico, String tratamiento, boolean esImportante) {
		super();
		this.id = id;
		this.citaAsociada = citaAsociada;
		this.paciente = paciente;
		this.medico = medico;
		this.fechaConsulta = fechaConsulta;
		this.sintomas = sintomas;
		this.diagnostico = diagnostico;
		this.tratamiento = tratamiento;
		this.esImportante = esImportante;
	}

	public Cita getCitaAsociada() {
		return citaAsociada;
	}

	public void setCitaAsociada(Cita citaAsociada) {
		this.citaAsociada = citaAsociada;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public LocalDateTime getFechaConsulta() {
		return fechaConsulta;
	}

	public void setFechaConsulta(LocalDateTime fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

	public String getSintomas() {
		return sintomas;
	}

	public void setSintomas(String sintomas) {
		this.sintomas = sintomas;
	}

	public Enfermedad getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(Enfermedad diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getTratamiento() {
		return tratamiento;
	}

	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}

	public boolean isEsImportante() {
		return esImportante;
	}

	public void setEsImportante(boolean esImportante) {
		this.esImportante = esImportante;
	}

	public String getId() {
		return id;
	}
	
}
