import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class UDPServer extends Thread{

    private static DatagramSocket socketUDP;

    private DatagramPacket receivePacket;

    private static String filename;

    private static byte[] sendData;

    private static final Logger LOGGER = Logger.getLogger("LOG");

    public static final int PUERTO_SERVIDOR_TCP = 46345;

    public static final int PUERTO_SERVIDOR_UDP = 46346;

    public final int PUERTO_CLIENTE_UDP;

    public static final int TAM_CHUNK = 1024;

    public static Buffer buffer = new Buffer();

    public UDPServer(DatagramPacket receivePacket, int puertoClienteUDP) {
        this.PUERTO_CLIENTE_UDP = puertoClienteUDP;
        this.receivePacket = receivePacket;
    }

    public void run() {

        try {
            buffer.conectar();

            // Servidor
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            String clientePrt = clientAddress + ":" + clientPort;
    
            FileInputStream fileInputStream = new FileInputStream(filename);
            int fileSize = fileInputStream.available();
            System.out.println("Enviando archivo " + filename + " (" + fileSize + " bytes) al cliente " + clientePrt);
            LOGGER.log(java.util.logging.Level.INFO, "[INFO] Enviando archivo " + filename + " (" + fileSize + " bytes) al cliente " + clientePrt);
            int numReads = (int) Math.ceil((double) fileSize / (double) sendData.length);
    
            long tiempoInicio = System.currentTimeMillis();
            for (int i = 0; i < numReads; i++) {
                int bytesRead = fileInputStream.read(sendData);
                DatagramPacket sendPacket = new DatagramPacket(sendData, bytesRead, clientAddress, clientPort);
                socketUDP.send(sendPacket);
            }
            long tiempoFinal = System.currentTimeMillis();
            fileInputStream.close();
            
            // Se envia un paquete vacio para indicar que se termino de enviar el archivo
            DatagramPacket sendPacket = new DatagramPacket(new byte[0], 0, clientAddress, clientPort);
            socketUDP.send(sendPacket);
            
            long tiempoTotal = tiempoFinal - tiempoInicio;
            LOGGER.log(java.util.logging.Level.INFO, "[FIN] Tiempo de envio del archivo al cliente (" + clientAddress + ":" + clientPort+ ") fue de " + tiempoTotal + " ms");

            buffer.desconectar();
        } catch (IOException e) {
            System.out.println("Error Catch" + e.getMessage());
            LOGGER.warning("[ERROR] Catch" + e.getMessage());
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
        FileHandler fh = new FileHandler("Logs/"+anio+"-"+mes+"-"+dia+"-"+hora+"-"+minuto+"-"+segundo+"-log.log");
        fh.setFormatter(new CustomFormatter());
        LOGGER.addHandler(fh);

        buffer.getLog(LOGGER);
        
        // Configurar el socket UDP
        ServerSocket socketTCP = new ServerSocket(PUERTO_SERVIDOR_TCP);
        DatagramSocket socketUDP = new DatagramSocket(PUERTO_SERVIDOR_UDP);
        UDPServer.sendData = new byte[TAM_CHUNK];
        
        // Creamos un socket de servidor TCP
        Socket conexionTCP = socketTCP.accept();
        BufferedReader entradaTCP = new BufferedReader(new InputStreamReader(conexionTCP.getInputStream()));
        String fileChoice = entradaTCP.readLine();
        filename = "archivo_" + fileChoice + "Mb.txt";
        
        while (true) {
            
            int puertoClienteUDP = Integer.parseInt(entradaTCP.readLine());
            
            // Esperar conexiones de clientes y enviar el archivo
            byte[] receivData = new byte[TAM_CHUNK];
            DatagramPacket receivePacket = new DatagramPacket(receivData, receivData.length);
            
            System.out.println("Esperando conexiones...");
            socketUDP.receive(receivePacket);
            LOGGER.log(java.util.logging.Level.INFO, "[INICIO] solicitud de conexion recibida");
            UDPServer serverThread = new UDPServer(receivePacket, puertoClienteUDP);
            serverThread.start();
            
        }

    }
}