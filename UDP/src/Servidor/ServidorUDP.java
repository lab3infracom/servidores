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
    // private static final int MAXIMO_CONEXIONES = 25;

    // Directorio donde se encuentran los archivos del servidor
    private static final String DIRECTORIO_ARCHIVOS = "./";

    // Logger
    private static final Logger LOGGER=Logger.getLogger("GENERAR LOG");
 
    /************************************************* MAIN ***********************************************/
    
    public static void main(String[] args) throws IOException {

        // Generar log
        int anio = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        int dia = LocalDate.now().getDayOfMonth();
        int hora = LocalTime.now().getHour();
        int minuto = LocalTime.now().getMinute();
        int segundo = LocalTime.now().getSecond();
        FileHandler fh = new FileHandler(DIRECTORIO_ARCHIVOS + "Logs/"+anio+"-"+mes+"-"+dia+"-"+hora+"-"+minuto+"-"+segundo+"-log.txt");
        LOGGER.addHandler(fh);
        // Se obtiene el nombre del archivo que se va a transmitir
        System.out.println("--------------------------------------------------");
        System.out.println("ESCOJA EL ARCHIVO QUE QUIERE TRANSMITIR");
        System.out.println("1. Archivo de 100MB");
        System.out.println("2. Archivo de 250MB");
        Scanner myObj = new Scanner(System.in);
        int numArchivo = Integer.parseInt(myObj.nextLine());
        // System.out.println("--------------------------------------------------");
        // System.out.println("NUMERO DE CLIENTES CONCURRENTES");
        // int numClientesConcurrentes = Integer.parseInt(myObj.nextLine());
        myObj.close();
        
        // Se crea el socket para la conexion
        DatagramSocket serverSocket = new DatagramSocket(PUERTO);
        // Se establece el buffer que almacena mensajes con el tamanio de un chunk
        byte[] buffer = new byte[TAMANIO_CHUNK];
        System.out.println("Servidor iniciado. Esperando conexiones...");
        
        while (true) {
            // Se crea un paquete que almacena el mensaje recibido
            DatagramPacket paqueteRecibido = new DatagramPacket(buffer, buffer.length);

            // Se espera hasta establecer la conxion con un cliente
            serverSocket.receive(paqueteRecibido);
            InetAddress ipCliente = paqueteRecibido.getAddress();
            int puertoCliente = paqueteRecibido.getPort();
            
            // Se obtiene el archivo solicitado por el cliente
            File archivo;
            if(numArchivo==1){
                archivo = new File(DIRECTORIO_ARCHIVOS + "archivo_100Mb.txt");
            } else {
                archivo = new File(DIRECTORIO_ARCHIVOS + "archivo_250Mb.txt");
            }

            // Se crea el Thread que se encarga de enviar el archivo al cliente
            Mensajero mensajero = new Mensajero(archivo, TAMANIO_CHUNK, ipCliente, puertoCliente, serverSocket, LOGGER);
            mensajero.start();
            
        }
    }

}