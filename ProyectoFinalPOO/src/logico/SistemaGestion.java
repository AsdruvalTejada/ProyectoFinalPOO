package logico;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Comparator; 

public class SistemaGestion {
	private ArrayList<Paciente> listaPacientes;
	private ArrayList<Medico> listaMedicos;
	private ArrayList<Cita> listaCitas;
	private ArrayList<Enfermedad> catalogoEnfermedades;
	private ArrayList<Vacuna> catalogoVacunas;
	private ArrayList<Usuario> listaUsuarios;
	private ArrayList<Secretaria> listaSecretarias;
	private static SistemaGestion clinica = null;
	public static int genIdCita = 0;
	public static int genIdConsulta = 0;

	private SistemaGestion() {
		super();
		listaPacientes = new ArrayList<>();
		listaMedicos = new ArrayList<>();
		listaCitas = new ArrayList<>();
		catalogoEnfermedades = new ArrayList<>();
		catalogoVacunas = new ArrayList<>();
		listaUsuarios = new ArrayList<>();
		listaSecretarias = new ArrayList<>();
		if (listaUsuarios.isEmpty()) {
			Usuario adminDefault = new Usuario("admin", "admin", "ADMIN", "admin-001");
			listaUsuarios.add(adminDefault);
		}
	}

	public static SistemaGestion getInstance(){
		if(clinica == null){
			clinica = new SistemaGestion();
		}
		return clinica;
	}

