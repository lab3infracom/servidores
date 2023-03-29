import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.*;
import java.security.*;
import java.util.logging.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ClienteTCP extends Thread{
    private static final String IP_SERVIDOR = "192.168.44.133"; // la dirección IP del servidor
    private static final int PUERTO = 43215; // el puerto del servidor
    private int id;
    private int numclientes;
    private static Logger LOGGER;
    public ClienteTCP(int id, int numclientes, Logger logger) {
        this.id = id;
        this.numclientes = numclientes;
        this.LOGGER = logger;
    }
    public void run() {
        try {
            long tiempoInicio = System.currentTimeMillis();
            Socket clienteSocket = new Socket(IP_SERVIDOR, PUERTO);
            LOGGER.info("Conexión establecida con el servidor " + IP_SERVIDOR + ":" + PUERTO);
            LOGGER.info("IDENTIFICACION DEL CLIENTE " + clienteSocket);
            try {
                DataInputStream dis = new DataInputStream(clienteSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clienteSocket.getOutputStream());
                dos.writeUTF("MENSAJE DE CONFIRMACION DE CONEXION");
                dos.flush();
                int longitud = dis.readInt();
                byte[] buffer = new byte[longitud];
                int size = buffer.length;
                LOGGER.info("Tamaño del archivo: " + size + " bytes");
                dis.readFully(buffer, 0, longitud);
                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("SHA-256");
                    byte[] hashBytes = md.digest(buffer);
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < hashBytes.length; i++) {
                        sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    String hashString = sb.toString().toUpperCase();
                    System.out.println("EL HASH CALCULADO ES: "+hashString);
                    
                    String codhash = dis.readUTF();
                    System.out.println("EL HASH RECIBIDO FUE: "+codhash);
                    if(hashString.equals(codhash))
                    {
                        System.out.println("EL ARCHIVO FUE RECIBIDO CORRECTAMENTE");
                        LOGGER.info("EL ARCHIVO FUE RECIBIDO CORRECTAMENTE");
                        long tiempoFinal = System.currentTimeMillis();
                        long tiempoTotal = tiempoFinal - tiempoInicio;
                        System.out.println("EL TIEMPO TOTAL DE DESCARGA FUE: "+tiempoTotal+" milisegundos");
                        LOGGER.info("EL TIEMPO TOTAL DE DESCARGA FUE: "+tiempoTotal+" milisegundos");
                    }
                    else
                    {
                        System.out.println("EL ARCHIVO FUE RECIBIDO INCORRECTAMENTE");
                        LOGGER.info("EL ARCHIVO FUE RECIBIDO INCORRECTAMENTE");
                    }
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                try (FileOutputStream fos = new FileOutputStream("ArchivosRecibidos/Cliente"+id+"Prueba"+numclientes+".txt")) {
                    fos.write(buffer);
                } catch (IOException e) {
                    System.out.println("Error al escribir el archivo recibido: " + e.getMessage());
                }
            } catch (IOException e) {
                System.out.println("Error al recibir el archivo: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }
        finally {
            //clienteSocket.close();
            System.out.println("Conexión cerrada");
        }
    }
}
