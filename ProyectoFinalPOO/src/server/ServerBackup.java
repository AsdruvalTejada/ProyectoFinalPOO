package server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerBackup {

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(8000)) {
            System.out.println("SERVIDOR PARA COPIAS DE SEGURIDAD");

            while (true) {
                Socket socket = server.accept();
                new Thread(() -> recibirYGuardar(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recibirYGuardar(Socket socket) {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            
            Object sistemaRecibido = in.readObject();
            
            File carpeta = new File("Copias_De_Seguridad");
            if (!carpeta.exists()) carpeta.mkdir();	
            
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String nombreArchivo = "Copias_De_Seguridad" + File.separator + "Backup_Clinica_" + fecha + ".dat";
            
            try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo);
                 ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
                
                objectOut.writeObject(sistemaRecibido);
                System.out.println("Copia guardada exitosamente: " + nombreArchivo);
            }
            
        } catch (Exception e) {
            System.out.println("Error al recibir el respaldo: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException e) {}
        }
    }
}