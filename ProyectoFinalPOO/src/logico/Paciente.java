package logico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Paciente extends Persona {
	
	private ArrayList<Consulta> historialConsultas;
	private HashMap<Vacuna, Boolean> registroVacunacion;
	
	public Paciente(String id, String name, String apellido, LocalDate fechaNacimiento, String sexo, String contacto) {
		super(id, name, apellido, fechaNacimiento, sexo, contacto);
		
		this.historialConsultas = new ArrayList<>();
		this.registroVacunacion = new HashMap<>();
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

	public void inicializarRegistroVacunas(ArrayList<Vacuna> catalogoGeneral) {
		this.registroVacunacion.clear();
		for (Vacuna vacuna : catalogoGeneral) {
			this.registroVacunacion.put(vacuna, false);
		}
	}

	public void agregarConsulta(Consulta nuevaConsulta) {
		if (nuevaConsulta != null) {
			this.historialConsultas.add(nuevaConsulta);
		}
	}

	public boolean marcarVacunaAplicada(Vacuna vacuna) {
		if (this.registroContieneVacuna(vacuna)) {
			this.registroVacunacion.put(vacuna, true);
			return true;
		}
		return false; 
	}

	public ArrayList<Consulta> generarResumenHistorial() {
		ArrayList<Consulta> resumen = new ArrayList<>();
		
		for (Consulta consulta : this.historialConsultas) {
			
			boolean esImportante = consulta.isEsImportanteParaResumen(); 
			
			boolean esVigilancia = false;
			if (consulta.getDiagnostico() != null) {
				esVigilancia = consulta.getDiagnostico().getEstaBajoVigilancia(); 
			}

			if (esImportante || esVigilancia) {
				resumen.add(consulta);
			}
		}
		
		return resumen;
	}


	public ArrayList<Vacuna> getVacunasPendientes() {
		ArrayList<Vacuna> pendientes = new ArrayList<>();

		for (Vacuna vacuna : this.registroVacunacion.keySet()) {
			Boolean estaAplicada = this.registroVacunacion.get(vacuna);
			
			if (estaAplicada == false) {
				pendientes.add(vacuna);
			}
		}
		return pendientes;
	}


	public ArrayList<Vacuna> getVacunasAplicadas() {
		ArrayList<Vacuna> aplicadas = new ArrayList<>();
		for (Vacuna vacuna : this.registroVacunacion.keySet()) {
			Boolean estaAplicada = this.registroVacunacion.get(vacuna);

			if (estaAplicada == true) {
				aplicadas.add(vacuna);
			}
		}
		return aplicadas;
	}
	

	private boolean registroContieneVacuna(Vacuna vacuna) {
		return this.registroVacunacion.containsKey(vacuna);
	}

}