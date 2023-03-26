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
    private static final Logger logger = Logger.getLogger("GeneradorLog");

    public ManejadorCliente(Socket clienteSocket, int archivo) {
        this.clienteSocket = clienteSocket;
        this.numarchivo = archivo;
    }

    public void run() {
        try {
            long tiempoInicio = System.currentTimeMillis();
            // Obtener el anio actual
            int anioActual = LocalDate.now().getYear();
            // Obtener el mes actual
            int mesActual = LocalDate.now().getMonthValue();
            // Obtener el dia actual
            int diaActual = LocalDate.now().getDayOfMonth();
            // Obtener la hora actual
            int horaActual = LocalTime.now().getHour();
            // Obtener el minuto actual
            int minutoActual = LocalTime.now().getMinute();
            // Obtener el segundo actual
            int segundoActual = LocalTime.now().getSecond();
            FileHandler fh = new FileHandler("Logs/"+anioActual+"-"+mesActual+"-"+diaActual+"-"+horaActual+"-"+minutoActual+"-"+segundoActual+"-log.log");
            logger.addHandler(fh);
            logger.info("---IDENTIFICADOR DEL CLIENTE: " + this.clienteSocket+"---");
            logger.info("Conexión establecida con el servidor " + clienteSocket.getInetAddress().getHostAddress() + ":" + clienteSocket.getPort());
            File archivo = new File("archivo_100Mb.txt");
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            if(this.numarchivo==1){
                archivo = new File("archivo_100Mb.txt");
                logger.info("SE HA ENVIADO EL ARCHIVO DE NOMBRE: archivo_100Mb.txt");
                logger.info("SE HA ENVIADO EL ARCHIVO DE TAMAÑO: "+archivo.length()+" bytes");
            }
            else{
                archivo = new File("archivo_250Mb.txt");
                logger.info("SE HA ENVIADO EL ARCHIVO DE NOMBRE: archivo_250Mb.txt");
                logger.info("SE HA ENVIADO EL ARCHIVO DE TAMAÑO: "+archivo.length()+" bytes");
            }
            byte[] buffer = new byte[(int) archivo.length()];
            try (FileInputStream fis = new FileInputStream(archivo)) {
                fis.read(buffer);
                md.update(buffer, 0, (int) archivo.length());
            } catch (IOException e) {
                System.out.println("Error al leer el archivo: " + e.getMessage());
                logger.info("--NO TUVO EXITO LA CONEXION--");
                logger.info("Error al leer el archivo: " + e.getMessage());

            }
            try {
                DataOutputStream dos = new DataOutputStream(clienteSocket.getOutputStream());
                DataInputStream dis = new DataInputStream(clienteSocket.getInputStream());
                String mensajeConfirmacion = dis.readUTF();
                dos.writeInt(buffer.length);
                dos.write(buffer);
                dos.flush();
                byte[] hashBytes = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b: hashBytes){
                    sb.append(String.format("%02x", b));
                }
                String hashString = sb.toString();
                logger.info("SE HA ENVIADO EL ARCHIVO DE HASH: "+hashString);
                dos.writeUTF(hashString);
                logger.info("--TUVO EXITO LA CONEXION--");
                long tiempoFinal = System.currentTimeMillis();
                long tiempoTotal = tiempoFinal - tiempoInicio;
                logger.info("TIEMPO TOTAL DE CONEXION: "+tiempoTotal+" milisegundos");
            } catch (IOException e) {
                System.out.println("Error al enviar el archivo: " + e.getMessage());
                logger.info("--NO TUVO EXITO LA CONEXION--");
            }
            fh.close();
        } catch (IOException | NoSuchAlgorithmException e) {
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