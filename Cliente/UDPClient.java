package Cliente;

import java.io.*;
import java.net.*;

public class UDPClient extends Thread {

    private static final String IP_SERVER = "192.168.152.117";

    private final int ID;

    private static int numClients;

    public UDPClient(int id) {
        this.ID = id;
    }

    public void run() {
        try {
            // Configurar el socket UDP
            DatagramSocket clientSocket = new DatagramSocket();
            byte[] receiveData = new byte[1024];
            
            // Conectarse al servidor
            InetAddress serverAddress = InetAddress.getByName(IP_SERVER);
            int serverPort = 46345;
            DatagramPacket sendPacket = new DatagramPacket(new byte[0], 0, serverAddress, serverPort);
            clientSocket.send(sendPacket);
            
            // Recibir el archivo del servidor
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String filename = "Cliente/ArchivosRecibidos/" + ID + "-Prueba-" + numClients + ".txt";
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            
            while (receivePacket.getLength() > 0) {
                fileOutputStream.write(receivePacket.getData(), 0, receivePacket.getLength());
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
            }
            
            fileOutputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        // Leer la entrada del usuario para determinar el número de clientes concurrentes
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("¿Cuántos clientes desea ejecutar?");
        numClients = Integer.parseInt(reader.readLine());
        
        // Iniciar tantos hilos de cliente como se indique en la entrada del usuario.
        for (int i = 1; i <= numClients; i++) {
            UDPClient client = new UDPClient(i);
            client.start();
        }
    }
}