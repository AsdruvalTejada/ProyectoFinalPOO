package logico;
import java.util.ArrayList;

public class SistemaGestion {
	private ArrayList<Paciente> listaPacientes;
	private ArrayList<Medico> listaMedicos;
	private ArrayList<Cita> listaCitas;
	private ArrayList<Enfermedad> catalogoEnfermedades;
	private ArrayList<Vacuna> catalogoVacunas; // vacunas
	
	public SistemaGestion() {
		super();
		listaPacientes = new ArrayList<>();
		listaMedicos = new ArrayList<>();;
		listaCitas = new ArrayList<>();
		catalogoEnfermedades = new ArrayList<>();
		catalogoVacunas = new ArrayList<>();
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
	
	

}
