package logico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Paciente extends Persona{
	private ArrayList<Consulta> historialConsultas;
	private HashMap<Vacuna, Boolean> registroVacunacion;
	
	public Paciente(String id, String name, String apellido, LocalDate fechaNacimiento, String sexo, String contacto) {
		super(id, name, apellido, fechaNacimiento, sexo, contacto);
	}

	public ArrayList<Consulta> getHistorialConsultas() {
		return historialConsultas;
	}

	public void setHistorialConsultas(ArrayList<Consulta> historialConsultas) {
		this.historialConsultas = historialConsultas;
	}

	public HashMap<Vacuna, Boolean> getRegistroVacunacion() {
		return registroVacunacion;
	}

	public void setRegistroVacunacion(HashMap<Vacuna, Boolean> registroVacunacion) {
		this.registroVacunacion = registroVacunacion;
	}
	
}
