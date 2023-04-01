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
    private static final String DIRECTORIO_ARCHIVOS = "./src/Cliente/";

    // Puerto para la conexion
    private static final int PUERTO = 43215;

    // Tamanio de un chunk (62KB)
    private static final int TAMANIO_CHUNK = 63488;

    // TODO: IP del servidor
    private static final String IP_SERVIDOR = "192.168.5.119";

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
        System.out.println("Cliente " + this.ID + " iniciado");
        // Crear socket UDP y enviar solicitud de conexión al servidor
        DatagramSocket clientSocket;
        try {
            // Se establece la conexión enviando un mensaje al servidor
            clientSocket = new DatagramSocket();
            byte[] datosConexion = "Conectar".getBytes();
            InetAddress direcServidor = InetAddress.getByName(IP_SERVIDOR);
            DatagramPacket connectPacket = new DatagramPacket(datosConexion, datosConexion.length, direcServidor, PUERTO);
            clientSocket.send(connectPacket);
            System.out.println(this.ID + "-Paquete de conexion enviado al servidor: " + connectPacket.getAddress() + ":" + connectPacket.getPort());

            // Se crea el archivo para almacenar la respuesta del servidor
            FileOutputStream outputFile = new FileOutputStream(this.RUTA);
            byte[] buffer = new byte[TAMANIO_CHUNK];
            DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);
            // Recibir respuesta del servidor
            int contador = 1;
            while (true) {
                System.out.println(this.ID + "-Esperando paquete del servidor...");
                clientSocket.receive(paqueteRecibido);
                System.out.println(this.ID + "-Recibido paquete " + contador + " del servidor...");
                outputFile.write(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
                // System.out.println(paqueteRecibido.getLength() + " bytes recibidos, " + buffer.length + " bytes esperados");
                contador++;
                if (paqueteRecibido.getLength() < buffer.length) {
                    // Si el paquete recibido es más corto que el búfer, significa que se ha alcanzado el final del archivo
                    System.out.println(this.ID + "-No hay mas datos que recibir");
                    break;
                }
            }
            
            // Cerrar el archivo de salida y el socket
            outputFile.close();
            clientSocket.close();
            System.out.println(this.ID + "-FIN");


        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor: " + e.getMessage());
        }

    }

}
