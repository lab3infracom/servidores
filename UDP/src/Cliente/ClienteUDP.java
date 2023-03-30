package Cliente;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

public class ClienteUDP extends Thread {

    /************************************************* CONSTANTES ***********************************************/

    // Directorio donde se encuentran los archivos del servidor
    private static final String DIRECTORIO_ARCHIVOS = "UDP/Servidor/";

    // Puerto para la conexion
    private static final int PUERTO = 43215;

    // Tamanio de un chunk
    private static final int TAMANIO_CHUNK = 1024;

    // TODO: IP del servidor
    private static final String IP_SERVIDOR = "192.168.5.116";

    /************************************************* CONSTANTES ***********************************************/

    private final int ID;
    private final int NUM_CONEXIONES;
    private final Logger LOGGER;
    private final String RUTA;

    /************************************************* CONSTRUCTOR ***********************************************/

    public ClienteUDP(int id, int numclientes, Logger logger) {
        this.ID = id;
        this.NUM_CONEXIONES = numclientes;
        this.LOGGER = logger;
        this.RUTA = DIRECTORIO_ARCHIVOS + "ArchivosRecibidos/" + ID + "-Prueba-" + NUM_CONEXIONES + ".txt";
    }

    /************************************************* RUN ***********************************************/

    @Override
    public void run() {

        // Crear socket UDP y enviar solicitud de conexión al servidor
        DatagramSocket clientSocket;
        try {
            // Se establece la conexión enviando un mensaje al servidor
            clientSocket = new DatagramSocket();
            byte[] datosConexion = "Conectar".getBytes();
            InetAddress direcServidor = InetAddress.getByName(IP_SERVIDOR);
            DatagramPacket connectPacket = new DatagramPacket(datosConexion, datosConexion.length, direcServidor, PUERTO);
            clientSocket.send(connectPacket);

            // Se crea el archivo para almacenar la respuesta del servidor
            FileOutputStream outputFile = new FileOutputStream(this.RUTA);
            byte[] buffer = new byte[TAMANIO_CHUNK];
            DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
            // Recibir respuesta del servidor
            while (true) {
                clientSocket.receive(paqueteRecibido);
                outputFile.write(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
                if (paqueteRecibido.getLength() < buffer.length) {
                    break;
                }
            }
            
            // Cerrar el archivo de salida y el socket
            outputFile.close();
            clientSocket.close();


        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }

    }

}