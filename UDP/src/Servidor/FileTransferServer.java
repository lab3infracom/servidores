package Servidor;
import java.io.*;
import java.net.*;

public class FileTransferServer {
    private static final int BUFFER_SIZE = 64 * 1024;
    private static final int MAX_CONNECTIONS = 25;
    private static final String LOGS_DIRECTORY = "Logs/";
    private DatagramSocket socket;
    private int port;
    private String fileName;
    private int fileSize;

    public FileTransferServer(int port) {
        this.port = port;
    }
    
    public FileTransferServer(String fileName) {
        this.fileName = fileName;
    }

    public void start() {
        try {
            // Create the socket and bind it to the specified port
            socket = new DatagramSocket(port);
            System.out.println("Server started on port " + port);

            // Wait for incoming connections
            while (true) {
                if (getConnectionCount() < MAX_CONNECTIONS) {
                    receiveFile();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void receiveFile() {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            // Receive the filename and size from the client
            socket.receive(packet);
            String[] fileInfo = new String(packet.getData(), 0, packet.getLength()).split(",");
            fileName = fileInfo[0];
            fileSize = Integer.parseInt(fileInfo[1]);
            System.out.println("Received file: " + fileName + " (" + fileSize + " bytes)");

            // Create the log file
            File logsDirectory = new File(LOGS_DIRECTORY);
            if (!logsDirectory.exists()) {
                logsDirectory.mkdirs();
            }
            String logFileName = LOGS_DIRECTORY + getLogFileName();
            File logFile = new File(logFileName);
            logFile.createNewFile();
            PrintWriter logWriter = new PrintWriter(new FileWriter(logFile));

            // Send the file to the client in fragments
            FileInputStream fileInput = new FileInputStream(fileName);
            int totalBytesSent = 0;
            int bytesSent;
            while (totalBytesSent < fileSize) {
                bytesSent = fileInput.read(buffer);
                if (bytesSent == -1) {
                    break;
                }
                packet.setData(buffer, 0, bytesSent);
                socket.send(packet);
                totalBytesSent += bytesSent;
            }

            // Close the log file
            logWriter.printf("File transfer completed in %.2f seconds\n", (double)totalBytesSent/BUFFER_SIZE);
            logWriter.close();
            System.out.println("File transfer completed in " + (double)totalBytesSent/BUFFER_SIZE + " seconds");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private int getConnectionCount() {
        return Thread.activeCount() - 1;
    }

    private String getLogFileName() {
        String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new java.util.Date());
        return timeStamp + "-log.txt";
    }

    public void join() throws InterruptedException {
        if (socket != null) {
            socket.close();
        }
    }
}
