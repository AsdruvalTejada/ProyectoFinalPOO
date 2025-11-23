package server;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServerCentral {

    // Mapa para saber dónde está conectado cada médico (ID -> Hilo)
    public static HashMap<String, Conexion> medicosConectados = new HashMap<>();
    
    // Lista de Recordatorios (Citas programadas que el servidor debe vigilar)
    private static ArrayList<RecordatorioCita> listaAlertas = new ArrayList<>();

    public static void main(String args[]) {
        try (ServerSocket sfd = new ServerSocket(7000)) {
            System.out.println("--- SERVIDOR DE ALERTAS MÉDICAS INICIADO (Puerto 7000) ---");

            // 1. HILO CRONÓMETRO (Tu idea principal)
            iniciarRelojMonitor();

            // 2. BUCLE DE CONEXIÓN
            while (true) {
                Socket nsfd = sfd.accept();
                Conexion flujo = new Conexion(nsfd);
                flujo.start();
            }
        } catch (IOException ioe) {
            System.out.println("Error en servidor: " + ioe);
        }
    }

    private static void iniciarRelojMonitor() {
        Thread reloj = new Thread(() -> {
            while (true) {
                try {
                    // Revisamos cada 30 segundos
                    Thread.sleep(30000); 
                    
                    LocalTime ahora = LocalTime.now();
                    
                    // --- CAMBIO CLAVE AQUÍ ---
                    // Calculamos qué hora será en 5 minutos
                    LocalTime horaEn5Minutos = ahora.plusMinutes(5);
                    
                    // Formateamos ESA hora futura para buscar coincidencias
                    String horaBusquedaStr = horaEn5Minutos.format(DateTimeFormatter.ofPattern("HH:mm"));
                    
                    System.out.println("Buscando citas programadas para las: " + horaBusquedaStr);

                    synchronized (listaAlertas) {
                        Iterator<RecordatorioCita> it = listaAlertas.iterator();
                        while (it.hasNext()) {
                            RecordatorioCita recordatorio = it.next();
                            
                            // Comparamos la hora de la cita con la hora futura (5 min más)
                            if (recordatorio.horaCita.equals(horaBusquedaStr)) {
                                
                                Conexion conexionMedico = medicosConectados.get(recordatorio.idMedico);
                                
                                if (conexionMedico != null) {
                                    // Actualizamos el mensaje para que tenga sentido
                                    String mensaje = "ALERTA: En 5 minutos tiene cita con " + recordatorio.nombrePaciente;
                                    conexionMedico.enviarMensaje(mensaje);
                                    System.out.println("Alerta enviada a " + recordatorio.idMedico);
                                    
                                    // Removemos la alerta para no avisar dos veces
                                    it.remove(); 
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        reloj.start();
    }

    // Clase auxiliar para guardar los datos en memoria del servidor
    static class RecordatorioCita {
        String idMedico;
        String horaCita; // Formato "14:00"
        String nombrePaciente;

        public RecordatorioCita(String idMedico, String horaCita, String nombrePaciente) {
            this.idMedico = idMedico;
            this.horaCita = horaCita;
            this.nombrePaciente = nombrePaciente;
        }
    }
    
    // Métodos estáticos para que el Manejador acceda
    public static void agregarAlerta(String idMed, String hora, String paciente) {
        synchronized (listaAlertas) {
            listaAlertas.add(new RecordatorioCita(idMed, hora, paciente));
            System.out.println("Nueva alerta registrada para Dr. " + idMed + " a las " + hora);
        }
    }
}