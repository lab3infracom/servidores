import java.io.*;
import java.net.*;

public class UDPServer {
    public static void main(String[] args) throws IOException {
        // Leer la entrada del usuario para determinar qué archivo enviar
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("¿Qué archivo desea enviar? (1 o 2)");
        String fileChoice = reader.readLine();
        // String filename = "archivo" + fileChoice + ".txt";
        String filename = "archivo_100Mb.txt";
        
        // Configurar el socket UDP
        DatagramSocket serverSocket = new DatagramSocket(46345);
        byte[] sendData = new byte[1024];
        
        while (true) {
            // Esperar conexiones de clientes y enviar el archivo
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            
            FileInputStream fileInputStream = new FileInputStream(filename);
            int fileSize = fileInputStream.available();
            System.out.println("Tamanio archivo " + fileSize);
            int numReads = (int) Math.ceil((double) fileSize / (double) sendData.length);
            
            for (int i = 0; i < numReads; i++) {
                int bytesRead = fileInputStream.read(sendData);
                DatagramPacket sendPacket = new DatagramPacket(sendData, bytesRead, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
            fileInputStream.close();

            DatagramPacket sendPacket = new DatagramPacket(new byte[0], 0, clientAddress, clientPort);
            serverSocket.send(sendPacket);
        }
    }
}