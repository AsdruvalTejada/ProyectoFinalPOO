package logico;
import java.util.ArrayList;
import java.time.LocalDateTime;

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
}

