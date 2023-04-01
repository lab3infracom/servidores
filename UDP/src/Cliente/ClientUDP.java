package Cliente;
import java.net.*;
import java.io.*;
import java.util.*;

public class ClientUDP {
    private static final int MAX_PACKET_SIZE = 65507; // Maximum size of a UDP packet
    private static final int TIMEOUT = 5000; // Timeout for receiving packets (in milliseconds)
    
    private DatagramSocket socket; // The UDP socket
    private InetAddress serverAddress; // The address of the server
    private int serverPort; // The port used by the server
    
    public ClientUDP(String serverHost, int serverPort) throws UnknownHostException, SocketException {
        this.serverAddress = InetAddress.getByName(serverHost);
        this.serverPort = serverPort;
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(TIMEOUT);
    }
    
    public void downloadFile(int clientId, int numConnections) {
        try {
            // Create the directory to store the received files
            File dir = new File("FilesReceived");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Create the file to store the received data
            String fileName = String.format("Client%d-Test-%d.txt", clientId, numConnections);
            File file = new File(dir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            
            // Receive the file data from the server
            int receivedPackets = 0;
            long startTime = System.currentTimeMillis();
            while (true) {
                byte[] buffer = new byte[MAX_PACKET_SIZE];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                receivedPackets++;
                fos.write(buffer, 0, packet.getLength());
                if (packet.getLength() < MAX_PACKET_SIZE) {
                    // This is the last packet, so we stop receiving
                    break;
                }
            }
            long endTime = System.currentTimeMillis();
            long transferTime = endTime - startTime;
            
            // Log the result
            String logFileName = String.format("%s-log.txt", getTimestamp());
            File logFile = new File("Logs", logFileName);
            FileWriter fw = new FileWriter(logFile, true);
            fw.write(String.format("File: %s (size: %d bytes)\n", fileName, file.length()));
            if (receivedPackets > 0) {
                fw.write(String.format("Transfer successful. Received %d packets.\n", receivedPackets));
                fw.write(String.format("Transfer time: %d ms.\n", transferTime));
            } else {
                fw.write("Transfer failed. No packets received.\n");
            }
            fw.close();
            
            // Close the output stream and the socket
            fos.close();
            socket.close();
            
            System.out.println("File transfer completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getTimestamp() {
        Calendar now = Calendar.getInstance();
        return String.format("%d-%02d-%02d-%02d-%02d-%02d",
            now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH),
            now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
    }
}
