package server;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServerCentral {

    public static HashMap<String, Conexion> medicosConectados = new HashMap<>();
    
    private static ArrayList<RecordatorioCita> listaAlertas = new ArrayList<>();

    public static void main(String args[]) {
        try (ServerSocket sfd = new ServerSocket(7000)) {
            System.out.println("--- SERVIDOR DE ALERTAS MÉDICAS INICIADO (Puerto 7000) ---");

            iniciarRelojMonitor();

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
                    Thread.sleep(10000);
                    
                    LocalTime ahora = LocalTime.now();
                    LocalTime horaEn5Minutos = ahora.plusMinutes(5);
                    String horaBusquedaStr = horaEn5Minutos.format(DateTimeFormatter.ofPattern("HH:mm"));
                    System.out.println("[DEBUG] Hora Servidor: " + ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss")) 
                            + " | Buscando citas programadas para las: " + horaBusquedaStr);

                    synchronized (listaAlertas) {
                        Iterator<RecordatorioCita> it = listaAlertas.iterator();
                        while (it.hasNext()) {
                            RecordatorioCita recordatorio = it.next();
                            
                            System.out.println("[DEBUG] Comparando: Objetivo [" + horaBusquedaStr + "] vs Guardada [" + recordatorio.horaCita + "]");
                            
                            if (recordatorio.horaCita.equals(horaBusquedaStr)) {
                                
                                Conexion conexionMedico = medicosConectados.get(recordatorio.idMedico);
                                
                                if (conexionMedico != null) {
                                    String mensaje = "ALERTA: En 5 minutos tiene cita con " + recordatorio.nombrePaciente;
                                    conexionMedico.enviarMensaje(mensaje);
                                    System.out.println(">>> ¡ALERTA ENVIADA AL MÉDICO " + recordatorio.idMedico + "! <<<");
                                    it.remove(); 
                                } else {
                                    System.out.println("[DEBUG] Hora coincide, pero el médico " + recordatorio.idMedico + " NO está conectado.");
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

    static class RecordatorioCita {
        String idMedico;
        String horaCita;
        String nombrePaciente;

        public RecordatorioCita(String idMedico, String horaCita, String nombrePaciente) {
            this.idMedico = idMedico;
            this.horaCita = horaCita;
            this.nombrePaciente = nombrePaciente;
        }
    }
    
    public static void agregarAlerta(String idMed, String hora, String paciente) {
        synchronized (listaAlertas) {
            listaAlertas.add(new RecordatorioCita(idMed, hora, paciente));
            System.out.println("Nueva alerta registrada para Dr. " + idMed + " a las " + hora);
        }
    }
}