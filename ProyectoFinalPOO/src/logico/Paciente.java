package logico;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;

public class Paciente extends Persona {
	private static final long serialVersionUID = 1L;
    private String tipoSangre;
    private float estatura;
    private ArrayList<Consulta> historialConsultas;
    private HashMap<Vacuna, Boolean> registroVacunacion;

    public Paciente(String id, String name, String apellido, LocalDate fechaNacimiento, String sexo, String contacto, 
                    String tipoSangre, float estatura) {
        super(id, name, apellido, fechaNacimiento, sexo, contacto);
        this.tipoSangre = tipoSangre;
        this.estatura = estatura;
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
	public String getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public float getEstatura() {
		return estatura;
	}

	public void setEstatura(float estatura) {
		this.estatura = estatura;
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

    public int getEdad() {
        if (getFechaNacimiento() != null) {
            return Period.between(getFechaNacimiento(), LocalDate.now()).getYears();
        }
        return 0;
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
        for(Vacuna v : registroVacunacion.keySet()) {
            if(registroVacunacion.get(v)) {
                aplicadas.add(v);
            }
        }
        return aplicadas;
    }	

	private boolean registroContieneVacuna(Vacuna vacuna) {
		return this.registroVacunacion.containsKey(vacuna);
	}
	
	 public void inicializarRegistroVacunas(ArrayList<Vacuna> catalogo) {
	        for(Vacuna v : catalogo) {
	            if(!registroVacunacion.containsKey(v)) {
	                registroVacunacion.put(v, false);
	            }
	        }
	    }

	   public void agregarConsulta(Consulta c) {
	        historialConsultas.add(c);
	    }
	    
	    public void marcarVacunaAplicada(Vacuna v) {
	        registroVacunacion.put(v, true);
	    }
	    
}