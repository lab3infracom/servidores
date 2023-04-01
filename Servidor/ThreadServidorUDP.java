import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ThreadServidorUDP extends Thread {

    private String filename;
    private DatagramSocket socketServidor;
    private byte[] sendData;
    private InetAddress direccionCliente;
    private int PuertoCliente;
    
    public ThreadServidorUDP(String filename, DatagramSocket socketServidor, byte[] sendData, InetAddress direccionCliente, int PuertoCliente) {
        this.filename = filename;
        this.socketServidor = socketServidor;
        this.sendData = sendData;
        this.direccionCliente = direccionCliente;
        this.PuertoCliente = PuertoCliente;
    }

    @Override
    public void run() {

        // Se lee el archivo y se determina el numero de paquetes a enviar
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(filename);
            int fileSize = fileInputStream.available();
            int numReads = (int) Math.ceil((double) fileSize / (double) sendData.length);

            //Se envían los paquetes
            for (int i = 0; i < numReads; i++) {
                int bytesRead = fileInputStream.read(sendData);
                DatagramPacket sendPacket = new DatagramPacket(sendData, bytesRead, direccionCliente, PuertoCliente);
                socketServidor.send(sendPacket);
            }
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
