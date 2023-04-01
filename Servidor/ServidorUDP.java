package Servidor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServidorUDP {

    /***************** Constantes *****************/

    private final static int PUERTO = 43215;

    private static final int TAM_CHUNK = 1024;

    public static void main(String[] args) throws IOException {

        // Se determina que archivo se va a enviar
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("/ ****************** ¿Qué archivo desea enviar? ********************** \\");
        System.out.println("1. Archivo de 100MB");
        System.out.println("2. Archivo de 250MB");
        int fileChoice = Integer.parseInt(reader.readLine());
        String nomArchivo;
        System.out.println("/ ******************************************************************** \\");


        if(fileChoice == 1) {
            nomArchivo = "archivo_100Mb.txt";
        } else {
            nomArchivo = "archivo_100Mb.txt";
        }
        
        // Configurar el socket UDP
        DatagramSocket socketServidor = new DatagramSocket(PUERTO);
        byte[] sendData = new byte[TAM_CHUNK];
        System.out.println("Servidor iniciado. Esperando conexiones...");
        
        while (true) {
            // Esperar conexiones de clientes y enviar el archivo
            byte[] datosRecibido = new byte[TAM_CHUNK];
            DatagramPacket paqueteRecibido = new DatagramPacket(datosRecibido, datosRecibido.length);
            socketServidor.receive(paqueteRecibido);
            
            // Se establece la IP y el puerto del cliente que envió el paquete
            InetAddress direccionCliente = paqueteRecibido.getAddress();
            int PuertoCliente = paqueteRecibido.getPort();
            
            ThreadServidorUDP threadServidorUDP = new ThreadServidorUDP(nomArchivo, socketServidor, sendData, direccionCliente, PuertoCliente);
            threadServidorUDP.start();
        }
    }
}