	public ArrayList<Paciente> getListaPacientes() {
		return listaPacientes;
	}
	public void setListaPacientes(ArrayList<Paciente> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}
	public ArrayList<Medico> getListaMedicos() {
		return listaMedicos;
	}
	public void setListaMedicos(ArrayList<Medico> listaMedicos) {
		this.listaMedicos = listaMedicos;
	}
	public ArrayList<Cita> getListaCitas() {
		return listaCitas;
	}
	public void setListaCitas(ArrayList<Cita> listaCitas) {
		this.listaCitas = listaCitas;
	}
	public ArrayList<Enfermedad> getCatalogoEnfermedades() {
		return catalogoEnfermedades;
	}
	public void setCatalogoEnfermedades(ArrayList<Enfermedad> catalogoEnfermedades) {
		this.catalogoEnfermedades = catalogoEnfermedades;
	}
	public ArrayList<Vacuna> getCatalogoVacunas() {
		return catalogoVacunas;
	}
	public void setCatalogoVacunas(ArrayList<Vacuna> catalogoVacunas) {
		this.catalogoVacunas = catalogoVacunas;
	}
	public ArrayList<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}
	public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	public ArrayList<Secretaria> getListaSecretarias() {
		return listaSecretarias;
	}
	public void setListaSecretarias(ArrayList<Secretaria> listaSecretarias) {
		this.listaSecretarias = listaSecretarias;
	}

	public Usuario validarLogin(String username, String password) {
		for (Usuario user : listaUsuarios) {
			if (user.getUsername().equals(username)) {
				if (user.VerifPass(password)) {
					return user; 
				} else {
					return null; 
				}
			}
		}
		return null;
	}
	public void agregarCita(Cita cita) {
		listaCitas.add(cita);
		genIdCita++;
	}
	public void registrarPaciente(Paciente pac) {
		listaPacientes.add(pac);
	}
	public void registrarMedico(Medico med) {
		listaMedicos.add(med);
	}
	public void agregarEnfermedadCatalogo(Enfermedad enf) {
		catalogoEnfermedades.add(enf);
	}
	public void agregarVacuna(Vacuna vac) {
		catalogoVacunas.add(vac);
	}
	public Cita buscarCitaPorId(String idCita) {
		Cita aux = null;
		boolean encontrado = false;
		int i = 0;
		while(!encontrado && i < listaCitas.size()) {
			if(listaCitas.get(i).getId().equalsIgnoreCase(idCita)) {
				aux = listaCitas.get(i);
				encontrado = true;
			}
			i++;
			
		}		
		return aux;
	}
	public Paciente buscarPacientePorId(String idPaciente) {
		Paciente aux = null;
		boolean encontrado = false;
		int i = 0;
		while(!encontrado && i < listaPacientes.size()) {
			if(listaPacientes.get(i).getId().equalsIgnoreCase(idPaciente)) {
				aux = listaPacientes.get(i);
				encontrado = true;
			}
			i++;
		}
		return aux;
	}
	public Medico buscarMedicoPorId(String idMedico) {
		Medico aux = null;
		boolean encontrado = false;
		int i = 0;
		while(!encontrado && i < listaMedicos.size()) {
			if(listaMedicos.get(i).getId().equalsIgnoreCase(idMedico)) {
				aux = listaMedicos.get(i);
				encontrado = true;
			}
			i++;
		}
		return aux;
	}
	public Consulta buscarConsultaPorId(String idConsulta) {
		for(Paciente paciente: listaPacientes) {
			ArrayList <Consulta> hisConsul = paciente.getHistorialConsultas();
			
			for(Consulta consulta: hisConsul) {
				if(consulta.getId().equalsIgnoreCase(idConsulta)) {
					return consulta;
				}
					
			}
		}
		return null;
	}
	public Secretaria buscarSecretariaPorId(String idSecretaria) {
	    Secretaria aux = null;
	    boolean encontrado = false;
	    int i = 0;
	    while(!encontrado && i < listaSecretarias.size()) { 
	        if(listaSecretarias.get(i).getId().equalsIgnoreCase(idSecretaria)) {
	            aux = listaSecretarias.get(i);
	            encontrado = true;
	        }
	        i++;
	    }
	    return aux;
	}
	
	public Cita crearCita(String idVisitante, String nombreVisitante,String idMedico, LocalDateTime horarioMed) {
		Cita aux = null;
		Medico med = buscarMedicoPorId(idMedico);
		if(med != null) {
			
			if(med.estaDisponible(horarioMed)) {
				agregarCita(aux);
				aux = new Cita("C-"+genIdCita, idVisitante, nombreVisitante, med, horarioMed, "pendiente");
				med.agregarEnAgenda(aux);
			}
		}
		
		return aux;
	}
	public void cancelarCita(String idCita) {
		Cita citaAux = buscarCitaPorId(idCita);
		if(citaAux != null && citaAux.citaPuedeCancelarse()) {
			citaAux.setEstado("cancelada");
			citaAux.getMedico().liberarAgenda(citaAux);		
		}
	}
	
	public Medico getMedicoLogueado(Usuario user) {
	    if (user != null && user.getRol().equalsIgnoreCase("MEDICO")) {
	        return buscarMedicoPorId(user.getIdPersonaVinculada());
	    } else {
	        return null;
	    }
	}
	
	public Secretaria getSecretariaLogueada(Usuario user) {
	    if (user != null && user.getRol().equalsIgnoreCase("SECRETARIA")) {
	        return buscarSecretariaPorId(user.getIdPersonaVinculada());
	    } else {
	        return null;
	    }
	}
	public Consulta iniciarConsulta(String idCita, String apellido, LocalDate fechaNacimiento, String sexo, String contacto, String sintomas) {
		Consulta ConAux = null;
		Cita citaAux = buscarCitaPorId(idCita);
		if(citaAux != null){
			Paciente pac = buscarPacientePorId(citaAux.getIdPaciente());
			if(pac == null){
				pac = new Paciente(citaAux.getIdPaciente(), citaAux.getNameVisitante(), apellido, fechaNacimiento, sexo, contacto);
				pac.inicializarRegistroVacunas(this.catalogoVacunas);
				registrarPaciente(pac);
				
				ConAux  = new Consulta("Co-"+genIdConsulta++, citaAux, pac, citaAux.getMedico(), LocalDateTime.now(), sintomas);
				citaAux.setEstado("completada");
				pac.agregarConsulta(ConAux);
			}
			else {
				ConAux  = new Consulta("Co-"+genIdConsulta++, citaAux, pac, citaAux.getMedico(), LocalDateTime.now(), sintomas);
				citaAux.setEstado("completada");
				pac.agregarConsulta(ConAux);
			}
		}
		return ConAux;
	}
	public boolean modificarCita(String idCita, LocalDateTime nuevaFecha, Medico nuevoMedico) {
		Cita cita = buscarCitaPorId(idCita);
		boolean realizado = false;
		
		if(cita != null) {
			if(cita.citaPuedeCancelarse()) {
				if(nuevoMedico.estaDisponible(nuevaFecha)) {
					cita.getMedico().liberarAgenda(cita);
					cita.setFechaCitada(nuevaFecha);
					cita.setMedico(nuevoMedico);
					nuevoMedico.agregarEnAgenda(cita);
					realizado = true;
				}
			}
		}
		return realizado;
	}
	public boolean finalizarConsulta(String idConsulta, Enfermedad diagnostico, String tratamiento, boolean esImportante, ArrayList <Vacuna>vacunasAplicadas) {
		boolean finalizado = false;
		Consulta consulta = buscarConsultaPorId(idConsulta);
		if(consulta != null) {
			consulta.finalizarConsulta(diagnostico, tratamiento, esImportante);
			for(Vacuna vacuna: vacunasAplicadas) {
				consulta.getPaciente().marcarVacunaAplicada(vacuna);
			}
			finalizado = true;
		}
		
		return finalizado;
	}
	public HashMap<Enfermedad, Integer>getReporteEnfermedades(){
		HashMap<Enfermedad, Integer> contEnfermedades = new HashMap<>();
		
		for(Paciente paciente: listaPacientes) {
			for(Consulta consulta: paciente.getHistorialConsultas()) {
				
				Enfermedad diagnostico = consulta.getDiagnostico();
				if(diagnostico != null) {
					int cont = contEnfermedades.getOrDefault(diagnostico, 0);
					contEnfermedades.put(diagnostico, cont+1);
				}
			}
		}
		return contEnfermedades;
	}
	public ArrayList<String> getTop5Enfermedades(){
		
		HashMap<Enfermedad, Integer> reporte = getReporteEnfermedades();
		ArrayList<Map.Entry<Enfermedad, Integer>> listaEntradas = new ArrayList<>(reporte.entrySet());
		listaEntradas.sort(new Comparator<Map.Entry<Enfermedad, Integer>>() {
			
	        @Override
	        public int compare(Map.Entry<Enfermedad, Integer> entry1, Map.Entry<Enfermedad, Integer> entry2) {
	            return entry2.getValue().compareTo(entry1.getValue());
	        }
	    });

	    ArrayList<String> reporteFinal = new ArrayList<>();

	    int maxResultados = Math.min(5, listaEntradas.size());
	    
	    for (int i = 0; i < maxResultados; i++) {
	        Map.Entry<Enfermedad, Integer> entrada = listaEntradas.get(i);

	        String nombreEnfermedad = entrada.getKey().getNombre(); 
	        int cantidadCasos = entrada.getValue();

	        String reporteItem = nombreEnfermedad + " - " + cantidadCasos + " casos";  
	        reporteFinal.add(reporteItem);
	    }
	    return reporteFinal;
	}
}

