package Servidor;

import java.io.*;
import java.net.*;
import java.util.*;

public class FileManager {
    private String filename;
    private long fileSize;
    private ArrayList<InetAddress> clients;

    public FileManager(String filename) {
        this.filename = filename;
        this.clients = new ArrayList<>();
    }

    public void setFileSize() throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + filename + " does not exist.");
        }
        this.fileSize = file.length();
    }

    public void addClient(InetAddress clientAddress) {
        clients.add(clientAddress);
    }

    public void removeClient(InetAddress clientAddress) {
        clients.remove(clientAddress);
    }

    public void sendFile() throws IOException {
        int fragmentSize = 1024;
        byte[] buffer = new byte[fragmentSize];
        DatagramPacket packet;
        long startTime = 0;
        long endTime = 0;
        int fragmentCount = 0;
        int packetsSent = 0;

        // Open the file
        FileInputStream fileInput = new FileInputStream(filename);
        BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);

        // Send the file to all connected clients
        for (InetAddress clientAddress : clients) {
            // Send file metadata
            String metadata = filename + "," + fileSize;
            byte[] metadataBytes = metadata.getBytes();
            packet = new DatagramPacket(metadataBytes, metadataBytes.length, clientAddress, 5000);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();

            // Send file fragments
            fragmentCount = 0;
            packetsSent = 0;
            startTime = System.nanoTime();
            while (true) {
                int bytesRead = bufferedInput.read(buffer, 0, buffer.length);
                if (bytesRead == -1) {
                    // End of file
                    break;
                }
                fragmentCount++;
                packet = new DatagramPacket(buffer, bytesRead, clientAddress, 5000);
                socket = new DatagramSocket();
                socket.send(packet);
                packetsSent++;
                socket.close();
            }
            endTime = System.nanoTime();

            // Log transfer time for this client
            String logMessage = "Client: " + clientAddress.toString() + ", Fragments sent: " + fragmentCount + ", Packets sent: " + packetsSent + ", Time (ms): " + (endTime - startTime) / 1000000;
            logTransfer(logMessage);
        }

        // Close the file
        bufferedInput.close();
    }

    private void logTransfer(String message) {
        String logDirectory = "Logs";
        String logFilename = new Date().toString().replace(" ", "-").replace(":", "-") + "-log.txt";
        String logFilePath = logDirectory + File.separator + logFilename;
        try {
            File logDirectoryFile = new File(logDirectory);
            if (!logDirectoryFile.exists()) {
                logDirectoryFile.mkdir();
            }
            FileWriter writer = new FileWriter(logFilePath, true);
            writer.write(message + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] listFiles() {
        File directory = new File(filename);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(filename + " is not a directory.");
        }
        return directory.list();
    }
}
