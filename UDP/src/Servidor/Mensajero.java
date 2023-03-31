import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// TODO - Revisar maximo 25 conexiones concurrentes
public class Mensajero extends Thread {
    
    /************************************************* CONSTANTES ***********************************************/

    // idActual
    private static int idActual = 0;

    // idMensajero
    private final int ID;

    // Archivo que se va a enviar
    private final File ARCHIVO;

    // Tamanio del chunck
    private final int TAMANIO_CHUNK;

    // Direccion IP del cliente
    private final InetAddress IP_CLIENTE;

    // Puerto del cliente
    private final int PUERTO_CLIENTE;

    // Socket del servidor
    private final DatagramSocket SERVER_SOCKET;

    // Logger
    private final Logger LOGGER;

    /************************************************* CONSTRUCTOR ***********************************************/

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

    /************************************************* RUN ***********************************************/
        
    @Override
    public void run() {
        try {
            LOGGER.info("CONEXION RECIBIDA de " + IP_CLIENTE + ":" + PUERTO_CLIENTE);
            LOGGER.info("ARCHIVO ENVIADO: " + ARCHIVO.getName() + " (" + Long.toString(ARCHIVO.length()) + " bytes)");
            
            byte[] nomArchivo = ARCHIVO.getName().getBytes();
            DatagramPacket paqueteNom = new DatagramPacket(nomArchivo, nomArchivo.length, IP_CLIENTE, PUERTO_CLIENTE);
            SERVER_SOCKET.send(paqueteNom);

            // Leer archivo y enviar en fragmentos
            FileInputStream fileInputStream = new FileInputStream(ARCHIVO);
            byte[] datosEnviados = new byte[TAMANIO_CHUNK];
            int bytesLeidos;

            // Se inicia el envio del archivo
            long inicio = System.currentTimeMillis();
            while ((bytesLeidos = fileInputStream.read(datosEnviados)) != -1) {
                byte[] datosChunck = Arrays.copyOfRange(datosEnviados, 0, bytesLeidos);
                DatagramPacket paqueteEnviado = new DatagramPacket(datosChunck, bytesLeidos, IP_CLIENTE, PUERTO_CLIENTE);
                SERVER_SOCKET.send(paqueteEnviado);
            }
            fileInputStream.close();

            long fin = System.currentTimeMillis();
            long tiempo = fin - inicio;

            LOGGER.info("TIEMPO TOTAL DE CONEXION (" + IP_CLIENTE + ":" + PUERTO_CLIENTE + "): "+ tiempo +" milisegundos");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
