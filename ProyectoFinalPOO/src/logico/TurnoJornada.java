package logico;

import java.time.LocalTime;

public class TurnoJornada {
	
	private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    
    public TurnoJornada(String diaSemana, LocalTime horaInicio, LocalTime horaFin) {
		super();
		this.diaSemana = diaSemana;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}
    

}
