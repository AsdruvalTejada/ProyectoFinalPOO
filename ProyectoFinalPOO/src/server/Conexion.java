package server;

import java.io.*;
import java.net.*;

public class Conexion extends Thread {
	Socket socket;
	DataInputStream entrada;
	DataOutputStream salida;
	String idUsuario;

	public Conexion(Socket s) {
		this.socket = s;
		try {
			entrada = new DataInputStream(new BufferedInputStream(s.getInputStream()));
			salida = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
		} catch (IOException e) { e.printStackTrace(); }
	}

	public void run() {
		try {
			while (true) {
				String mensaje = entrada.readUTF();

				if (mensaje.startsWith("LOGIN:")) { 
					idUsuario = mensaje.split(":")[1];
					ServerCentral.medicosConectados.put(idUsuario, this);
                    System.out.println("Médico conectado: " + idUsuario);
				} 
				else if (mensaje.startsWith("NUEVA_CITA;")) {

					String[] partes = mensaje.split(";");

					ServerCentral.agregarAlerta(partes[1], partes[2], partes[3]);
				}
                else if (mensaje.startsWith("STATS_UPDATE;")) {
                	
                    String datos = mensaje.split(";", 2)[1];
                    
                    ServerCentral.broadcast("VIGILANCIA;" + datos);
                    System.out.println("Difundiendo actualización epidemiológica...");
                }
			}
		} catch (IOException e) {
			if(idUsuario != null) ServerCentral.medicosConectados.remove(idUsuario);
		}
	}

	public void enviarMensaje(String msg) {
		try {
			salida.writeUTF(msg);
			salida.flush();
		} catch (IOException e) { e.printStackTrace(); }
	}
}