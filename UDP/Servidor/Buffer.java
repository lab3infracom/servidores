import java.util.logging.Logger;

public class Buffer {
    private static int numClientes=0;

    private static Logger LOGGER;

    public void getLog(Logger log) {
        LOGGER = log;
    }
    public synchronized void conectar() {  
        System.out.println("Conectando cliente");
        if (numClientes == 25) {
            try {
                LOGGER.log(java.util.logging.Level.INFO, "[BUFFER] Se ha alcanzado el numero maximo de clientes concurrentes, se pone una conexion en espera");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        numClientes++;
    }

    public synchronized void desconectar() {
        numClientes--;
        notify();
    }
}