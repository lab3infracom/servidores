import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;


public class ServidorTCP {
    private static final int PUERTO = 43215;
    private static int con_simultaneas =25;

    public static void main(String[] args) {
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
            System.out.println("Servidor iniciado en el puerto "+ PUERTO + " con " + con_simultaneas + " conexiones simultaneas");
            System.out.println("Esperando clientes...");
            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente);
                new Thread(new ManejadorCliente(cliente, numArchivo)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
