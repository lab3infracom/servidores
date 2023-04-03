import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class UDPServer extends Thread{

    private static DatagramSocket serverSocket;

    private static String filename;

    private static byte[] sendData;

    private static final Logger LOGGER = Logger.getLogger("LOG");

    public static final int PUERTO_TCP_SERVIDOR = 46345;

    public static final int PUERTO_UDP_SERVIDOR = 46346;

    public static final int TAM_CHUNK = 1024;

    public static Buffer buffer = new Buffer();

    public InetAddress IP_CLIENTE;

    public int PUERTO_CLIENTE;

    public UDPServer(InetAddress clientAddress, int clientPort) {
        IP_CLIENTE = clientAddress;
        PUERTO_CLIENTE = clientPort;
    }

    public void run() {

        try {
            buffer.conectar();

            String clientePrt = IP_CLIENTE + ":" + PUERTO_CLIENTE;
    
            FileInputStream fileInputStream = new FileInputStream(filename);
            int fileSize = fileInputStream.available();
            System.out.println("Enviando archivo " + filename + " (" + fileSize + " bytes) al cliente " + clientePrt);
            LOGGER.log(java.util.logging.Level.INFO, "[INFO] Enviando archivo " + filename + " (" + fileSize + " bytes) al cliente " + clientePrt);
            int numReads = (int) Math.ceil((double) fileSize / (double) sendData.length);
    
            long tiempoInicio = System.currentTimeMillis();
            for (int i = 0; i < numReads; i++) {
                int bytesRead = fileInputStream.read(sendData);
                DatagramPacket sendPacket = new DatagramPacket(sendData, bytesRead, IP_CLIENTE, PUERTO_CLIENTE);
                UDPServer.serverSocket.send(sendPacket);
            }
            long tiempoFinal = System.currentTimeMillis();
            fileInputStream.close();
            
            // Se envia un paquete vacio para indicar que se termino de enviar el archivo
            DatagramPacket sendPacket = new DatagramPacket(new byte[0], 0, IP_CLIENTE, PUERTO_CLIENTE   );
            UDPServer.serverSocket.send(sendPacket);
            
            long tiempoTotal = tiempoFinal - tiempoInicio;
            LOGGER.log(java.util.logging.Level.INFO, "[FIN] Tiempo de envio del archivo" + filename + " al cliente (" + clientePrt + ") fue de " + tiempoTotal + " ms");

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
        UDPServer.serverSocket = new DatagramSocket(PUERTO_UDP_SERVIDOR);
        UDPServer.sendData = new byte[TAM_CHUNK];
        
        while (true) {

            LOGGER.log(java.util.logging.Level.INFO, "[INICIO] Esperando conexiones...");

            //Obtener en TCP el nombre del archivo a enviar
            ServerSocket serverSocketTCP = new ServerSocket(PUERTO_TCP_SERVIDOR);
            Socket clientSocket = serverSocketTCP.accept();
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String[] data = input.readLine().split(",");
            filename = data[0];
            int puerto = Integer.parseInt(data[1]);

            LOGGER.log(java.util.logging.Level.INFO, "[INFO] solicitud de conexion recibida");
            
            // Servidor
            InetAddress clientAddress = clientSocket.getLocalAddress();
            System.out.println("Cliente conectado: " + clientAddress + ":" + puerto);
            
            UDPServer serverThread = new UDPServer(clientAddress,puerto);
            serverThread.start();

            input.close();
            clientSocket.close();
            serverSocketTCP.close();
        }
    }
}