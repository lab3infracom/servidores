import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Mensajero extends Thread {
    
    private static int idActual = 0;
    private final int ID;
    private final File ARCHIVO;
    private final int TAMANIO_CHUNK;
    private final InetAddress IP_CLIENTE;
    private final int PUERTO_CLIENTE;
    private final DatagramSocket SERVER_SOCKET;
    private final Logger LOGGER;

    public Mensajero(File archivo, int tamanioChunck, InetAddress ipCliente, int puertoCliente, DatagramSocket socket, Logger logger) {
        idActual++;
        this.ID = idActual;
        this.ARCHIVO = archivo;
        this.TAMANIO_CHUNK = tamanioChunck;
        this.IP_CLIENTE = ipCliente;
        this.PUERTO_CLIENTE = puertoCliente;
        this.SERVER_SOCKET = socket;
        this.LOGGER = logger;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Mensajero" + this.ID + "---CONEXION RECIBIDA de " + IP_CLIENTE + ":" + PUERTO_CLIENTE);
            LOGGER.info("Mensajero" + this.ID + "---ARCHIVO: " + ARCHIVO.getName() + " (" + Long.toString(ARCHIVO.length()) + " bytes)");
            
            FileInputStream fileInputStream = new FileInputStream(ARCHIVO);
            byte[] datosEnviados = new byte[TAMANIO_CHUNK];
            int bytesLeidos;

            long inicio = System.currentTimeMillis();
            int contador = 1;
            while ((bytesLeidos = fileInputStream.read(datosEnviados)) != -1) {
                DatagramPacket paqueteEnviado = new DatagramPacket(datosEnviados, bytesLeidos, IP_CLIENTE, PUERTO_CLIENTE);
                SERVER_SOCKET.setSendBufferSize(1024 * 1024); // aumenta el tamaño del buffer del socket
                SERVER_SOCKET.send(paqueteEnviado);
                LOGGER.info("Mensajero" + this.ID + "- " + contador + " DATAGRAMAS ENVIADOS");
                contador++;
            }
            fileInputStream.close();

            long fin = System.currentTimeMillis();
            long tiempo = fin - inicio;
            LOGGER.info("Mensajero" + this.ID + "-TIEMPO TOTAL DE CONEXION: "+ tiempo +" milisegundos");

            SERVER_SOCKET.close(); // cierra el socket

            System.out.println("Archivo enviado a " + IP_CLIENTE + ":" + PUERTO_CLIENTE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
