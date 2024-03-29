package Cliente;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class UDPClient extends Thread {

    private static final Logger LOGGER = Logger.getLogger("LOG");

    private static final String IP_SERVER = "192.168.1.112";

    public static final int PUERTO_SERVIDOR = 46345;

    public static final int TAM_CHUNK = 1024;

    public static final String DIR_ARCHIVOS = "Cliente/";

    public static int numClients;

    private final int ID;

    public UDPClient(int id) {
        this.ID = id;
    }

    public void run() {
        try {

            LOGGER.log(java.util.logging.Level.INFO, "[INFO] Cliente " + ID + " iniciado");

            // Configurar el socket UDP
            DatagramSocket clientSocket = new DatagramSocket();
            byte[] receiveData = new byte[TAM_CHUNK];
            
            // Conectarse al servidor
            InetAddress serverAddress = InetAddress.getByName(IP_SERVER);
            DatagramPacket sendPacket = new DatagramPacket(new byte[0], 0, serverAddress, PUERTO_SERVIDOR);
            clientSocket.send(sendPacket);
            
            // Recibir el archivo del servidor
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            long tiempoInicio = System.currentTimeMillis();
            LOGGER.log(java.util.logging.Level.INFO, "[INFO] Cliente " + ID + " Conexion establecida con el servidor");

            String filename = DIR_ARCHIVOS + "ArchivosRecibidos/" + ID + "-Prueba-" + numClients + ".txt";
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            
            int tamanio = receivePacket.getLength();
            while (receivePacket.getLength() > 0) {
                fileOutputStream.write(receivePacket.getData(), 0, receivePacket.getLength());
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                tamanio += receivePacket.getLength();
                clientSocket.receive(receivePacket);
            }
            long tiempoFinal = System.currentTimeMillis();

            long tiempoTotal = tiempoFinal - tiempoInicio;
            String nombreArchivo = ""; //TODO: Obtener el nombre del archivo
            boolean transferenciaExitosa = true; // Se asume que la transferencia fue exitosa
            if (tamanio == 0) {
                transferenciaExitosa = false;
            }
            LOGGER.log(java.util.logging.Level.INFO, "[INFO] Cliente " + ID + " Tiempo de recepcion del archivo " + nombreArchivo + " (" + tamanio + " bytes) fue de " + tiempoTotal + " ms. Transferencia exitosa: " + transferenciaExitosa);
            
            fileOutputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        // Generar Log
        int anio = LocalDate.now().getYear();
        int mes = LocalDate.now().getMonthValue();
        int dia = LocalDate.now().getDayOfMonth();
        int hora = LocalTime.now().getHour();
        int minuto = LocalTime.now().getMinute();
        int segundo = LocalTime.now().getSecond();
        FileHandler fh = new FileHandler(DIR_ARCHIVOS + "Logs/"+anio+"-"+mes+"-"+dia+"-"+hora+"-"+minuto+"-"+segundo+"-log.log");
        fh.setFormatter(new CustomFormatter());
        LOGGER.addHandler(fh);
        
        // Leer la entrada del usuario para determinar el número de clientes concurrentes
        System.out.println("--------------------------------------------------");
        System.out.println("CLIENTES QUE SE QUIEREN EJECUTAR");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        numClients =  Integer.parseInt(reader.readLine());
        System.out.println("--------------------------------------------------");
        
        // Iniciar tantos hilos de cliente como se indique en la entrada del usuario.
        for (int i = 1; i <= numClients; i++) {
            UDPClient client = new UDPClient(i);
            client.start();
        }
    }
}