package logico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;

public class MainPruebas {

    public static void main(String[] args) {
        System.out.println("--- INICIANDO GENERACIÓN MASIVA DE DATOS DE PRUEBA ---");
        
        // 1. Empezar limpio (sin cargar datos previos para evitar duplicados)
        // Si quieres conservar lo que tienes, comenta estas líneas de borrado.
        File archivoDatos = new File("clinica_data.dat");
        if (archivoDatos.exists()) {
            archivoDatos.delete();
            System.out.println("Datos anteriores eliminados para empezar de cero.");
        }
        
        SistemaGestion sistema = SistemaGestion.getInstance();

        // --- 2. CREAR VACUNAS ---
        System.out.println("Generando Vacunas...");
        Vacuna v1 = new Vacuna("V-001", "Influenza");
        Vacuna v2 = new Vacuna("V-002", "Pfizer COVID-19");
        Vacuna v3 = new Vacuna("V-003", "Sinovac COVID-19");
        Vacuna v4 = new Vacuna("V-004", "Tétanos (DTP)");
        Vacuna v5 = new Vacuna("V-005", "Hepatitis B");
        Vacuna v6 = new Vacuna("V-006", "Sarampión (SRP)");
        
        sistema.agregarVacuna(v1);
        sistema.agregarVacuna(v2);
        sistema.agregarVacuna(v3);
        sistema.agregarVacuna(v4);
        sistema.agregarVacuna(v5);
        sistema.agregarVacuna(v6);

        // --- 3. CREAR ENFERMEDADES ---
        System.out.println("Generando Enfermedades...");
        Enfermedad e1 = new Enfermedad("E-001", "Gripe Estacional", false);
        Enfermedad e2 = new Enfermedad("E-002", "COVID-19", true); // Vigilancia
        Enfermedad e3 = new Enfermedad("E-003", "Dengue Hemorrágico", true); // Vigilancia
        Enfermedad e4 = new Enfermedad("E-004", "Hipertensión", false);
        Enfermedad e5 = new Enfermedad("E-005", "Diabetes Tipo 2", false);
        Enfermedad e6 = new Enfermedad("E-006", "Cólera", true); // Vigilancia
        Enfermedad e7 = new Enfermedad("E-007", "Neumonía", false);
        Enfermedad e8 = new Enfermedad("E-008", "Malaria", true); // Vigilancia
        
        sistema.agregarEnfermedadCatalogo(e1);
        sistema.agregarEnfermedadCatalogo(e2);
        sistema.agregarEnfermedadCatalogo(e3);
        sistema.agregarEnfermedadCatalogo(e4);
        sistema.agregarEnfermedadCatalogo(e5);
        sistema.agregarEnfermedadCatalogo(e6);
        sistema.agregarEnfermedadCatalogo(e7);
        sistema.agregarEnfermedadCatalogo(e8);

        // --- 4. CREAR MEDICOS ---
        System.out.println("Generando Médicos...");
        
        // Dr. Juan (General)
        Medico m1 = new Medico("MED-01", "Juan", "Pérez", LocalDate.of(1980, 5, 20), "M", "809-555-0101", "Medicina General", 15, 30, null, null, null);
        m1.addTurnoJornada("MONDAY", LocalTime.of(8, 0), LocalTime.of(12, 0));
        m1.addTurnoJornada("WEDNESDAY", LocalTime.of(8, 0), LocalTime.of(12, 0));
        m1.addTurnoJornada("FRIDAY", LocalTime.of(8, 0), LocalTime.of(12, 0));
        sistema.registrarMedico(m1);
        sistema.crearUsuarioMedico(m1, "juan", "123");

        // Dra. Ana (Pediatra)
        Medico m2 = new Medico("MED-02", "Ana", "Gómez", LocalDate.of(1985, 8, 10), "F", "809-555-0202", "Pediatría", 12, 45, null, null, null);
        m2.addTurnoJornada("TUESDAY", LocalTime.of(14, 0), LocalTime.of(18, 0));
        m2.addTurnoJornada("THURSDAY", LocalTime.of(14, 0), LocalTime.of(18, 0));
        sistema.registrarMedico(m2);
        sistema.crearUsuarioMedico(m2, "ana", "123");

        // Dr. Luis (Epidemiólogo)
        Medico m3 = new Medico("MED-03", "Luis", "Ramírez", LocalDate.of(1975, 1, 15), "M", "809-555-0303", "Epidemiología", 10, 60, null, null, null);
        m3.addTurnoJornada("MONDAY", LocalTime.of(9, 0), LocalTime.of(13, 0));
        m3.addTurnoJornada("WEDNESDAY", LocalTime.of(9, 0), LocalTime.of(13, 0));
        m3.addTurnoJornada("FRIDAY", LocalTime.of(9, 0), LocalTime.of(13, 0));
        sistema.registrarMedico(m3);
        sistema.crearUsuarioMedico(m3, "luis", "123");
        
        // Dra. Sofia (Cardióloga)
        Medico m4 = new Medico("MED-04", "Sofia", "Martínez", LocalDate.of(1988, 11, 30), "F", "809-555-0404", "Cardiología", 8, 45, null, null, null);
        m4.addTurnoJornada("MONDAY", LocalTime.of(14, 0), LocalTime.of(18, 0));
        m4.addTurnoJornada("WEDNESDAY", LocalTime.of(14, 0), LocalTime.of(18, 0));
        sistema.registrarMedico(m4);
        sistema.crearUsuarioMedico(m4, "sofia", "123");

        // --- 5. CREAR PACIENTES Y GENERAR HISTORIAL MASIVO ---
        System.out.println("Generando Pacientes e Historial Masivo...");
        
        Random random = new Random();
        int totalPacientes = 50; // ¡50 Pacientes!
        
        for(int i=1; i<=totalPacientes; i++) {
            String sexo = (random.nextBoolean()) ? "M" : "F";
            String nombre = (sexo.equals("F") ? "Maria" : "Jose") + " " + i;
            String apellido = "Apellido" + i;
            
            Paciente p = new Paciente("001-00000" + i, nombre, apellido, LocalDate.of(1970 + random.nextInt(30), 1, 1), sexo, "809-555-00"+i, "O+", 1.70f);
            
            // Inicializar vacunas
            p.inicializarRegistroVacunas(sistema.getCatalogoVacunas());
            
            // Aplicar vacunas aleatorias (Para que el gráfico de barras se vea variado)
            if(random.nextBoolean()) p.marcarVacunaAplicada(v1); // Influenza
            if(random.nextBoolean()) p.marcarVacunaAplicada(v2); // Pfizer
            if(random.nextBoolean()) p.marcarVacunaAplicada(v3); // Sinovac
            if(i % 3 == 0) p.marcarVacunaAplicada(v4); // Tétanos
            if(i % 4 == 0) p.marcarVacunaAplicada(v5); // Hepatitis
            if(i % 5 == 0) p.marcarVacunaAplicada(v6); // Sarampión
            
            sistema.registrarPaciente(p);
            
            // --- GENERAR CONSULTAS (MUCHAS) ---
            
            // Año 2023
            if(random.nextBoolean()) crearConsulta(sistema, p, m1, e4, 2023, 1 + random.nextInt(12), 1 + random.nextInt(28));
            if(random.nextBoolean()) crearConsulta(sistema, p, m1, e1, 2023, 1 + random.nextInt(12), 1 + random.nextInt(28));
            
            // Año 2024 (Epidemia de Dengue simulada en verano)
            if(random.nextBoolean()) crearConsulta(sistema, p, m3, e3, 2024, 6 + random.nextInt(3), 1 + random.nextInt(28)); 
            if(random.nextBoolean()) crearConsulta(sistema, p, m2, e1, 2024, 1, 15); // Gripe en invierno
            
            // Año 2025 (Datos actuales para la tendencia)
            // Simulamos consultas todos los meses
            int consultasEsteAno = 1 + random.nextInt(4); // Cada paciente fue de 1 a 4 veces este año
            for(int k=0; k<consultasEsteAno; k++) {
                int mes = 1 + random.nextInt(11); // Ene - Nov
                Enfermedad enf = null;
                Medico med = m1;
                
                // Aleatoriedad de enfermedades
                int r = random.nextInt(100);
                if (r < 30) { enf = e1; med = m1; } // 30% Gripe
                else if (r < 50) { enf = e4; med = m4; } // 20% Hipertensión
                else if (r < 70) { enf = e2; med = m3; } // 20% Covid (Vigilancia)
                else if (r < 85) { enf = e5; med = m1; } // 15% Diabetes
                else { enf = e3; med = m3; } // 15% Dengue (Vigilancia)
                
                crearConsulta(sistema, p, med, enf, 2025, mes, 1 + random.nextInt(28));
            }
        }

        // --- 6. GUARDAR DATOS ---
        sistema.guardarDatos();
        System.out.println("--- DATOS MASIVOS GENERADOS Y GUARDADOS EN clinica_data.dat ---");
        System.out.println("Ahora ejecuta Login.java para ver los reportes llenos de información.");
    }
    
    private static void crearConsulta(SistemaGestion sys, Paciente p, Medico m, Enfermedad e, int anio, int mes, int dia) {
        if(dia > 28) dia = 28;
        LocalDateTime fecha = LocalDateTime.of(anio, mes, dia, 10, 0);
        
        // 1. Cita histórica
        Cita c = new Cita("HIST-" + sys.getGenIdCita(), p.getId(), p.getNombreCompleto(), m, fecha, "Completada");
        sys.agregarCita(c);
        
        // 2. Consulta histórica
        Consulta cons = new Consulta("CONS-" + sys.getGenIdConsulta(), c, p, m, fecha, "Sintomas simulados", 70.0f, "120/80");
        cons.finalizarConsulta(e, "Tratamiento estándar", (e.getEstaBajoVigilancia())); 
        
        // 3. Guardar
        p.agregarConsulta(cons);
        sys.setGenIdCita(sys.getGenIdCita() + 1);
        sys.setGenIdConsulta(sys.getGenIdConsulta() + 1);
    }
}