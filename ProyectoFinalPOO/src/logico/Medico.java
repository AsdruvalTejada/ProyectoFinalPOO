package logico;


import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Medico {
	
	private String especialidad;
    private int limiteCitasPorDia;
    private int duracionCitaMinutos;
    private ArrayList<Cita> agenda;
    private ArrayList<TurnoJornada> horarioFijo;
    private ArrayList<BloqueoAgenda> exceptHorario;
    
	public Medico(String especialidad, int limiteCitasPorDia, int duracionCitaMinutos, ArrayList<Cita> agenda,
			ArrayList<TurnoJornada> horarioFijo, ArrayList<BloqueoAgenda> exceptHorario) {
		super();
		this.especialidad = especialidad;
		this.limiteCitasPorDia = 10;
		this.duracionCitaMinutos = 30;
		this.agenda = agenda;
		this.horarioFijo = horarioFijo;
		this.exceptHorario = exceptHorario;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public int getLimiteCitasPorDia() {
		return limiteCitasPorDia;
	}

	public void setLimiteCitasPorDia(int limiteCitasPorDia) {
		this.limiteCitasPorDia = limiteCitasPorDia;
	}

	public int getDuracionCitaMinutos() {
		return duracionCitaMinutos;
	}

	public void setDuracionCitaMinutos(int duracionCitaMinutos) {
		this.duracionCitaMinutos = duracionCitaMinutos;
	}

	public ArrayList<Cita> getAgenda() {
		return agenda;
	}

	public void setAgenda(ArrayList<Cita> agenda) {
		this.agenda = agenda;
	}

	public ArrayList<TurnoJornada> getHorarioFijo() {
		return horarioFijo;
	}

	public void setHorarioFijo(ArrayList<TurnoJornada> horarioFijo) {
		this.horarioFijo = horarioFijo;
	}

	public ArrayList<BloqueoAgenda> getExceptHorario() {
		return exceptHorario;
	}

	public void setExceptHorario(ArrayList<BloqueoAgenda> exceptHorario) {
		this.exceptHorario = exceptHorario;
	}
    
	public void definirLimiteCitas(int limite) {
        this.limiteCitasPorDia = limite;
    }
	

	public boolean estaDisponible(LocalDateTime fechaHora) {

	    LocalDateTime fechaHoraFinCita = fechaHora.plusMinutes(this.duracionCitaMinutos);
	    
	    DayOfWeek diaSemanaBuscado = fechaHora.getDayOfWeek();
	    LocalTime horaInicioPropuesta = fechaHora.toLocalTime();
	    LocalTime horaFinPropuesta = fechaHoraFinCita.toLocalTime();

	    boolean enTurnoFijo = false;
	    for (TurnoJornada turno : this.horarioFijo) {
	        if (turno.getDiaSemana().equalsIgnoreCase(diaSemanaBuscado.name())) {
	            boolean esDespuesOIgualInicio = !horaInicioPropuesta.isBefore(turno.getHoraInicio());
	            boolean esAntesOIgualFin = !horaFinPropuesta.isAfter(turno.getHoraFin());

	            if (esDespuesOIgualInicio && esAntesOIgualFin) {
	                enTurnoFijo = true;
	            }
	        }
	    }
	    
	    boolean chocaConBloqueo = false;
	    for (BloqueoAgenda bloqueo : this.exceptHorario) {
	        boolean haySolapamiento = fechaHora.isBefore(bloqueo.getFechaHoraFin()) &&
	                                  bloqueo.getFechaHoraInicio().isBefore(fechaHoraFinCita);
	        if (haySolapamiento) {
	            chocaConBloqueo = true;
	        }
	    }
	    LocalDate diaBuscado = fechaHora.toLocalDate();
	    int contadorCitasDia = 0;
	    for (Cita citaEnAgenda : this.agenda) {
	        if (citaEnAgenda.getFechaHora().toLocalDate().isEqual(diaBuscado)) {
	            contadorCitasDia++;
	        }
	    }
	    boolean limiteAlcanzado = (contadorCitasDia >= this.limiteCitasPorDia); 

	    boolean chocaConCita = false; 
	    for (Cita citaEnAgenda : this.agenda) {
	        LocalDateTime inicioCitaExistente = citaEnAgenda.getFechaHora();
	        LocalDateTime finCitaExistente = inicioCitaExistente.plusMinutes(this.duracionCitaMinutos);

	        boolean haySolapamiento = fechaHora.isBefore(finCitaExistente) && 
	                                  inicioCitaExistente.isBefore(fechaHoraFinCita);
	        
	        if (haySolapamiento) {
	            chocaConCita = true;
	        }
	    }
	    
	    return enTurnoFijo && !chocaConBloqueo && !limiteAlcanzado && !chocaConCita;
	}
}
