package Cliente;

import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClienteUDP {

    public static void main(String[] args) {
        int puertoServidor = 5000;
        InetAddress direccionServidor = null;
        try {
            direccionServidor = InetAddress.getByName("127.0.0.1");
            DatagramSocket socket = new DatagramSocket();
            String nombreArchivo = "archivo.txt";
            byte[] solicitud = nombreArchivo.getBytes();
            DatagramPacket paqueteSolicitud = new DatagramPacket(solicitud, solicitud.length, direccionServidor, puertoServidor);
            socket.send(paqueteSolicitud);
            byte[] buffer = new byte[1024];
            DatagramPacket paqueteRespuesta = new DatagramPacket(buffer, buffer.length);
            socket.receive(paqueteRespuesta);
            byte[] datosArchivo = paqueteRespuesta.getData();
            int longitudDatos = paqueteRespuesta.getLength();
            FileOutputStream archivoSalida = new FileOutputStream(nombreArchivo);
            archivoSalida.write(datosArchivo, 0, longitudDatos);
            archivoSalida.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}