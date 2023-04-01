package Servidor;

public class Main {
    public static void main(String[] args) {
        int port = 5000;
        FileTransferServer server = new FileTransferServer(port);
        server.start();
    }
}
