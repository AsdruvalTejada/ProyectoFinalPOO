package logico;

import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Medico extends Persona {
	
	private String especialidad;
    private int limiteCitasPorDia;
    private int duracionCitaMinutos;
    private ArrayList<Cita> agenda;
    private ArrayList<TurnoJornada> horarioFijo;
    private ArrayList<BloqueoAgenda> exceptHorario;
    
	public Medico(String id, String name, String apellido, LocalDate fechaNacimiento, String sexo, String contacto,
			String especialidad, int limiteCitasPorDia, int duracionCitaMinutos, ArrayList<Cita> agenda,
			ArrayList<TurnoJornada> horarioFijo, ArrayList<BloqueoAgenda> exceptHorario) {
		super(id, name, apellido, fechaNacimiento, sexo, contacto);
		this.especialidad = especialidad;
		this.limiteCitasPorDia = limiteCitasPorDia;
		this.duracionCitaMinutos = duracionCitaMinutos;
		this.agenda = new ArrayList<>();
		this.horarioFijo = new ArrayList<>();
		this.exceptHorario = new ArrayList<>();
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
	
	public void agregarEnAgenda(Cita ag) {
		agenda.add(ag);
	}
	public void liberarAgenda(Cita ag) {
		agenda.remove(ag);
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
	        if (citaEnAgenda.getFechaCitada().toLocalDate().isEqual(diaBuscado)) {
	            contadorCitasDia++;
	        }
	    }
	    boolean limiteAlcanzado = (contadorCitasDia >= this.limiteCitasPorDia); 

	    boolean chocaConCita = false; 
	    for (Cita citaEnAgenda : this.agenda) {
	        LocalDateTime inicioCitaExistente = citaEnAgenda.getFechaCitada();
	        LocalDateTime finCitaExistente = inicioCitaExistente.plusMinutes(this.duracionCitaMinutos);

	        boolean haySolapamiento = fechaHora.isBefore(finCitaExistente) && 
	                                  inicioCitaExistente.isBefore(fechaHoraFinCita);
	        
	        if (haySolapamiento) {
	            chocaConCita = true;
	        }
	    }
	    
	    return enTurnoFijo && !chocaConBloqueo && !limiteAlcanzado && !chocaConCita;
	}
	
	public ArrayList<Consulta> verHistorialPaciente(Paciente paciente){
		
		ArrayList<Consulta> historial = paciente.getHistorialConsultas();
		ArrayList<Consulta> listaHistorial = new ArrayList<>();
		
		for(Consulta consulta: historial) {
			if(consulta.esVisiblePara(this)) {
				listaHistorial.add(consulta);
			}
		}
		
		return listaHistorial;
	}
	
	public void addBloqueoHorario(LocalDateTime inicio, LocalDateTime fin, String motivo) {
		BloqueoAgenda bloqueoAniadido = new BloqueoAgenda(inicio, fin, motivo);
		this.exceptHorario.add(bloqueoAniadido);
	}
	
	public void addTurnoJornada(String dia, LocalTime inicio, LocalTime fin) {
		TurnoJornada turnoAniadido = new TurnoJornada(dia, inicio, fin);
		this.horarioFijo.add(turnoAniadido);
	}
	
	public ArrayList<LocalTime> getSlotsDisponibles(LocalDate dia) {
	    
	    ArrayList<LocalTime> slotsDisponibles = new ArrayList<>();
	    
	    // 1. Encontrar el turno para ese día (Ej. Lunes 08:00 a 12:00)
	    TurnoJornada turnoDelDia = null;
	    for (TurnoJornada t : this.horarioFijo) {
            // Comparamos ignorando mayúsculas/minúsculas
            // El día de la semana en Java es en inglés (MONDAY, TUESDAY...)
	        if (t.getDiaSemana().equalsIgnoreCase(dia.getDayOfWeek().name())) {
	            turnoDelDia = t;
	            break;
	        }
	    }

	    if (turnoDelDia == null) {
	        return slotsDisponibles; // No trabaja ese día, devuelve lista vacía
	    }

	    // 2. Iterar por el día, hora por hora (o cada 30 min según la duración)
	    LocalTime horaActual = turnoDelDia.getHoraInicio();
	    
	    // Si duracionCitaMinutos es 0 o negativo, poner un default para evitar bucle infinito
	    int duracion = (this.duracionCitaMinutos > 0) ? this.duracionCitaMinutos : 30; 

        // Mientras la hora de fin de la cita no se pase del fin de turno
	    while (!horaActual.plusMinutes(duracion).isAfter(turnoDelDia.getHoraFin())) {
	        
	        // 3. Probamos si está disponible
	        LocalDateTime fechaHoraPrueba = LocalDateTime.of(dia, horaActual);
	        
	        if (this.estaDisponible(fechaHoraPrueba)) {
	            // ¡Está disponible! Lo añadimos a la lista
	            slotsDisponibles.add(horaActual);
	        }
	        
	        // Avanzamos a la siguiente hora
	        horaActual = horaActual.plusMinutes(duracion);
	    }
	    
	    return slotsDisponibles; // Devuelve la lista de horas libres
	}
}
