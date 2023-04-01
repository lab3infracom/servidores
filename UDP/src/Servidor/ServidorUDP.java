import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServidorUDP {

    public static void main(String[] args) {
        int puerto = 5000;
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(puerto);
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket paqueteEntrada = new DatagramPacket(buffer, buffer.length);
                socket.receive(paqueteEntrada);
                InetAddress direccionCliente = paqueteEntrada.getAddress();
                int puertoCliente = paqueteEntrada.getPort();
                byte[] datos = paqueteEntrada.getData();
                Thread hiloConexion = new Thread(new Runnable() {
                    public void run() {
                        DatagramPacket paqueteSalida = new DatagramPacket(datos, datos.length, direccionCliente, puertoCliente);
                        try {
                            socket.send(paqueteSalida);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                hiloConexion.start();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

}