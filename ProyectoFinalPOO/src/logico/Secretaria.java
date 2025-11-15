package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Secretaria extends Persona {

	private ArrayList<Medico> medicosAsignados;

	public Secretaria(String id, String name, String apellido, LocalDate fechaNacimiento, String sexo, String contacto) {
		super(id, name, apellido, fechaNacimiento, sexo, contacto);
		this.medicosAsignados = new ArrayList<>();
	}

	public ArrayList<Medico> getMedicosAsignados() {
		return medicosAsignados;
	}

	public void setMedicosAsignados(ArrayList<Medico> medicosAsignados) {
		this.medicosAsignados = medicosAsignados;
	}

	public boolean asignarMedico(Medico medico) {

		if (!this.medicosAsignados.contains(medico)) {
			this.medicosAsignados.add(medico);
			return true;
		}

		if (this.medicosAsignados.size() >= 3) {
			return false;
		}

		return false;
	}

	public boolean desasignarMedico(Medico medico) {
		if (this.medicosAsignados.size() <= 1 && this.medicosAsignados.contains(medico)) {
			return false;
		}
		return this.medicosAsignados.remove(medico);
	}
}