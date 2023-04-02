public class Buffer {
    private static int numClientes=0;

    public synchronized void conectar() {  
        System.out.println("Conectando cliente");
        if (numClientes == 25) {
            try {
                System.out.println("Esperando a que se desconecte un cliente");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        numClientes++;
    }

    public synchronized void desconectar() {
        System.out.println("Desconectando cliente");
        numClientes--;
        notify();
    }
}