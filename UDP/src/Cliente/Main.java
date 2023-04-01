package Cliente;

public class Main{
    public static void main(String[] args) {
    String serverHost = "localhost"; // Replace with the actual server host name or IP address
    int serverPort = 12345; // Replace with the actual server port number
    int clientId = 1; // Replace with the actual client ID
    int numConnections = 2; // Replace with the actual number of connections
    
    try {
        ClientUDP client = new ClientUDP(serverHost, serverPort);
        client.downloadFile(clientId, numConnections);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}


