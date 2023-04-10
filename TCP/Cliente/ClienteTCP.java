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
                dos.writeUTF("MENSAJE DE CONFIRMACION DE CONEXION\n");
                dos.flush();
                int longitud = dis.readInt();
                byte[] buffer = new byte[longitud];
                int size = buffer.length;
                LOGGER.info("El tamaño del archivo enviado es: " + size + " bytes\n");
                LOGGER.info("El nombre del archivo enviado es: " + "Archivo"+id+"Prueba"+numclientes+".txt");
                LOGGER.info("El cliente al que se envía el archivo es: " + clienteSocket.getInetAddress().getHostAddress() + "\n");
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
                    LOGGER.info("El hash calculado del archivo es: " + hashString + "\n");
                    String codhash = dis.readUTF();
                    LOGGER.info("El hash recibido del archivo es: " + codhash + "\n");
                    if(hashString.equals(codhash))
                    {
                        LOGGER.info("El archivo fue recibido correctamente" + "\n");
                        long tiempoFinal = System.currentTimeMillis();
                        long tiempoTotal = tiempoFinal - tiempoInicio;
                        LOGGER.info("El tiempo total de transferencia del archivo fue: "+tiempoTotal+" milisegundos" + "\n");
                    }
                    else
                    {
                        LOGGER.info("El archivo no fue recibido correctamente" + "\n");
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                
                try (FileOutputStream fos = new FileOutputStream("ArchivosRecibidos/"+"Archivo"+id+"Prueba"+numclientes+".txt")) {
                    fos.write(buffer);
                } catch (IOException e) {
                    LOGGER.info("Error al escribir el archivo recibido: " + e.getMessage());
                }
            } catch (IOException e) {
                LOGGER.info("Error al recibir el archivo: " + e.getMessage());
            }
        } catch (IOException e) {
            LOGGER.info("Error al conectar con el servidor: " + e.getMessage());
        }
        finally {
            //clienteSocket.close();
            System.out.println("Conexión cerrada");
        }
    }
}
