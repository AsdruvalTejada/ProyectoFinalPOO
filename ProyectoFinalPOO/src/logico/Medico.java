package logico;

import java.util.ArrayList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class Medico extends Persona {
	private static final long serialVersionUID = 1L;
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
        this.agenda = (agenda != null) ? agenda : new ArrayList<>();
        
        if(horarioFijo != null && !horarioFijo.isEmpty()) {
            this.horarioFijo = horarioFijo;
        } else {
            inicializarHorarioDefecto();
        }
        
        this.exceptHorario = (exceptHorario != null) ? exceptHorario : new ArrayList<>();
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
    
    private void inicializarHorarioDefecto() {
        this.horarioFijo = new ArrayList<>();
        String[] dias = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"};
        for (String dia : dias) {
            boolean trabaja = !dia.equals("SABADO") && !dia.equals("DOMINGO");
            this.horarioFijo.add(new TurnoJornada(dia, LocalTime.of(8, 0), LocalTime.of(17, 0), trabaja));
        }
    }

    public void agregarEnAgenda(Cita ag) {
        this.agenda.add(ag);
    }

    public void liberarAgenda(Cita ag) {
        this.agenda.remove(ag);
    }
    
    public ArrayList<LocalTime> getSlotsDisponibles(LocalDate fecha) {
        ArrayList<LocalTime> slots = new ArrayList<>();
        
        String nombreDiaHoy = obtenerNombreDia(fecha.getDayOfWeek());
        
        TurnoJornada turnoHoy = null;
        for(TurnoJornada t : horarioFijo) {
            if(t.getDiaSemana().equalsIgnoreCase(nombreDiaHoy)) {
                turnoHoy = t;
                break;
            }
        }
        
        if(turnoHoy == null || !turnoHoy.isActivo()) {
            return slots; 
        }
        
        int citasHoy = 0;
        for(Cita c : agenda) {
            if(c.getFechaCitada().toLocalDate().equals(fecha) && !c.getEstado().equalsIgnoreCase("Cancelada")) {
                citasHoy++;
            }
        }
        if(citasHoy >= limiteCitasPorDia) {
            return slots; 
        }

        LocalTime cursor = turnoHoy.getHoraInicio();
        while(cursor.plusMinutes(duracionCitaMinutos).isBefore(turnoHoy.getHoraFin()) || cursor.plusMinutes(duracionCitaMinutos).equals(turnoHoy.getHoraFin())) {
            LocalDateTime fechaHoraSlot = LocalDateTime.of(fecha, cursor);
            if(estaDisponible(fechaHoraSlot)) {
                slots.add(cursor);
            }
            cursor = cursor.plusMinutes(duracionCitaMinutos);
        }
        
        return slots;
    }

    public boolean estaDisponible(LocalDateTime fechaHora) {
        LocalDateTime fechaHoraFinCita = fechaHora.plusMinutes(this.duracionCitaMinutos);
        for (BloqueoAgenda bloqueo : this.exceptHorario) {
            boolean solapa = fechaHora.isBefore(bloqueo.getFechaHoraFin()) && 
                             fechaHoraFinCita.isAfter(bloqueo.getFechaHoraInicio());
            if (solapa) return false; 
        }
        for (Cita c : this.agenda) {
            if(!c.getEstado().equalsIgnoreCase("Cancelada")) {
                LocalDateTime inicioCita = c.getFechaCitada();
                LocalDateTime finCita = inicioCita.plusMinutes(this.duracionCitaMinutos);
                boolean solapa = fechaHora.isBefore(finCita) && fechaHoraFinCita.isAfter(inicioCita);
                if (solapa) return false; 
            }
        }
        return true; 
    }
    
    private String obtenerNombreDia(DayOfWeek day) {
        String d = day.getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase();
        d = d.replace("É", "E").replace("Á", "A");
        return d;
    }
    
    public void agregarBloqueo(BloqueoAgenda bloqueo) {
        this.exceptHorario.add(bloqueo);
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
    
    public void addTurnoJornada(String dia, java.time.LocalTime inicio, java.time.LocalTime fin) {
        TurnoJornada turno = new TurnoJornada(dia, inicio, fin, false);
        this.horarioFijo.add(turno);
    }

    
    @Override
    public String toString() {
        return this.name + " " + this.apellido + " (" + this.especialidad + ")";
    }
}