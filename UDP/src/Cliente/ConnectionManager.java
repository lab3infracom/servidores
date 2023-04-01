package Cliente;

import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionManager {
    private final int MAX_PACKET_SIZE = 65507; // Maximum size of a UDP packet
    private final int MAX_CONNECTIONS = 25; // Maximum number of concurrent connections
    private final int TIMEOUT = 5000; // Timeout in milliseconds for waiting for a response from the server
    private DatagramSocket socket; // Socket for sending and receiving UDP packets
    private List<Integer> connections; // List of active connections
    private InetAddress serverAddress; // IP address of the server
    private int serverPort; // Port number of the server

    public ConnectionManager(String serverIp, int serverPort) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket();
        this.connections = new ArrayList<Integer>();
        this.serverAddress = InetAddress.getByName(serverIp);
        this.serverPort = serverPort;
    }

    public void connect() throws Exception {
        if (this.connections.size() >= MAX_CONNECTIONS) {
            throw new Exception("Maximum number of connections reached");
        }

        // Send a connection request to the server
        byte[] requestData = "connect".getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, this.serverAddress, this.serverPort);
        this.socket.send(requestPacket);

        // Wait for a response from the server
        byte[] responseData = new byte[MAX_PACKET_SIZE];
        DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
        this.socket.receive(responsePacket);

        String responseString = new String(responsePacket.getData(), 0, responsePacket.getLength());
        int connectionId = Integer.parseInt(responseString);

        // Add the connection to the list of active connections
        this.connections.add(connectionId);
    }

    public void disconnect(int connectionId) throws Exception {
        if (!this.connections.contains(connectionId)) {
            throw new Exception("Invalid connection ID");
        }

        // Send a disconnection request to the server
        byte[] requestData = ("disconnect " + connectionId).getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, this.serverAddress, this.serverPort);
        this.socket.send(requestPacket);

        // Wait for a response from the server
        byte[] responseData = new byte[MAX_PACKET_SIZE];
        DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
        this.socket.receive(responsePacket);

        String responseString = new String(responsePacket.getData(), 0, responsePacket.getLength());
        if (!responseString.equals("disconnect")) {
            throw new Exception("Unexpected response from server");
        }

        // Remove the connection from the list of active connections
        this.connections.remove(new Integer(connectionId));
    }

    public boolean isConnected(int connectionId) {
        return this.connections.contains(connectionId);
    }
}
