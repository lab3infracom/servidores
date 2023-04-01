import java.io.*;
import java.net.*;

import javax.sound.midi.Receiver;

public class UDPServer extends Thread{

    private static DatagramSocket serverSocket;

    private DatagramPacket receivePacket;

    private static String filename;

    private static byte[] sendData;

    public UDPServer(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }

    public void run() {
        try {
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
    
            FileInputStream fileInputStream = new FileInputStream(filename);
            int fileSize = fileInputStream.available();
            System.out.println("Tamanio archivo " + fileSize);
            int numReads = (int) Math.ceil((double) fileSize / (double) sendData.length);
    
            for (int i = 0; i < numReads; i++) {
                int bytesRead = fileInputStream.read(sendData);
                DatagramPacket sendPacket = new DatagramPacket(sendData, bytesRead, clientAddress, clientPort);
                UDPServer.serverSocket.send(sendPacket);
            }
            fileInputStream.close();
    
            DatagramPacket sendPacket = new DatagramPacket(new byte[0], 0, clientAddress, clientPort);
            UDPServer.serverSocket.send(sendPacket);
        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    public static void main(String[] args) throws IOException {
        // Leer la entrada del usuario para determinar qué archivo enviar
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("¿Qué archivo desea enviar? (100 o 250)");
        String fileChoice = reader.readLine();
        String filena = "archivo_" + fileChoice + "Mb.txt";
        UDPServer.filename = filena;
        
        // Configurar el socket UDP
        UDPServer.serverSocket = new DatagramSocket(46345);
        UDPServer.sendData = new byte[1024];
        
        while (true) {
            // Esperar conexiones de clientes y enviar el archivo
            byte[] receivData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receivData, receivData.length);
            serverSocket.receive(receivePacket);

            UDPServer serverThread = new UDPServer(receivePacket);
            serverThread.start();
            
        }
    }
}