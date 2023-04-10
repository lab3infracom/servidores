import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.*;
import java.net.Socket;
import java.util.logging.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.nio.file.*;
import java.security.*;

public class ManejadorCliente implements Runnable {
    private Socket clienteSocket;
    private int numarchivo;
    private int conexiones;
    private BufferTCP mibuffer;
    private static Logger logger;

    public ManejadorCliente(Socket clienteSocket, int archivo,  BufferTCP mibuffer, int conexiones, Logger logger) {
        this.clienteSocket = clienteSocket;
        this.numarchivo = archivo;
        this.conexiones = conexiones;
        this.mibuffer = mibuffer;
        this.logger = logger;
    }

    public void run() {
        try {
            long tiempoInicio = System.currentTimeMillis();
            logger.info("---IDENTIFICADOR DEL CLIENTE: " + this.clienteSocket + "---");
            logger.info("Conexión establecida con el servidor " + clienteSocket.getInetAddress().getHostAddress() + ":" + clienteSocket.getPort());
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            File archivo = null;
            String nombreArchivo = "";
            long tamanoArchivo = 0;
            if(this.numarchivo == 1){
                archivo = new File("archivo_100Mb.txt");
                nombreArchivo = "archivo_100Mb.txt";
                tamanoArchivo = archivo.length();
            }
            else{
                archivo = new File("archivo_250Mb.txt");
                nombreArchivo = "archivo_250Mb.txt";
                tamanoArchivo = archivo.length();
            }
            logger.info("Se envía el archivo " + nombreArchivo + " de tamaño " + tamanoArchivo + " bytes");
            byte[] buffer = new byte[(int) archivo.length()];
            try (FileInputStream fis = new FileInputStream(archivo)) {
                fis.read(buffer);
                md.update(buffer, 0, (int) archivo.length());
                logger.info("Archivo " + nombreArchivo + " leído correctamente");
            } catch (IOException e) {
                logger.info("Error al leer el archivo " + nombreArchivo + ": " + e.getMessage());
                logger.info("La conexión no tuvo éxito");
                return;
            }
            try {
                DataOutputStream dos = new DataOutputStream(clienteSocket.getOutputStream());
                DataInputStream dis = new DataInputStream(clienteSocket.getInputStream());
                String mensajeConfirmacion = dis.readUTF();
                mibuffer.aumentar();
                while(mibuffer.dar() < conexiones){
                    Thread.yield();
                }
                dos.writeInt(buffer.length);
                dos.write(buffer);
                dos.flush();
                byte[] hashBytes = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b: hashBytes){
                    sb.append(String.format("%02x", b));
                }
                String hashString = sb.toString();
                logger.info("Archivo " + nombreArchivo + " enviado correctamente");
                logger.info("El hash del archivo " + nombreArchivo + " es: " + hashString);
                dos.writeUTF(hashString);
                logger.info("La conexión tuvo éxito");
                long tiempoFinal = System.currentTimeMillis();
                long tiempoTotal = tiempoFinal - tiempoInicio;
                logger.info("Tiempo total de transferencia para el archivo " + nombreArchivo + " a " + clienteSocket.getInetAddress().getHostAddress() + " es " + tiempoTotal + " milisegundos");
            } catch (IOException e) {
                System.out.println("Error al enviar el archivo: " + e.getMessage());
                logger.info("--NO TUVO EXITO LA CONEXION--");
            }
        } catch (Exception e) {
            System.out.println("Error al enviar el archivo: " + e.getMessage());
            logger.info("--NO TUVO EXITO LA CONEXION--");
        } 
        finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar la conexión del cliente: " + e.getMessage());
            }
        }
    }
}