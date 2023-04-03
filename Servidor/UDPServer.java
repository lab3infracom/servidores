import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class UDPServer extends Thread{

    private static DatagramSocket serverSocket;

    private DatagramPacket receivePacket;

    private static String filename;

    private static byte[] sendData;

    private static final Logger LOGGER = Logger.getLogger("LOG");

    public static final int PUERTO_SERVIDOR = 46345;

    public static final int TAM_CHUNK = 1024;

    public static Buffer buffer = new Buffer();

    public UDPServer(DatagramPacket receivePacket) {
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
                UDPServer.serverSocket.send(sendPacket);
            }
            long tiempoFinal = System.currentTimeMillis();
            fileInputStream.close();
            
            // Se envia un paquete vacio para indicar que se termino de enviar el archivo
            DatagramPacket sendPacket = new DatagramPacket(new byte[0], 0, clientAddress, clientPort);
            UDPServer.serverSocket.send(sendPacket);
            
            long tiempoTotal = tiempoFinal - tiempoInicio;
            LOGGER.log(java.util.logging.Level.INFO, "[INFO] Tiempo de envio del archivo al cliente (" + clientAddress + ":" + clientPort+ ") fue de " + tiempoTotal + " ms");

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

        // Leer la entrada del usuario para determinar qué archivo enviar
        System.out.println("--------------------------------------------------");
        System.out.println("ESCOJA EL ARCHIVO QUE QUIERE TRANSMITIR");
        System.out.println("100. Archivo de 100MB");
        System.out.println("250. Archivo de 250MB");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileChoice = reader.readLine();
        UDPServer.filename = "archivo_" + fileChoice + "Mb.txt";
        System.out.println("--------------------------------------------------");

        // Configurar el socket UDP
        UDPServer.serverSocket = new DatagramSocket(PUERTO_SERVIDOR);
        UDPServer.sendData = new byte[TAM_CHUNK];
        
        while (true) {
            // Esperar conexiones de clientes y enviar el archivo
            byte[] receivData = new byte[TAM_CHUNK];
            DatagramPacket receivePacket = new DatagramPacket(receivData, receivData.length);

            System.out.println("Esperando conexiones...");
            serverSocket.receive(receivePacket);

            UDPServer serverThread = new UDPServer(receivePacket);
            serverThread.start();
        }
    }
}