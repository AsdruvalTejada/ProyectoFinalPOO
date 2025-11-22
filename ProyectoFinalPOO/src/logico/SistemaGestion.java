package logico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

        Secretaria secrePrueba = new Secretaria("SEC-001", "Wilmary", "Hernández", LocalDate.of(1995, 3, 15), "F", "829-307-1234");
        registrarSecretaria(secrePrueba);
        crearUsuarioSecretaria(secrePrueba, "secre", "123");

        Medico medPrueba = new Medico("Med-001", "Angel", "Belliard", LocalDate.of(1995, 7, 23), "M", "809-555-5555", "Cardiologia", 10, 30, null, null, null);
        registrarMedico(medPrueba);
        crearUsuarioMedico(medPrueba, "doc", "123");
        
        secrePrueba.asignarMedico(medPrueba);
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

	public static SistemaGestion getClinica() {
		return clinica;
	}

	public static void setClinica(SistemaGestion clinica) {
		SistemaGestion.clinica = clinica;
	}

	public static int getGenIdCita() {
		return genIdCita;
	}

	public static void setGenIdCita(int genIdCita) {
		SistemaGestion.genIdCita = genIdCita;
	}

	public static int getGenIdConsulta() {
		return genIdConsulta;
	}

	public static void setGenIdConsulta(int genIdConsulta) {
		SistemaGestion.genIdConsulta = genIdConsulta;
	}

    public static SistemaGestion getInstance(){
        if(clinica == null){
            clinica = new SistemaGestion();
        }
        return clinica;
    }


    public Usuario validarLogin(String username, String password) {
        for (Usuario user : listaUsuarios) {
            if (user.getUsername().equals(username)) {
                if (user.VerifPass(password)) {
                    return user; 
                }
            }
        }
        return null;
    }

    public void agregarCita(Cita cita) {
        genIdCita++;
        listaCitas.add(cita);
    }
    
    public void registrarPaciente(Paciente pac) { listaPacientes.add(pac); }
    public void registrarMedico(Medico med) { listaMedicos.add(med); }
    public void agregarEnfermedadCatalogo(Enfermedad enf) { catalogoEnfermedades.add(enf); }
    public void agregarVacuna(Vacuna vac) { catalogoVacunas.add(vac); }   
    public void registrarSecretaria(Secretaria sec) { this.listaSecretarias.add(sec); }

    public Cita buscarCitaPorId(String idCita) {
        for(Cita c : listaCitas) {
            if(c.getId().equalsIgnoreCase(idCita)) return c;
        }
        return null;
    }
    
    public Paciente buscarPacientePorId(String idPaciente) {
        for(Paciente p : listaPacientes) {
            if(p.getId().equalsIgnoreCase(idPaciente)) return p;
        }
        return null;
    }
    
    public Medico buscarMedicoPorId(String idMedico) {
        for(Medico m : listaMedicos) {
            if(m.getId().equalsIgnoreCase(idMedico)) return m;
        }
        return null;
    }
    
    public Secretaria buscarSecretariaPorId(String idSecretaria) {
        for(Secretaria s : listaSecretarias) {
            if(s.getId().equalsIgnoreCase(idSecretaria)) return s;
        }
        return null;
    }

    public Consulta buscarConsultaPorId(String idConsulta) {
        for(Paciente paciente: listaPacientes) {
            for(Consulta consulta: paciente.getHistorialConsultas()) {
                if(consulta.getId().equalsIgnoreCase(idConsulta)) {
                    return consulta;
                }
            }
        }
        return null;
    }

    public Cita crearCita(String idVisitante, String nombreVisitante, String idMedico, LocalDateTime horarioMed) {
        Cita aux = null;
        Medico med = buscarMedicoPorId(idMedico);
        
        if(med != null) {
            if(med.estaDisponible(horarioMed)) {
                aux = new Cita("C-"+(genIdCita+1), idVisitante, nombreVisitante, med, horarioMed, "pendiente");
                agregarCita(aux);
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
        }
        return null;
    }
    
    public Secretaria getSecretariaLogueada(Usuario user) {
        if (user != null && user.getRol().equalsIgnoreCase("SECRETARIA")) {
            return buscarSecretariaPorId(user.getIdPersonaVinculada());
        }
        return null;
    }
    
    public void crearUsuarioMedico(Medico med, String username, String password) {
        Usuario nuevoUsuario = new Usuario(username, password, "MEDICO", med.getId());
        this.listaUsuarios.add(nuevoUsuario);
    }
    
    public void crearUsuarioSecretaria(Secretaria sec, String username, String password) {
        Usuario nuevoUsuario = new Usuario(username, password, "SECRETARIA", sec.getId());
        this.listaUsuarios.add(nuevoUsuario);
    }

    public Consulta iniciarConsulta(String idCita, String apellido, LocalDate fechaNacimiento, 
                                    String sexo, String contacto, String sintomas, 
                                    String tipoSangre, float estatura, float peso, String presionArterial) {
        Consulta conAux = null;
        Cita citaAux = buscarCitaPorId(idCita);
        
        if(citaAux != null){
            Paciente pac = buscarPacientePorId(citaAux.getIdPaciente());
            
            if(pac == null){
                pac = new Paciente(citaAux.getIdPaciente(), citaAux.getNameVisitante(), apellido, 
                                   fechaNacimiento, sexo, contacto, tipoSangre, estatura);
                pac.inicializarRegistroVacunas(this.catalogoVacunas);
                registrarPaciente(pac);
            } else {
                pac.setEstatura(estatura); 
            }

            conAux = new Consulta("Co-"+(++genIdConsulta), citaAux, pac, citaAux.getMedico(), 
                                  LocalDateTime.now(), sintomas, peso, presionArterial);
            
            citaAux.setEstado("activa");
            pac.agregarConsulta(conAux);
        }
        return conAux;
    }

    public boolean finalizarConsulta(String idConsulta, Enfermedad diagnostico, String tratamiento, 
                                     boolean esImportante, ArrayList<Vacuna> vacunasAplicadas) {
        boolean finalizado = false;
        Consulta consulta = buscarConsultaPorId(idConsulta);
        if(consulta != null) {
            consulta.finalizarConsulta(diagnostico, tratamiento, esImportante);
            consulta.getCitaAsociada().setEstado("completada");
            
            for(Vacuna vacuna: vacunasAplicadas) {
                consulta.getPaciente().marcarVacunaAplicada(vacuna);
            }
            finalizado = true;
        }
        return finalizado;
    }

    public ArrayList<String> getTop5Enfermedades(){
        HashMap<Enfermedad, Integer> reporte = new HashMap<>();
        for(Paciente paciente: listaPacientes) {
            for(Consulta consulta: paciente.getHistorialConsultas()) {
                Enfermedad diagnostico = consulta.getDiagnostico();
                if(diagnostico != null) {
                    reporte.put(diagnostico, reporte.getOrDefault(diagnostico, 0) + 1);
                }
            }
        }

        ArrayList<Map.Entry<Enfermedad, Integer>> listaEntradas = new ArrayList<>(reporte.entrySet());
        listaEntradas.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        ArrayList<String> reporteFinal = new ArrayList<>();
        int maxResultados = Math.min(5, listaEntradas.size());

        for (int i = 0; i < maxResultados; i++) {
            Map.Entry<Enfermedad, Integer> entrada = listaEntradas.get(i);
            reporteFinal.add(entrada.getKey().getNombre() + " - " + entrada.getValue() + " casos");
        }
        return reporteFinal;
    }

    public HashMap<String, Integer> getReporteVacunacion() {
        HashMap<String, Integer> reporte = new HashMap<>();
        for (Vacuna vacCatalogo : this.catalogoVacunas) {
            int cantAplicadas = 0;
            int cantPendientes = 0;
            for (Paciente paciente : this.listaPacientes) {
                Boolean estaAplicada = paciente.getRegistroVacunacion().get(vacCatalogo);
                if (estaAplicada != null && estaAplicada) {
                    cantAplicadas++;
                } else {
                    cantPendientes++;
                }
            }
            reporte.put(vacCatalogo.getNombre() + " - Aplicadas", cantAplicadas);
            reporte.put(vacCatalogo.getNombre() + " - Pendientes", cantPendientes);
        }
        return reporte;
    }
}