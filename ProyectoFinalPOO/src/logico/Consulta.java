package logico;

import java.time.LocalDateTime;

public class Consulta {

    private String id;
    private Cita citaAsociada;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fechaConsulta;
    private String sintomas;
    private Enfermedad diagnostico;
    private String tratamiento;
    private boolean esImportanteParaResumen; 

    public Consulta(String id, Cita citaAsociada, Paciente paciente, Medico medico, LocalDateTime fechaConsulta, String sintomas) {
        super();
        this.id = id;
        this.citaAsociada = citaAsociada;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaConsulta = fechaConsulta;
        this.sintomas = sintomas;
        this.diagnostico = null;
        this.tratamiento = "";
        this.esImportanteParaResumen = false;
    }

    public String getId() {
        return id;
    }

    public Cita getCitaAsociada() {
        return citaAsociada;
    }

    public void setCitaAsociada(Cita citaAsociada) {
        this.citaAsociada = citaAsociada;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDateTime fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public Enfermedad getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Enfermedad diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public boolean isEsImportanteParaResumen() {
        return this.esImportanteParaResumen; 
    }

    public void setEsImportanteParaResumen(boolean esImportanteParaResumen) {
        this.esImportanteParaResumen = esImportanteParaResumen;
    }
    
    public void finalizarConsulta(Enfermedad diagnostico, String tratamiento, boolean esImportanteParaResumen) {
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.esImportanteParaResumen = esImportanteParaResumen;
    }

    public boolean esVisiblePara(Medico medico) {
        if (this.medico.equals(medico)) {
            return true;
        }

        if (this.diagnostico != null && this.diagnostico.getEstaBajoVigilancia()) {
            return true;
        }

        return false;
    }
}