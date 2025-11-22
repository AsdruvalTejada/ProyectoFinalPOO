package logico;

import java.time.LocalDateTime;
import java.io.Serializable;

public class BloqueoAgenda implements Serializable{
	private static final long serialVersionUID = 1L;
	private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private String motivo;
    
	public BloqueoAgenda(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, String motivo) {
		super();
		this.fechaHoraInicio = fechaHoraInicio;
		this.fechaHoraFin = fechaHoraFin;
		this.motivo = motivo;
	}

	public LocalDateTime getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
		this.fechaHoraInicio = fechaHoraInicio;
	}

	public LocalDateTime getFechaHoraFin() {
		return fechaHoraFin;
	}

	public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
		this.fechaHoraFin = fechaHoraFin;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
    
    
}
