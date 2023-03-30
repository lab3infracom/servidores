package UDP.Cliente;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClienteUDP {

    /************************************************* CONSTANTES ***********************************************/

    // Puerto para la conexion
    private static final int PUERTO = 43215;

    // Tamanio de un chunk
    private static final int TAMANIO_CHUNK = 1024;

    // TODO: IP del servidor
    private static final String IP_SERVIDOR = "XXX.XXX.XX.XX";

    /************************************************* MAIN ***********************************************/

    public static void main(String[] args) throws IOException {

        // Obtener numero de clientes concurrentes
        System.out.println("--------------------------------------------------");
        System.out.println("NUMERO DE CONEXIONES CONCURRENTES");
        Scanner myObj = new Scanner(System.in);
        int numConexiones = Integer.parseInt(myObj.nextLine());
        myObj.close();

        // Crear socket UDP y enviar solicitud de conexión al servidor
        DatagramSocket clientSocket = new DatagramSocket();
        byte[] datosConexion = "Conectar".getBytes();
        InetAddress ipServidor = InetAddress.getByName(IP_SERVIDOR);
        DatagramPacket connectPacket = new DatagramPacket(datosConexion, datosConexion.length, ipServidor, PUERTO);
        clientSocket.send(connectPacket);

        // Recibir el archivo desde el servidor
        String fileName = "ArchivosRecibidos/" + numCliente + "-Prueba-" + numConexiones +".txt";
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

        byte[] buffer = new byte[TAMANIO_CHUNK];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        while (receivePacket.getLength() >= TAMANIO_CHUNK) {
            clientSocket.receive(receivePacket);
            byte[] fileChunk = receivePacket.getData();
            bufferedOutputStream.write(fileChunk, 0, receivePacket.getLength());
        }

        System.out.println("Archivo recibido.");
        bufferedOutputStream.close();
        clientSocket.close();
    }
}