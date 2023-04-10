import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.*;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;


public class ServidorTCP {
    private static final int PUERTO = 43215;
    private static int con_simultaneas = 25;
    private static final Logger LOGGER = Logger.getLogger("GENERAR LOG");
    private static BufferTCP buffer = new BufferTCP();

    public static void main(String[] args) throws SecurityException, IOException {
        int anioActual = LocalDate.now().getYear();
        int mesActual = LocalDate.now().getMonthValue();
        int diaActual = LocalDate.now().getDayOfMonth();
        int horaActual = LocalTime.now().getHour();
        int minutoActual = LocalTime.now().getMinute();
        int segundoActual = LocalTime.now().getSecond();
        FileHandler fh = new FileHandler("Logs/"+anioActual+"-"+mesActual+"-"+diaActual+"-"+horaActual+"-"+minutoActual+"-"+segundoActual+"-log.log");
        fh.setFormatter(new CustomFormatter());
        LOGGER.addHandler(fh);

        LOGGER.log(java.util.logging.Level.INFO, "[INICIO] solicitud de conexion recibida");

        System.out.println("--------------------------------------------------");
        System.out.println("ESCOJA EL ARCHIVO QUE QUIERE TRANSMITIR");
        System.out.println("1. Archivo de 100MB");
        System.out.println("2. Archivo de 250MB");
        Scanner myObj = new Scanner(System.in);
        int numArchivo = Integer.parseInt(myObj.nextLine());
        System.out.println("--------------------------------------------------");
        System.out.println("ESCOJA EL NUMERO DE CONEXIONES SIMULTANEAS");
        Scanner myObj2 = new Scanner(System.in);
        System.out.println("Coloque el numero de conexiones simultaneas");
        con_simultaneas = Integer.parseInt(myObj2.nextLine());

        ServerSocket servidor = null;
        Socket sc = null;
        DataInputStream in;
        DataOutputStream out;
        try {
            servidor = new ServerSocket(PUERTO, con_simultaneas);
            LOGGER.log(java.util.logging.Level.INFO, "[INFO] Servidor iniciado en el puerto "+ PUERTO + " con " + con_simultaneas + " conexiones simultaneas");
            LOGGER.log(java.util.logging.Level.INFO, "[INFO] Esperando clientes...");
            while (true) {
                Socket cliente = servidor.accept();
                String clientAddress = cliente.getInetAddress().toString();
                int clientPort = cliente.getPort();
                LOGGER.log(java.util.logging.Level.INFO, "[INFO] Cliente conectado: " + clientAddress + ":" + clientPort);
                new Thread(new ManejadorCliente(cliente, numArchivo, buffer, con_simultaneas, LOGGER)).start();
            }
        } catch (IOException ex) {
            LOGGER.warning("[ERROR] Catch" + ex.getMessage());
        }
    }
}
