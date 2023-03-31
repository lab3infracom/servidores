import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;


public class ServidorUDP {
    /************************************************* CONSTANTES ***********************************************/

    // Puerto para la conexion
    private static final int PUERTO = 43215;

    // Tamanio de un chunk (62KB)
    private static final int TAMANIO_CHUNK = 63488;

    // Numero maximo de conexiones concurrentes
    private static final int MAXIMO_CONEXIONES = 25;

    // Directorio donde se encuentran los archivos del servidor
    private static final String DIRECTORIO_ARCHIVOS = "./";

    // Logger
    private static final Logger LOGGER = Logger.getLogger("GENERAR LOG");

    private static Buffer colaServidor = new Buffer(MAXIMO_CONEXIONES);

    /************************************************* MAIN ***********************************************/

    public static void main(String[] args) throws IOException {

        // Generar log
        int anio = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        int dia = LocalDate.now().getDayOfMonth();
        int hora = LocalTime.now().getHour();
        int minuto = LocalTime.now().getMinute();
        int segundo = LocalTime.now().getSecond();
        FileHandler fh = new FileHandler(DIRECTORIO_ARCHIVOS + "Logs/" + anio + "-" + mes + "-" + dia + "-" + hora + "-" + minuto + "-" + segundo + "-log.txt");
        LOGGER.addHandler(fh);

        // Se obtiene el nombre del archivo que se va a transmitir
        System.out.println("--------------------------------------------------");
        System.out.println("ESCOJA EL ARCHIVO QUE QUIERE TRANSMITIR");
        System.out.println("1. Archivo de 100MB");
        System.out.println("2. Archivo de 250MB");
        Scanner myObj = new Scanner(System.in);
        int numArchivo = Integer.parseInt(myObj.nextLine());

        // Se crea el archivo que se va a transmitir
        String nombreArchivo;
        if (numArchivo == 1) {
            nombreArchivo = "archivo100MB.dat";
        } else {
            nombreArchivo = "archivo250MB.dat";
        }
        File archivo = new File(DIRECTORIO_ARCHIVOS + nombreArchivo);

        // Se crea el socket del servidor
        DatagramSocket servidorSocket = new DatagramSocket(PUERTO);

        while (true) {
            // Se espera a que llegue una conexion
            byte[] bufferRecepcion = new byte[TAMANIO_CHUNK];
            DatagramPacket paqueteRecepcion = new DatagramPacket(bufferRecepcion, TAMANIO_CHUNK);
            servidorSocket.receive(paqueteRecepcion);

            InetAddress direccionCliente = paqueteRecepcion.getAddress();
            int puertoCliente = paqueteRecepcion.getPort();

            // Se encola la conexion en la colaServidor
            Mensajero mensajero = new Mensajero(colaServidor, archivo, TAMANIO_CHUNK, direccionCliente, puertoCliente, servidorSocket, LOGGER);
            colaServidor.put(mensajero);
            mensajero.start();

            // Se eliminan de la colaServidor los mensajes que ya han sido procesados
            while (colaServidor.dar()==25) {
                Mensajero mensaje = colaServidor.get();
                if (mensaje != null && mensaje.isAlive()) {
                    colaServidor.put(mensaje);
                }
            }
        }
    }
}

